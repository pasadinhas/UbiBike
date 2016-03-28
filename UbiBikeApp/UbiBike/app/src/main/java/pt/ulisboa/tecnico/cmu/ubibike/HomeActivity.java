package pt.ulisboa.tecnico.cmu.ubibike;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import pt.ulisboa.tecnico.cmu.ubibike.domain.User;
import pt.ulisboa.tecnico.cmu.ubibike.listners.DrawerItemClickListner;

public class HomeActivity extends Activity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        user = (User)getIntent().getSerializableExtra("User");
        ListView listView = (ListView) findViewById(R.id.left_drawer);
        String[] drawerItems = getResources().getStringArray(R.array.drawer_items);
        //Populate UI components
        listView.setAdapter(new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,drawerItems));
        listView.setOnItemClickListener(new DrawerItemClickListner(this));
        ((TextView)findViewById(R.id.username_textView)).setText(user.getUsername());
        ((TextView)findViewById(R.id.points_textView)).setText("Points: " + user.getPoints());
    }

}
