package pt.ulisboa.tecnico.cmu.ubibike.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import pt.ulisboa.tecnico.cmu.ubibike.domain.User;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UtilREST;

/**
 * Manipulates (persistent) preferences to control user local Login (Data).
 */
public abstract class UserLoginData extends PreferencesData{

    private static final String PREF_LOGGEDIN_USER_USERNAME = "logged_in_username";
    private static final String PREF_USER_LOGGEDIN_STATUS = "logged_in_status";
    private static final String PREF_USER_JSON_OBJECT = "user_json_object";


    public static void setUserLoggedIn(Context ctx, String username,User jsonUser)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LOGGEDIN_USER_USERNAME, username);
        editor.putBoolean(PREF_USER_LOGGEDIN_STATUS, true);
        editor.putString(PREF_USER_JSON_OBJECT, UtilREST.GSON.toJson(jsonUser,User.class));
        editor.apply();
    }

    public static void setUser(Context ctx,User jsonUser){
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_JSON_OBJECT,UtilREST.GSON.toJson(jsonUser, User.class));
        editor.apply();
    }

    public static boolean getUserLoggedInStatus(Context ctx)
    {
        return getSharedPreferences(ctx).getBoolean(PREF_USER_LOGGEDIN_STATUS, false);
    }

    public static User getUser(Context ctx){
        String jsonUser;
        jsonUser = getSharedPreferences(ctx).getString(PREF_USER_JSON_OBJECT,null);
        if(jsonUser != null) {
            return UtilREST.GSON.fromJson(jsonUser, User.class);
        }
        return null;
    }

    public static void clearUserLoggedIn(Context ctx)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.remove(PREF_LOGGEDIN_USER_USERNAME);
        editor.remove(PREF_USER_LOGGEDIN_STATUS);
        editor.remove(PREF_USER_JSON_OBJECT);
        editor.apply();
    }
}
