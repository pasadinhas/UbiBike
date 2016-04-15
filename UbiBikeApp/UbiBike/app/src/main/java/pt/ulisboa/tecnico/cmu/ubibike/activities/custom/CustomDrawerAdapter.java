package pt.ulisboa.tecnico.cmu.ubibike.activities.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.zip.Inflater;

import pt.ulisboa.tecnico.cmu.ubibike.R;

/**
 * Created by André on 14-04-2016.
 */
public class CustomDrawerAdapter<T> extends ArrayAdapter<T> {

    private Context context;

    private T[] objects;

    public CustomDrawerAdapter(Context context, int resource, T[] objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position,convertView,parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.custom_row, parent, false);
        ((TextView)row.findViewById(R.id.information)).setText(objects[position].toString());
        ImageView icon =(ImageView)row.findViewById(R.id.icon);
        if(position == 0){
            icon.setImageResource(R.mipmap.finish);
        } else if(position == 1){
            icon.setImageResource(R.mipmap.finish);
        } else if(position == 2){
            icon.setImageResource(R.mipmap.finish);
        } else if(position == 3){
            icon.setImageResource(R.mipmap.finish);
        } else if(position == 4){
            icon.setImageResource(R.mipmap.finish);
        } else if(position == 5){
            icon.setImageResource(R.mipmap.finish);
        }
        return row;
    }
}
