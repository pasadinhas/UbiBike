package pt.ulisboa.tecnico.cmu.ubibike.activities;
 
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.ResponseBody;
import pt.ulisboa.tecnico.cmu.ubibike.R;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UserServiceREST;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UtilREST;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccountActivity extends Activity {

    protected EditText usernameEditText;
    protected EditText passwordEditText;
    protected EditText verifyPasswordEditText;
    protected CheckBox termsCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        usernameEditText = (EditText)findViewById(R.id.username_editText);
        passwordEditText = (EditText)findViewById(R.id.pwd_editText);
        verifyPasswordEditText = (EditText)findViewById(R.id.pwd_verify_editText);
        termsCheckBox = (CheckBox)findViewById(R.id.terms_checkBox);
    }

    public void submitCreateAccount(View view){
        boolean valid = true;
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String verifyPassword = verifyPasswordEditText.getText().toString();
        if(username.isEmpty()) {
            usernameEditText.setError(getString(R.string.field_required_hint));
            valid = false;
        }
        else if(password.isEmpty()){
            passwordEditText.setError(getString(R.string.field_required_hint));
            valid = false;
        }
        else if(verifyPassword.isEmpty()){
            verifyPasswordEditText.setError(getString(R.string.field_required_hint));
            valid = false;
        }
        else if(!password.equals(verifyPassword)){
            verifyPasswordEditText.setError(getString(R.string.password_doesnt_match_toast));
            valid = false;
        }
        else if(!((CheckBox)findViewById(R.id.terms_checkBox)).isChecked()){
            termsCheckBox.setError(getString(R.string.accept_terms_checkbox));
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
                    Toast.makeText(getBaseContext(),R.string.creation_success_toast,Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    Toast.makeText(getBaseContext(),R.string.username_already_exist_toast,Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(),R.string.impossible_connect_server_toast,Toast.LENGTH_LONG).show();
            }
        });
    }

}
