package pt.ulisboa.tecnico.cmu.ubibike.activities;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pInfo;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.service.SimWifiP2pService;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketManager;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketServer;
import pt.ulisboa.tecnico.cmu.ubibike.R;
import pt.ulisboa.tecnico.cmu.ubibike.data.UserLoginData;
import pt.ulisboa.tecnico.cmu.ubibike.domain.User;
import pt.ulisboa.tecnico.cmu.ubibike.wifidirect.UbiBroadcastReceiver;

public class NearbyUsersActivity extends BaseDrawerActivity implements
        SimWifiP2pManager.PeerListListener, SimWifiP2pManager.GroupInfoListener {

    public static final String TAG = "ubibike";

    private SimWifiP2pManager mManager = null;
    private SimWifiP2pManager.Channel mChannel = null;
    private Messenger mService = null;
    private boolean mBound = false;
    private SimWifiP2pSocketServer mSrvSocket = null;
    private SimWifiP2pSocket mCliSocket = null;
    private TextView mTextInput;
    private TextView mTextOutput;
    private UbiBroadcastReceiver mReceiver;
    private Set<String> peerIPs = new TreeSet<>();
    private Map<String, String> peers = new HashMap<>();
    private Map<String, String> peerIPbyNames = new HashMap<>();
    private String deviceName = null;

    @Override
    protected int getPosition() {
        return 1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_users);

        final Button findPeersButton = (Button) findViewById(R.id.find_peers_button);
        findPeersButton.setVisibility(View.GONE);

        Switch wifiDirectSwitch = (Switch) findViewById(R.id.switch_wifi_direct);

        wifiDirectSwitch.setChecked(false);

        wifiDirectSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    initializeWifiDirect();
                    bindWifiDirect();

                    findPeersButton.setVisibility(View.VISIBLE);

                    Toast toast = Toast.makeText(getApplicationContext(), "Wifi Direct Enabled", Toast.LENGTH_SHORT);
                    toast.show();

                } else {
                    findPeersButton.setVisibility(View.GONE);
                    unregisterReceiver(mReceiver);
                    unbindService(mConnection);
                    mBound = false;

                    Toast toast = Toast.makeText(getApplicationContext(), "Wifi Direct Disabled", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    private void initializeWifiDirect() {
        SimWifiP2pSocketManager.Init(getApplicationContext());

        // register broadcast receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION);
        mReceiver = new UbiBroadcastReceiver(NearbyUsersActivity.this);
        registerReceiver(mReceiver, filter);
    }

    private void bindWifiDirect() {
        Intent intent = new Intent(getApplicationContext(), SimWifiP2pService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        mBound = true;

        // spawn the chat server background task
        new IncommingCommTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void findPeers(View view) {
        updatePeers();
    }

    public void updatePeers() {
        if (mBound) {
            mManager.requestPeers(mChannel, NearbyUsersActivity.this);
        } else {
            Toast.makeText(getApplicationContext(), "Service not bound", Toast.LENGTH_SHORT).show();
        }
    }

    private void findPeerNames() {
        for (String ip : peerIPs) {
            connectToPeer(ip);
            exchangeIdentity(ip);
            disconnectFromPeer();
        }
    }

    private void exchangeIdentity(String ip) {
        User user = UserLoginData.getUser(getApplicationContext());
        String exprotocol = "[Protocol]AHOY " + user.getUsername() + " \n";

        new SendCommTask().executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR,
                exprotocol, ip);
    }

    private void registerPeer(String msg, String ip) {
        String[] parts = msg.split(" ");
        if (parts[0].equals("[Protocol]Ahoy")) {
            peers.put(ip, parts[1]);
        }
    }

    private void connectToPeer(String IP) {
        new OutgoingCommTask().executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR,
                IP);
    }

    private void disconnectFromPeer() {
        if (mCliSocket != null) {
            try {
                mCliSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mCliSocket = null;
    }

    @Override
    public void onGroupInfoAvailable(SimWifiP2pDeviceList simWifiP2pDeviceList, SimWifiP2pInfo info) {
        deviceName = info.getDeviceName();
        Log.d(TAG, "onGroupInfoAvailable: " + deviceName);
    }

    @Override
    public void onPeersAvailable(SimWifiP2pDeviceList peers) {
        mManager.requestGroupInfo(mChannel, NearbyUsersActivity.this);
        for (SimWifiP2pDevice device : peers.getDeviceList()) {
            String ip = device.getVirtIp();
            peerIPs.add(ip);
            peerIPbyNames.put(device.deviceName, ip);
        }
        //findPeerNames();

        //Test print
        String output = "";
        for (String ip : peerIPs) {
            output += ip + "\n";
        }
        new AlertDialog.Builder(this)
                .setTitle("Devices in WiFi Range")
                .setMessage(output + deviceName)
                .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        // callbacks for service binding, passed to bindService()

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
            mManager = new SimWifiP2pManager(mService);
            mChannel = mManager.initialize(getApplication(), getMainLooper(), null);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mService = null;
            mManager = null;
            mChannel = null;
            mBound = false;
        }
    };

    /*
	 * Asynctasks implementing message exchange
	 */

    public class IncommingCommTask extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Log.d(TAG, "IncommingCommTask started (" + this.hashCode() + ").");

            try {
                mSrvSocket = new SimWifiP2pSocketServer(
                        Integer.parseInt(getString(R.string.port)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    SimWifiP2pSocket sock = mSrvSocket.accept();
                    try {
                        BufferedReader sockIn = new BufferedReader(
                                new InputStreamReader(sock.getInputStream()));
                        while (true) {
                            String st = sockIn.readLine();
                            String[] parts = st.split(" ");
                            if (parts[0].equals("[Protocol]AHOY")) {
                                //registerPeer(st, mManager.requestGroupInfo(mChannel, NearbyUsersActivity.this));
                            }

                            publishProgress(st);
                            sock.getOutputStream().write(("\n").getBytes());
                        }
                    } catch (IOException e) {
                        Log.d("Error reading socket:", e.getMessage());
                    } finally {
                        //sock.close();
                    }
                } catch (IOException e) {
                    Log.d("Error socket:", e.getMessage());
                    break;
                    //e.printStackTrace();
                }
            }
            return null;
        }
    }

    public class OutgoingCommTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {}

        @Override
        protected String doInBackground(String... params) {
            try {
                mCliSocket = new SimWifiP2pSocket(params[0],
                        Integer.parseInt(getString(R.string.port)));
            } catch (UnknownHostException e) {
                return "Unknown Host:" + e.getMessage();
            } catch (IOException e) {
                return "IO error:" + e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {}
    }

    public class SendCommTask extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... msg) {
            try {
                mCliSocket.getOutputStream().write((msg[0] + "\n").getBytes());
                BufferedReader sockIn = new BufferedReader(
                        new InputStreamReader(mCliSocket.getInputStream()));
                String response = sockIn.readLine();
                publishProgress(response, msg[1]);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            registerPeer(values[0], values[1]);
        }

        @Override
        protected void onPostExecute(Void result) {}
    }
}
