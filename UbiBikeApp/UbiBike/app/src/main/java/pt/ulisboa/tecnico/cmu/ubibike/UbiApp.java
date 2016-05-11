package pt.ulisboa.tecnico.cmu.ubibike;

import android.app.Application;

import pt.ulisboa.tecnico.cmu.ubibike.data.DatabaseManager;
import pt.ulisboa.tecnico.cmu.ubibike.data.WifiDirectData;
import pt.ulisboa.tecnico.cmu.ubibike.domain.User;

/**
 * Created by cedac on 07/05/16.
 */
public class UbiApp extends Application {

    private static UbiApp app;

    private User user;

    public User getUser(){
        return this.user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public static UbiApp getInstance(){
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        getBaseContext().deleteDatabase(DatabaseManager.DB_NAME);
        DatabaseManager.getInstance(getBaseContext()).close();
        getBaseContext().deleteDatabase(DatabaseManager.DB_NAME);
        DatabaseManager.getInstance(getBaseContext());

        WifiDirectData.setIsEnabled(getBaseContext(), false);
    }
}
