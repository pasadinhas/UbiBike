package pt.ulisboa.tecnico.cmu.ubibike;

import android.app.Application;

import pt.ulisboa.tecnico.cmu.ubibike.data.DatabaseManager;
import pt.ulisboa.tecnico.cmu.ubibike.data.WifiDirectData;

/**
 * Created by cedac on 07/05/16.
 */
public class UbiApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        getBaseContext().deleteDatabase(DatabaseManager.DB_NAME);
        DatabaseManager.getInstance(getBaseContext()).close();
        getBaseContext().deleteDatabase(DatabaseManager.DB_NAME);
        DatabaseManager.getInstance(getBaseContext());

        WifiDirectData.setIsEnabled(getBaseContext(), false);
    }
}
