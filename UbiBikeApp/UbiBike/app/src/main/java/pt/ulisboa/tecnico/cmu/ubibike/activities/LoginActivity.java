package pt.ulisboa.tecnico.cmu.ubibike.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import pt.ulisboa.tecnico.cmu.ubibike.R;
import pt.ulisboa.tecnico.cmu.ubibike.data.LoginData;
import pt.ulisboa.tecnico.cmu.ubibike.data.UserData;
import pt.ulisboa.tecnico.cmu.ubibike.data.files.UtilFile;
import pt.ulisboa.tecnico.cmu.ubibike.domain.User;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UserServiceREST;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UtilREST;
import pt.ulisboa.tecnico.cmu.ubibike.services.UserUpdateService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //APP Entry Point (Activity)!!!!
        startService(new Intent(getBaseContext(), UserUpdateService.class));
        if(LoginData.getUserLoggedInStatus(this)){
            Intent intent = new Intent(getBaseContext(),HomeActivity.class);
            User user = (User) UtilFile.readFromFile(this,UtilFile.USER_FILE);
            intent.putExtra("User",user);
            finish();
            startActivity(intent);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void createAccount(View view) {
        Intent intent = new Intent(getBaseContext(), CreateAccountActivity.class);
        startActivity(intent);
    }

    public void submitLogin(View view) {
        boolean valid = true;
        TextView usernameTextView = (EditText)findViewById(R.id.login_username);
        TextView passwordTextView = (EditText)findViewById(R.id.login_password);
        String username = usernameTextView.getText().toString();
        String password = passwordTextView.getText().toString();

        if(username.isEmpty()){
            usernameTextView.setError(getString(R.string.field_required));
            valid = false;
        }
        else if(password.isEmpty()){
            passwordTextView.setError(getString(R.string.field_required));
            valid = false;
        }
        if(!valid)
            return;

        UserServiceREST userService = UtilREST.getRetrofit().create(UserServiceREST.class);
        Call<User> call = userService.login(username, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code() == UtilREST.HTTP_OK){
                    Intent intent = new Intent(getBaseContext(),HomeActivity.class);
                    User user = response.body();
                    user.userFromServer();
                    UserData.setUserData(getBaseContext(), user);
                    UserData.saveUserData(getBaseContext());
                    LoginData.setLoggedIn(getBaseContext(),user.getUsername());
                    intent.putExtra("User", user);
                    finish();
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getBaseContext(),R.string.login_failed,Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getBaseContext(),R.string.impossible_connect_server,Toast.LENGTH_LONG).show();
            }
        });
    }

}
