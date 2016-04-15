package pt.ulisboa.tecnico.cmu.ubibike.activities;
 
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.ResponseBody;
import pt.ulisboa.tecnico.cmu.ubibike.R;
import pt.ulisboa.tecnico.cmu.ubibike.domain.User;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UserServiceREST;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UtilREST;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccountActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        else if(!password.equals(verifyPassword)){
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
        Call<ResponseBody> call = userService.createUsername(username,password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == UtilREST.HTTP_OK){
                    Toast.makeText(getBaseContext(),R.string.creation_success,Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    Toast.makeText(getBaseContext(),R.string.username_already_exist,Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(),R.string.impossible_connect_server,Toast.LENGTH_LONG).show();
            }
        });
    }

}
