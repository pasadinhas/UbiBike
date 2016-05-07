package pt.ulisboa.tecnico.cmu.ubibike.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by cedac on 07/05/16.
 */
public class WifiDirectData extends PreferencesData {
    private static final String PREF_IS_ENABLED = "is_enabled";
    private static final String PREF_IS_FIRST_TIME = "is_first_time";

    public static void setIsEnabled (Context ctx, boolean status) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_IS_ENABLED, status).apply();
        Log.d("UbiBike", "setIsEnabled: trying to " + status);
    }

    public static boolean getIsEnabled (Context ctx) {
        return getSharedPreferences(ctx).getBoolean(PREF_IS_ENABLED, false);
    }

    public static void setIsFirst(Context ctx, boolean status) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_IS_FIRST_TIME, status).apply();
    }

    public static boolean getIsFirst(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(PREF_IS_FIRST_TIME, true);
    }
}
