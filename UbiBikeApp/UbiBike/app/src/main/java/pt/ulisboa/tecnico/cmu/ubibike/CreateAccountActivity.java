package pt.ulisboa.tecnico.cmu.ubibike;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import pt.ulisboa.tecnico.cmu.ubibike.domain.User;
import pt.ulisboa.tecnico.cmu.ubibike.rest.UserServiceREST;
import pt.ulisboa.tecnico.cmu.ubibike.rest.UtilREST;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.HTTP;

public class CreateAccountActivity extends AppCompatActivity {

    private Activity currentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentActivity = this;
        setContentView(R.layout.activity_create_account);
    }

    public void submitCreateAccount(View view){
        boolean valid = true;
        String username = ((EditText)findViewById(R.id.username_editText)).getText().toString();
        String password = ((EditText)findViewById(R.id.pwd_editText)).getText().toString();
        String verifyPassword = ((EditText)findViewById(R.id.pwd_verify_editText)).getText().toString();
        if(username.isEmpty()) {
            ((EditText)findViewById(R.id.username_editText)).setError(getString(R.string.field_required));
            valid = false;
        }
        else if(password.isEmpty()){
            ((EditText)findViewById(R.id.pwd_editText)).setError(getString(R.string.field_required));
            valid = false;
        }
        else if(verifyPassword.isEmpty()){
            ((EditText)findViewById(R.id.pwd_verify_editText)).setError(getString(R.string.field_required));
            valid = false;
        }
        else if(valid && !password.equals(verifyPassword)){
            ((EditText)findViewById(R.id.pwd_verify_editText)).setError(getString(R.string.password_doesnt_match));
            valid = false;
        }
        else if(!((CheckBox)findViewById(R.id.terms_checkBox)).isChecked()){
            ((CheckBox)findViewById(R.id.terms_checkBox)).setError(getString(R.string.accept_terms));
            valid = false;
        }
        if(!valid){
            return;
        }

        UserServiceREST userService = UtilREST.getRetrofit().create(UserServiceREST.class);
        Call<User> call = userService.createUsername(username,password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code() == 200){
                    Toast.makeText(getBaseContext(),R.string.creation_success,Toast.LENGTH_LONG).show();
                    currentActivity.finishActivity(0);
                }
                else{
                    Toast.makeText(getBaseContext(),R.string.username_already_exist,Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getBaseContext(),R.string.impossible_connect_server,Toast.LENGTH_LONG).show();
            }
        });
    }

}
