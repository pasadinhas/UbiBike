package pt.ulisboa.tecnico.cmu.ubibikebeacon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pInfo;

/**
 * Created by cedac on 08/05/16.
 */
public class BeaconBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

        } else if (SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {


        } else if (SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION.equals(action)) {


        } else if (SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION.equals(action)) {

        }
    }
}
