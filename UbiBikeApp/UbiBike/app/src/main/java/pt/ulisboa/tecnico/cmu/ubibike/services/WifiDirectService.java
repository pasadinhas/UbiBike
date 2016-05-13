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
import android.os.DropBoxManager;
import android.os.Handler;
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
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import okhttp3.ResponseBody;
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
import pt.ulisboa.tecnico.cmu.ubibike.UbiApp;
import pt.ulisboa.tecnico.cmu.ubibike.activities.ChatActivity;
import pt.ulisboa.tecnico.cmu.ubibike.activities.MyBikeActiviy;
import pt.ulisboa.tecnico.cmu.ubibike.activities.NearbyUsersActivity;
import pt.ulisboa.tecnico.cmu.ubibike.data.BikeStatusData;
import pt.ulisboa.tecnico.cmu.ubibike.data.DatabaseManager;
import pt.ulisboa.tecnico.cmu.ubibike.data.GeofenceData;
import pt.ulisboa.tecnico.cmu.ubibike.data.StationsData;
import pt.ulisboa.tecnico.cmu.ubibike.data.UserLoginData;
import pt.ulisboa.tecnico.cmu.ubibike.data.WifiDirectData;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Coordinates;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Station;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Trajectory;
import pt.ulisboa.tecnico.cmu.ubibike.domain.User;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UserServiceREST;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UtilREST;
import pt.ulisboa.tecnico.cmu.ubibike.services.gps.track.GpsTrackingService;
import pt.ulisboa.tecnico.cmu.ubibike.wifidirect.UbiBroadcastReceiver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.getIntent;

public class WifiDirectService extends Service implements
        SimWifiP2pManager.PeerListListener, SimWifiP2pManager.GroupInfoListener {

    private ChatActivity chatListener = null;

    public static final String TAG = "ubibike";
    private String username = null;

    private SimWifiP2pManager mManager = null;
    private SimWifiP2pManager.Channel mChannel = null;
    private Messenger mService = null;
    private boolean mBound = false;
    private SimWifiP2pSocketServer mSrvSocket = null;
    private SimWifiP2pSocket mCliSocket = null;
    private UbiBroadcastReceiver mReceiver;
    private Set<String> currentPeerIPs = new TreeSet<>();
    private Map<String, String> peers = new HashMap<>();
    private Map<String, String> peerIPbyNames = new HashMap<>();
    private String deviceName = null;
    private Pair<String, String> ownID = new Pair<>(null, null);

    private MyBikeActiviy bikeListener = null;

    private static WifiDirectService instance = null;

    public WifiDirectService() {
        instance = this;
    }

    public static WifiDirectService getInstance() {
        return instance;
    }

    public static Boolean isRunning() {
        return instance != null;
    }

    public void setChatListener(ChatActivity listener) {
        chatListener = listener;
    }

    public void setBikeListener(MyBikeActiviy listener) {bikeListener = listener;}

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

    @Override
    public void onDestroy() {
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
            unregisterReceiver(mReceiver);
        }
        instance = null;
        WifiDirectData.setIsEnabled(this, false);
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

    public void updatePeers() {

        Log.d(TAG, "updatePeers: entered!");
        if (mBound) {
            mManager.requestPeers(mChannel, WifiDirectService.this);
        } else {
            Toast.makeText(getBaseContext(), "Service not bound", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateGroup() {
        if (mBound) {
            mManager.requestGroupInfo(mChannel, WifiDirectService.this);
        } else {
            Toast.makeText(getBaseContext(), "Service not bound", Toast.LENGTH_SHORT).show();
        }
    }

    private void findPeerNames() {
        for (String ip : peerIPbyNames.values()) {
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
            DatabaseManager.getInstance(getBaseContext()).insertPeer(ip, parts[1]);
            Log.d(TAG, "registerPeer: registered: " + peers.get(ip) + " ip: " + ip );
        }
    }

    @Override
    public void onGroupInfoAvailable(SimWifiP2pDeviceList peers, SimWifiP2pInfo info) {
        if (! info.askIsConnected()) {
            DatabaseManager.getInstance(this).clearPeers();
            peerIPbyNames.clear();
        }

        deviceName = info.getDeviceName();
        Log.d(TAG, "onGroupInfoAvailable: " + deviceName);

        currentPeerIPs.clear();

        for (SimWifiP2pDevice device : peers.getDeviceList()) {
            if (device.deviceName.equals(deviceName) || device.deviceName.startsWith("Bike")) {
                continue;
            }
            String ip = device.getVirtIp();
            currentPeerIPs.add(ip);
            peerIPbyNames.put(device.deviceName, ip);
            Log.d(TAG, "onGroupInfoAvailable: regsietred:" + device.deviceName);
        }

        removeOldPeers();

        Log.d(TAG, "onGroupInfo: Starting boradcast");
        broadcastPeerIdentities();
    }

    @Override
    public void onPeersAvailable(SimWifiP2pDeviceList peers) {

        if (UbiApp.getInstance().getUser().hasBike()) {
            boolean previousStateOfIsNear = BikeStatusData.getIsNear(this);
            boolean bikeIsNear = false;
            boolean possibleDropInTwoPhase = GeofenceData.getInstance().getPossibleDropOffWhenLeave();
            boolean leftStation = false;
            boolean seenStation = false;
            String stationName = GeofenceData.getInstance().getCurrentStation();

            for (SimWifiP2pDevice device : peers.getDeviceList()) {
                if (device.deviceName.startsWith("Bike")) {
                    Log.d(TAG, "onPeersAvailable: seen bike: " + device.deviceName);

                    if (device.deviceName.substring(4).equals(UbiApp.getInstance().getUser().getReservedBike().getIdentifier())){
                        bikeIsNear = true;
                    }
                }
                if (device.deviceName.startsWith("Station")) {
                    Log.d(TAG, "onPeersAvailable: seen station: " + device.deviceName + " and is on a station: " + leftStation);
                    leftStation = manageSationFencing(device.deviceName);
                    seenStation = true;
                }

            }

            if (GeofenceData.getInstance().isInStation() && !seenStation) {
                GeofenceData.getInstance().leaveStation();
                leftStation = true;
            }

            Log.d(TAG, "onPeersAvailable: bike was near: " + BikeStatusData.getIsNear(this) + " left station: " + leftStation
                    + " bike IS near: " + bikeIsNear);

            if (previousStateOfIsNear && leftStation && !bikeIsNear) {
                Log.d(TAG, "onPeersAvailable: booked for removal");
                processDropOff(stationName);
            }

            if (possibleDropInTwoPhase && leftStation && !bikeIsNear) {
                Log.d(TAG, "onPeersAvailable: booked for removal");
                processDropOff(stationName);
            }

            if (previousStateOfIsNear != bikeIsNear) {
                if (bikeIsNear) {
                    startService(new Intent(getBaseContext(), GpsTrackingService.class));
                    if ( ! BikeStatusData.isPicked(this)) {
                        processPickUp();
                    }
                } else {
                    stopService(new Intent(getBaseContext(), GpsTrackingService.class));
                }
            }

            if (!bikeIsNear && !leftStation) {
                GeofenceData.getInstance().setPossibleDropOffWhenLeave(true);
            } else {
                GeofenceData.getInstance().setPossibleDropOffWhenLeave(false);
            }


            BikeStatusData.setIsNear(this, bikeIsNear);
        }

        if (bikeListener != null) {
            bikeListener.update();
        }
    }

    private void processPickUp() {
        BikeStatusData.setIsPicked(this, true);
        User user = UbiApp.getInstance().getUser();
        UserServiceREST service = UtilREST.getRetrofit().create(UserServiceREST.class);
        Call<ResponseBody> call = service.pickBike(user.getUsername(), user.getReservedBike().getIdentifier());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getBaseContext(), "Bike picked",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(), R.string.impossible_connect_server_toast,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void processDropOff(String stationName) {
        Log.d(TAG, "processDropOff: Station Name:" + stationName);
        User user = UbiApp.getInstance().getUser();
        String bikeId = user.getReservedBike().getIdentifier();
        user.dropBike();
        UserLoginData.setUser(this, user);

        Trajectory t = user.getAllTrajectories().get(0);
        Coordinates c = null;
        if (t.getTrajectory().size() > 3) {
            c = t.getTrajectory().get(t.getTrajectory().size() - 3);
        } else {
            Station currentStation = null;

            for (Station station : StationsData.getStations(this)) {
                Log.d(TAG, "processDropOff: iterating station:" + station.getName());
                if (station.getName().equals(stationName)) {
                    currentStation = station;
                }
            }

            Random r = new Random();
            double randomValue = -0.0002 + (0.0003 + 0.0002) * r.nextDouble();
            c = currentStation.getPosition();
            c.setLatitude(c.getLatitude()+randomValue);
            c.setLongitude(c.getLongitude()+randomValue);
        }

        UserServiceREST service = UtilREST.getRetrofit().create(UserServiceREST.class);
        Call<ResponseBody> call = service.dropBike(UtilREST.ACCEPT_HEADER, UtilREST.CONTENT_TYPE_HEADER,
                bikeId, stationName, c);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getBaseContext(), "Bike dropped",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(), R.string.impossible_connect_server_toast,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean manageSationFencing(String deviceName) {
        String stationName = deviceName.substring(7);
        if (GeofenceData.getInstance().isInStation()) {
            if (GeofenceData.getInstance().getCurrentStation().equals(stationName)) {
                return false;
            } else {
                GeofenceData.getInstance().enterStation(stationName);
                return true;
            }
        }
        else {
            GeofenceData.getInstance().enterStation(stationName);
            return false;
        }
    }

    private void removeOldPeers() {
        for(Map.Entry<String, String> oldPeer : peerIPbyNames.entrySet()) {
            if (! currentPeerIPs.contains(oldPeer.getValue())) {
                peerIPbyNames.remove(oldPeer.getKey());
                DatabaseManager.getInstance(this).removePeer(oldPeer.getValue());
            }
        }
    }

    private void registerPoints(final String username, final String pointsStr) {
        long points = Long.parseLong(pointsStr);
        User user = UbiApp.getInstance().getUser();
        user.addUserPoints(points);

        UserLoginData.setUser(this, user);

        Handler h = new Handler(WifiDirectService.this.getMainLooper());

        h.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(WifiDirectService.this, "Received " + pointsStr + " from " + username, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void toastMessage(final String username) {
        Handler h = new Handler(WifiDirectService.this.getMainLooper());

        h.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(WifiDirectService.this, "Received new message from " + username, Toast.LENGTH_LONG).show();
            }
        });
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
                        String[] parts = st.split(" ", 3);
                        if (parts[0].equals("[Protocol]AHOY")) {
                            registerPeer(st, parts[2]);
                            response = "[Protocol]AHOY " + username;
                        }
                        else if (parts[0].equals("[Protocol]MEMANTINE")) {
                            registerOwnID(parts[1], parts[2]);
                            findPeerNames();
                        }
                        else if (parts[0].equals("[Protocol]MESSAGE")) {
                            DatabaseManager.getInstance(getBaseContext()).insertMessage(parts[1], parts[2]);
                            if (chatListener != null) {
                                chatListener.updateChat();
                            } else {
                                toastMessage(parts[1]);
                            }
                            Log.d(TAG, "IncommingCommTask msg received from: " + parts[1]);
                        }
                        else if (parts[0].equals("[Protocol]POINTS")) {
                            registerPoints(parts[1], parts[2]);
                        }

                        publishProgress(st);
                        sock.getOutputStream().write((response + "\n").getBytes());

                    } catch (IOException e) {
                        Log.d("Error reading socket:", e.getMessage());
                    } catch (NullPointerException e) {
                        Log.d("Error reading socket:", " null (quick disconnect?)" + e.getMessage());
                    } finally {
                        sock.close();
                    }
                } catch (IOException e) {
                    Log.d("Error socket:", e.getMessage());
                    break;
                    //e.printStackTrace();
                } catch (NullPointerException e) {
                    Log.d("Error reading socket:", " null (quick disconnect?)" + e.getMessage());
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

                Log.d(TAG, "doInBackground: going to broadcast " + msg[0]);

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

                Log.d(TAG, "doInBackground: going to broadcast " + msg[0]);

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
