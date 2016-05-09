package pt.ulisboa.tecnico.cmu.ubibike.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by cedac on 09/05/16.
 */
public class BikeStatusData extends PreferencesData {
    private static final String PREF_NEAR_BIKE= "is_enabled";

    public static void setIsNear (Context ctx, boolean status) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_NEAR_BIKE, status).apply();
        Log.d("UbiBike", "Near Bike: set to " + status);
    }

    public static boolean getIsNear (Context ctx) {
        return getSharedPreferences(ctx).getBoolean(PREF_NEAR_BIKE, false);
    }

}
