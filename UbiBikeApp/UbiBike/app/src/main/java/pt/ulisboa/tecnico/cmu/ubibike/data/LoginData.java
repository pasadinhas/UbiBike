package pt.ulisboa.tecnico.cmu.ubibike.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public abstract class LoginData {

    private static final String PREF_LOGGEDIN_USER_USERNAME = "logged_in_username";
    private static final String PREF_USER_LOGGEDIN_STATUS = "logged_in_status";

    private static SharedPreferences getSharedPreferences(Context ctx)
    {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setLoggedIn(Context ctx, String username)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LOGGEDIN_USER_USERNAME, username);
        editor.putBoolean(PREF_USER_LOGGEDIN_STATUS,true);
        editor.apply();
    }

    public static String getLoggedInUsername(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_LOGGEDIN_USER_USERNAME, "");
    }

    public static boolean getUserLoggedInStatus(Context ctx)
    {
        return getSharedPreferences(ctx).getBoolean(PREF_USER_LOGGEDIN_STATUS, false);
    }

    public static void clearLoggedIn(Context ctx)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.remove(PREF_LOGGEDIN_USER_USERNAME);
        editor.remove(PREF_USER_LOGGEDIN_STATUS);
        editor.apply();
    }
}
