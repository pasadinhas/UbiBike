package pt.ulisboa.tecnico.cmu.ubibike.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by André on 05-05-2016.
 */
public abstract class PreferencesData {

    protected static SharedPreferences getSharedPreferences(Context ctx)
    {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

}
