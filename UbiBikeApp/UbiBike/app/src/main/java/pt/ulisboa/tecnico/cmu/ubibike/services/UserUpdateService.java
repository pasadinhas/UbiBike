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

import pt.ulisboa.tecnico.cmu.ubibike.data.UserData;
import pt.ulisboa.tecnico.cmu.ubibike.domain.User;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UserServiceREST;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UtilREST;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Service updates user information in the cloud when user has internet connection.
 */
public class UserUpdateService extends Service {

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(isConnected(context)){
                User user = UserData.getUserData(context);
                if(user != null && user.getIsDirty()){
                    Log.d("UPDATE","Updating user information in server.");
                    updateUserRemotely(user.getUsername(),user.getPoints());
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(receiver,intentFilter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void updateUserRemotely(String username,long points){
        UserServiceREST userService = UtilREST.getRetrofit().create(UserServiceREST.class);
        Call<User> call = userService.updateUserPoints(username,points);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code() == UtilREST.HTTP_OK){
                    Log.d("UPDATE","User information in server update with success");
                    User user = UserData.getUserData(getBaseContext());
                    user.setIsDirty(false);
                    UserData.saveUserData(getBaseContext());
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                //Maintain dirty user
            }
        });
    }

    private boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}
