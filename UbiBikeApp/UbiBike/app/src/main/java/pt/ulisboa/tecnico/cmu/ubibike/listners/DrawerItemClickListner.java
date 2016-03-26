package pt.ulisboa.tecnico.cmu.ubibike.listners;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import pt.ulisboa.tecnico.cmu.ubibike.GetUserInformationActivity;
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
        if(!current.getClass().equals(HomeActivity.class) && position == 0)   //Home
            current.startActivity(new Intent(current, HomeActivity.class));
        else if(position == 1)      //Users
            current.startActivity((new Intent(current, GetUserInformationActivity.class)));
        else if(position == 2)      //Stations
            current.startActivity(new Intent(current, StationsActivity.class));
        else if(position == 3)      //Logout
            Log.d("TODO","TODO :)");
    }

}
