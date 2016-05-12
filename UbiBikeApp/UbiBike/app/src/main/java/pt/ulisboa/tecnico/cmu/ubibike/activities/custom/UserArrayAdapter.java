package pt.ulisboa.tecnico.cmu.ubibike.activities.custom;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pt.ulisboa.tecnico.cmu.ubibike.R;
import pt.ulisboa.tecnico.cmu.ubibike.domain.User;

/**
 * Created by André on 12-05-2016.
 */
public class UserArrayAdapter extends ArrayAdapter<User> {

    private List<User> users;

    private Context ctx;

    private int layoutId;

    public UserArrayAdapter(Context context,int layoutId,List<User> users) {
        super(context, layoutId,users);
        this.ctx = context;
        this.users = users;
        this.layoutId = layoutId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        UserHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)ctx).getLayoutInflater();
            row = inflater.inflate(layoutId, parent, false);
            holder = new UserHolder();
            holder.row_users_points_textView = (TextView)row.findViewById(R.id.row_users_points_textView);
            holder.row_user_name_textView = (TextView)row.findViewById(R.id.row_user_name_textView);
            row.setTag(holder);
        }
        else
        {
            holder = (UserHolder)row.getTag();
        }
        User user = users.get(position);
        holder.row_user_name_textView.setText(user.getUsername());
        holder.row_users_points_textView.setText(user.getPoints()+"");
        return row;
    }

    static class UserHolder
    {
        TextView row_user_name_textView;
        TextView row_users_points_textView;
    }
}
