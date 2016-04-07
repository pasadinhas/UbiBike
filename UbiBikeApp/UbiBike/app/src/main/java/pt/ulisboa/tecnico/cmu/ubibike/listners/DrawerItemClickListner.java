package pt.ulisboa.tecnico.cmu.ubibike.listners;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.nearby.Nearby;

import pt.ulisboa.tecnico.cmu.ubibike.LoginActivity;
import pt.ulisboa.tecnico.cmu.ubibike.NearbyUsersActivity;
import pt.ulisboa.tecnico.cmu.ubibike.SearchUserActivity;
import pt.ulisboa.tecnico.cmu.ubibike.HomeActivity;
import pt.ulisboa.tecnico.cmu.ubibike.StationsActivity;

/**
 * Created by André on 25-03-2016.
 */
public class DrawerItemClickListner implements ListView.OnItemClickListener {

    private Activity current;

    public DrawerItemClickListner(Activity current){
        this.current = current;
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        if(!current.getClass().equals(HomeActivity.class) && position == 0)             //Home
            current.startActivity(new Intent(current, HomeActivity.class));
        else if(!current.getClass().equals(NearbyUsersActivity.class) && position == 1)
            current.startActivity(new Intent(current,NearbyUsersActivity.class));
        else if(!current.getClass().equals(SearchUserActivity.class) && position == 2)
            current.startActivity((new Intent(current, SearchUserActivity.class)));
        else if(!current.getClass().equals(StationsActivity.class) && position == 3)     //Stations
            current.startActivity(new Intent(current, StationsActivity.class));
        else if(position == 4) {      //Logout
            Intent intent = new Intent(current, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            current.startActivity(intent);
        }
    }

}
