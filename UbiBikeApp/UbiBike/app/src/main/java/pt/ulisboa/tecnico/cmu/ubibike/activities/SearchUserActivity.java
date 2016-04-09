package pt.ulisboa.tecnico.cmu.ubibike.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmu.ubibike.R;
import pt.ulisboa.tecnico.cmu.ubibike.domain.User;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UserServiceREST;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UtilREST;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchUserActivity extends BaseDrawerActivity {

    private Activity currentActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        ListView list = (ListView)findViewById(R.id.usernames_listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String username = (String) parent.getItemAtPosition(position);

                UserServiceREST userService = UtilREST.getRetrofit().create(UserServiceREST.class);
                Call<User> call = userService.getUser(username);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() == UtilREST.HTTP_OK) {
                            Intent intent = new Intent(currentActivity, UserPresentationActivity.class);
                            intent.putExtra("User", response.body());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(getBaseContext(), R.string.impossible_connect_server,
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void search(View view){
        TextView usernameTextView = (EditText)findViewById(R.id.username_editText);
        String username = usernameTextView.getText().toString();

        UserServiceREST userService = UtilREST.getRetrofit().create(UserServiceREST.class);
        Call<List<String>> call = userService.getUsernames(username);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.code() == UtilREST.HTTP_OK){
                    List<String> usernamesList = response.body();
                    usernamesList = ((usernamesList == null) ? new ArrayList<String>() : usernamesList);
                    if(!usernamesList.isEmpty()){
                        ListView listView = (ListView)findViewById(R.id.usernames_listView);
                        listView.setAdapter(new ArrayAdapter<>(currentActivity,
                                R.layout.support_simple_spinner_dropdown_item,usernamesList));
                    }
                    Toast.makeText(getBaseContext(),usernamesList.size()+" "+getString(R.string.users_found),
                            Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(getBaseContext(),R.string.impossible_connect_server,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

}
