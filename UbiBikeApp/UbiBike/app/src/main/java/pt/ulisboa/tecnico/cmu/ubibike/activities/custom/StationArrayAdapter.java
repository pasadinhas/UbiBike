package pt.ulisboa.tecnico.cmu.ubibike.activities.custom;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pt.ulisboa.tecnico.cmu.ubibike.R;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Station;

/**
 * Created by André on 12-05-2016.
 */
public class StationArrayAdapter extends ArrayAdapter<Station> {

    private List<Station> stations;

    private Context ctx;

    private int layoutId;

    public StationArrayAdapter(Context context,int layoutId,List<Station> stations) {
        super(context, layoutId,stations);
        this.ctx = context;
        this.stations = stations;
        this.layoutId = layoutId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        StationHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)ctx).getLayoutInflater();
            row = inflater.inflate(layoutId, parent, false);
            holder = new StationHolder();
            holder.stationName = (TextView)row.findViewById(R.id.stationNameTextView);
            holder.bikes = (TextView)row.findViewById(R.id.bikesTextView);
            row.setTag(holder);
        }
        else
        {
            holder = (StationHolder)row.getTag();
        }
        Station station = stations.get(position);
        holder.stationName.setText(station.getName());
        holder.bikes.setText(station.getBikes().size()+"");
        return row;
    }

    static class StationHolder
    {
        TextView stationName;
        TextView bikes;
    }

}
