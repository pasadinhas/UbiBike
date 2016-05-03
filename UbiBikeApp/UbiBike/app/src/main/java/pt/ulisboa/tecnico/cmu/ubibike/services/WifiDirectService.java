package pt.ulisboa.tecnico.cmu.ubibike.services;

import android.app.AlertDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
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
import pt.ulisboa.tecnico.cmu.ubibike.data.DatabaseManager;
import pt.ulisboa.tecnico.cmu.ubibike.data.UserLoginData;
import pt.ulisboa.tecnico.cmu.ubibike.domain.User;
import pt.ulisboa.tecnico.cmu.ubibike.wifidirect.UbiBroadcastReceiver;

import static android.content.Intent.getIntent;

public class WifiDirectService extends Service implements
        SimWifiP2pManager.PeerListListener, SimWifiP2pManager.GroupInfoListener {

    public static final String TAG = "ubibike";
    private String username = null;

    private SimWifiP2pManager mManager = null;
    private SimWifiP2pManager.Channel mChannel = null;
    private Messenger mService = null;
    private boolean mBound = false;
    private SimWifiP2pSocketServer mSrvSocket = null;
    private SimWifiP2pSocket mCliSocket = null;
    private UbiBroadcastReceiver mReceiver;
    private Set<String> peerIPs = new TreeSet<>();
    private Map<String, String> peers = new HashMap<>();
    private Map<String, String> peerIPbyNames = new HashMap<>();
    private String deviceName = null;
    private Pair<String, String> ownID = new Pair<>(null, null);

    public WifiDirectService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        username = (String) intent.getSerializableExtra("USERNAME");
        initializeWifiDirect();
        bindWifiDirect();

        return 0;
    }

    private void initializeWifiDirect() {
        SimWifiP2pSocketManager.Init(getBaseContext());

        // register broadcast receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION);
        mReceiver = new UbiBroadcastReceiver(WifiDirectService.this);
        registerReceiver(mReceiver, filter);
    }

    private void bindWifiDirect() {
        Intent intent = new Intent(getBaseContext(), SimWifiP2pService.class);
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
            mManager.requestPeers(mChannel, WifiDirectService.this);
        } else {
            Toast.makeText(getBaseContext(), "Service not bound", Toast.LENGTH_SHORT).show();
        }
    }

    private void findPeerNames() {
        for (String ip : peerIPs) {
            exchangeIdentity(ip);
        }
    }

    private void broadcastPeerIdentities() {
        for (String name : peerIPbyNames.keySet()) {
            tellPeerName(name, peerIPbyNames.get(name));
        }
    }

    private void tellPeerName(String name, String ip) {
        String exprotocol = "[Protocol]MEMANTINE " + name + " " + ip;

        new TellPeerNameTask().executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR,
                exprotocol, ip);
    }

    private void exchangeIdentity(String ip) {
        String exprotocol = "[Protocol]AHOY " + username + " " + ownID.second;

        new ExchangeIdTask().executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR,
                exprotocol, ip);
    }

    private void registerOwnID(String name, String ip) {
        ownID = new Pair<>(name, ip);

        Log.d(TAG, "registerOwnID: registered!");
        Log.d(TAG, "registerOwnID: name: " + name + " IP:" + ip);

        findPeerNames();

        //Toast.makeText(getApplicationContext(), "I am " + name + " " + ip, Toast.LENGTH_SHORT).show();
    }

    private void registerPeer(String msg, String ip) {
        final String[] parts = msg.split(" ");
        if (parts[0].equals("[Protocol]AHOY")) {
            peers.put(ip, parts[1]);
            peerIPbyNames.put(parts[1], ip);
            DatabaseManager.getInstance(getBaseContext()).insertPeer(ip, parts[1]);
            Log.d(TAG, "registerPeer: registered: " + peers.get(ip) + " ip: " + ip );
        }
    }

    @Override
    public void onGroupInfoAvailable(SimWifiP2pDeviceList simWifiP2pDeviceList, SimWifiP2pInfo info) {
        deviceName = info.getDeviceName();
        Log.d(TAG, "onGroupInfoAvailable: " + deviceName);
    }

    @Override
    public void onPeersAvailable(SimWifiP2pDeviceList peers) {
        mManager.requestGroupInfo(mChannel, WifiDirectService.this);
        for (SimWifiP2pDevice device : peers.getDeviceList()) {
            String ip = device.getVirtIp();
            peerIPs.add(ip);
            peerIPbyNames.put(device.deviceName, ip);
        }

        broadcastPeerIdentities();
        //findPeerNames();
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
                        String response = "";
                        BufferedReader sockIn = new BufferedReader(
                                new InputStreamReader(sock.getInputStream()));

                        String st = sockIn.readLine();
                        Log.d(TAG, "incommingCommTask: line read: " + st);
                        String[] parts = st.split(" ");
                        if (parts[0].equals("[Protocol]AHOY")) {
                            registerPeer(st, parts[2]);
                            response = "[Protocol]AHOY " + username;
                        }
                        else if (parts[0].equals("[Protocol]MEMANTINE")) {
                            registerOwnID(parts[1], parts[2]);
                            findPeerNames();
                        }
                        else if (parts[0].equals("[Protocol]MESSAGE")) {
                            DatabaseManager.getInstance(getBaseContext()).insertMessage(parts[1], "correct this " + parts[2]);
                            Log.d(TAG, "IncommingCommTask msg received from: " + parts[1]);
                        }

                        publishProgress(st);
                        sock.getOutputStream().write((response + "\n").getBytes());

                    } catch (IOException e) {
                        Log.d("Error reading socket:", e.getMessage());
                    } finally {
                        sock.close();
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

    public class TellPeerNameTask extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... msg) {
            try {
                mCliSocket = new SimWifiP2pSocket(msg[1],
                        Integer.parseInt(getString(R.string.port)));

                mCliSocket.getOutputStream().write((msg[0] + "\n").getBytes());
                BufferedReader sockIn = new BufferedReader(
                        new InputStreamReader(mCliSocket.getInputStream()));
                String response = sockIn.readLine();
                //publishProgress(response, msg[1]);

                mCliSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mCliSocket = null;
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

    public class ExchangeIdTask extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... msg) {
            Log.d(TAG, "exchangeIdTask: starting to exchange id: " + msg[0]);
            try {
                mCliSocket = new SimWifiP2pSocket(msg[1],
                        Integer.parseInt(getString(R.string.port)));

                mCliSocket.getOutputStream().write((msg[0] + "\n").getBytes());
                BufferedReader sockIn = new BufferedReader(
                        new InputStreamReader(mCliSocket.getInputStream()));
                String response = sockIn.readLine();
                Log.d(TAG, "exchangeIdTask: received response: " + response);
                publishProgress(response, msg[1]);

                mCliSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mCliSocket = null;
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
