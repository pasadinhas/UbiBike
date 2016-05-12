package pt.ulisboa.tecnico.cmu.ubibike.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmu.ubibike.R;
import pt.ulisboa.tecnico.cmu.ubibike.activities.custom.UserArrayAdapter;
import pt.ulisboa.tecnico.cmu.ubibike.domain.User;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UserServiceREST;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UtilREST;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchUserActivity extends BaseDrawerActivity {

    private Activity currentActivity;

    protected ListView usernamesListView;
    protected EditText usernameEditText;

    @Override
    protected int getPosition() {
        return 2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        currentActivity = this;
        usernamesListView = (ListView)findViewById(R.id.usernames_listView);
        usernameEditText = (EditText)findViewById(R.id.username_editText);

        usernamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)   //NOT header
                    return;
                User username = (User) parent.getItemAtPosition(position);
                UserServiceREST userService = UtilREST.getRetrofit().create(UserServiceREST.class);
                Call<User> call = userService.getUserWithDetailSpecified(username.getUsername(),
                        UserServiceREST.USER_DETAIL_MEDIUM);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() == UtilREST.HTTP_OK) {
                            Intent intent = new Intent(getBaseContext(), UserPresentationActivity.class);
                            intent.putExtra("User", response.body());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(getBaseContext(), R.string.impossible_connect_server_toast,
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void search(View view){
        String usernamePrefix = usernameEditText.getText().toString();

        UserServiceREST userService = UtilREST.getRetrofit().create(UserServiceREST.class);
        Call<List<User>> call = userService.getAllUsers(usernamePrefix);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.code() == UtilREST.HTTP_OK){
                    List<User> userList = response.body();
                    userList = ((userList == null) ? new ArrayList<User>() : userList);
                    if(!userList.isEmpty()){
                        if(usernamesListView.getHeaderViewsCount() == 0) {
                            usernamesListView.addHeaderView(getLayoutInflater().inflate(R.layout.custom_header_user, null,false));
                        }
                        usernamesListView.setAdapter(new UserArrayAdapter(currentActivity,
                                R.layout.custom_row_users,userList));
                    }
                    Toast.makeText(getBaseContext(),userList.size()+" "+getString(R.string.users_found_toast),
                            Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getBaseContext(),R.string.impossible_connect_server_toast,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

}
