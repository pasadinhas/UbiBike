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
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
import pt.ulisboa.tecnico.cmu.ubibike.data.DatabaseManager;
import pt.ulisboa.tecnico.cmu.ubibike.data.UserLoginData;
import pt.ulisboa.tecnico.cmu.ubibike.data.WifiDirectData;
import pt.ulisboa.tecnico.cmu.ubibike.domain.User;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UserServiceREST;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UtilREST;
import pt.ulisboa.tecnico.cmu.ubibike.services.WifiDirectService;
import pt.ulisboa.tecnico.cmu.ubibike.wifidirect.UbiBroadcastReceiver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearbyUsersActivity extends BaseDrawerActivity implements
        Runnable {
    public static final String TAG = "UbiBike";

    private User user = null;

    private Handler handler;

    private Map<String, String> peerIPbyNames = new HashMap<>();

    @Override
    protected int getPosition() {
        return 1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_users);

        handler = new Handler();

        user = UserLoginData.getUser(getApplicationContext());

        Switch wifiDirectSwitch = (Switch) findViewById(R.id.switch_wifi_direct);

        if (WifiDirectData.getIsEnabled(this)) {
            wifiDirectSwitch.setChecked(true);
        } else {
            wifiDirectSwitch.setChecked(false);
        }

        wifiDirectSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent serviceIntent = new Intent(getBaseContext(), WifiDirectService.class);
                    serviceIntent.putExtra("USERNAME", user.getUsername());
                    startService(serviceIntent);
                    WifiDirectData.setIsEnabled(getApplicationContext(), true);
                } else {
                    stopService(new Intent(getBaseContext(), WifiDirectService.class));
                    WifiDirectData.setIsEnabled(getApplicationContext(), false);
                    DatabaseManager.getInstance(getBaseContext()).clearPeers();
                    peerIPbyNames.clear();
                }
            }
        });

        ListView listView = (ListView)findViewById(R.id.nearby_listView);
        listView.setAdapter(new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, new ArrayList<String>()));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String username = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(getBaseContext(), ChatActivity.class);
                intent.putExtra("OtherUsername", username);
                intent.putExtra("OtherIP", peerIPbyNames.get(username));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Switch wifiDirectSwitch = (Switch) findViewById(R.id.switch_wifi_direct);

        if (WifiDirectData.getIsEnabled(this)) {
            wifiDirectSwitch.setChecked(true);
            Log.d(TAG, "onResume: wifiDirect is enabled");
        } else {
            wifiDirectSwitch.setChecked(false);
            Log.d(TAG, "onResume: wifiDirect is NOT enabled");
        }

        new GetEventTask().execute();

    }

    @Override
    public void run() {
        new GetEventTask().execute();
    }

    private class GetEventTask extends AsyncTask<Void, Void, Event>{
        @Override
        protected Event doInBackground(Void... params) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Event event = new Event();
            event.name = "Google I/O " + System.currentTimeMillis();
            event.nextStartTime = System.currentTimeMillis() + 10000;

            return event;
        }

        @Override
        protected void onPostExecute(Event event) {
            ListView listView = (ListView)findViewById(R.id.nearby_listView);
            Set<Pair<String, String>> peers = DatabaseManager.getInstance(getBaseContext()).getPeersSet();
            peerIPbyNames.clear();
            for (Pair<String, String> peerAndIp : peers) {
                peerIPbyNames.put(peerAndIp.second, peerAndIp.first);
            }

            listView.setAdapter(new ArrayAdapter<>(getBaseContext(),
                    android.R.layout.simple_list_item_1,
                    new ArrayList<>(peerIPbyNames.keySet())));

            handler.postDelayed(NearbyUsersActivity.this, event.nextStartTime - System.currentTimeMillis());
        }
    }


    private class Event {
        public String name;
        public long nextStartTime;
    }
}
