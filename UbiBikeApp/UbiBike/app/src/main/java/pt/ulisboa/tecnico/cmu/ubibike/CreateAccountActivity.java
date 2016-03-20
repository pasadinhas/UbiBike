package pt.ulisboa.tecnico.cmu.ubibike;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by cedac on 20/03/16.
 */
public class CreateAccountActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText("Create Account");

        // Set the text view as the activity layout
        setContentView(textView);
    }


}
