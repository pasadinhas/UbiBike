package pt.ulisboa.tecnico.cmu.ubibike.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import okhttp3.ResponseBody;
import pt.ulisboa.tecnico.cmu.ubibike.UbiApp;
import pt.ulisboa.tecnico.cmu.ubibike.data.UserLoginData;
import pt.ulisboa.tecnico.cmu.ubibike.domain.User;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UserServiceREST;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UtilREST;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Service updates user information in the cloud when user has internet connection.
 */
public class UserSynchronizeService extends Service {

    public static final String SYNCHRONIZE_USER_INTENT = "pt.ulisboa.tecnico.cmu.ubibike.services.SYNCUSER";

    private static boolean RUNNING = false;

    public static boolean isRunning() { return RUNNING; }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        Log.d("[USER SERVICE]","INTENT RECEIVED");
        if(isConnected(getBaseContext())){
            User user = UbiApp.getInstance().getUser();
            if(user != null && user.getIsDirty()){
                Log.d("[USER SERVICE]","GOING UPDATE USER");
                updateUserRemotely(user);
            }
        }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        RUNNING = true;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction(SYNCHRONIZE_USER_INTENT);
        registerReceiver(receiver, intentFilter);
        Log.d("[USER SERVICE]", "STARTED");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("[USER SERVICE]","STOPED AND DESTROYED");
        unregisterReceiver(receiver);
        RUNNING = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Try synchronize user (local replica) with remote server.
    private void updateUserRemotely(final User user){
        UserServiceREST userService = UtilREST.getRetrofit().create(UserServiceREST.class);
        Call<ResponseBody> call = userService.synchronizeUser(UtilREST.ACCEPT_HEADER,UtilREST.CONTENT_TYPE_HEADER,
                user.getUsername(),user.getPoints(),user.getLocalTrajectories());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == UtilREST.HTTP_OK){
                    user.archiveLocalModifications();
                    UserLoginData.setUser(getBaseContext(), user);
                }
                //If not ok DO Nothing, maintain dirty user, will try next time.
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //DO Nothing, maintain dirty user, will try next time.
            }
        });
    }

    //Test if device is connected to a network.
    private boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}
