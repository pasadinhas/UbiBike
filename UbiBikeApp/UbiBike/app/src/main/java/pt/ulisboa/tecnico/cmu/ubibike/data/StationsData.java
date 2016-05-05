package pt.ulisboa.tecnico.cmu.ubibike.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import pt.ulisboa.tecnico.cmu.ubibike.domain.Station;
import pt.ulisboa.tecnico.cmu.ubibike.domain.User;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UtilREST;

/**
 * Created by André on 05-05-2016.
 */
public abstract class StationsData extends PreferencesData{

    private static final String PREF_STATIONS_JSON_OBJECT = "stations_json_object";

    private static final String PREF_STATIONS_SAVED = "stations_saved";


    public static boolean getStationsSaved(Context ctx){
        return getSharedPreferences(ctx).getBoolean(PREF_STATIONS_SAVED,false);
    }

    public static void setStations(Context ctx,List<Station> jsonStations)
    {
        Type listType = new TypeToken<List<Station>>(){}.getType();
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_STATIONS_JSON_OBJECT,UtilREST.GSON.toJson(jsonStations,listType));
        editor.putBoolean(PREF_STATIONS_SAVED,true);
        editor.apply();
    }

    public static List<Station> getStations(Context ctx)
    {
        Type listType = new TypeToken<List<Station>>(){}.getType();
        String jsonStations;
        jsonStations = getSharedPreferences(ctx).getString(PREF_STATIONS_JSON_OBJECT,null);
        if(jsonStations != null) {
            return UtilREST.GSON.fromJson(jsonStations, listType);
        }
        return null;
    }

}
