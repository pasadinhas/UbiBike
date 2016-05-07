package pt.ulisboa.tecnico.cmu.ubibike.activities;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.ulisboa.tecnico.cmu.ubibike.R;
import pt.ulisboa.tecnico.cmu.ubibike.data.DatabaseManager;
import pt.ulisboa.tecnico.cmu.ubibike.data.UserLoginData;
import pt.ulisboa.tecnico.cmu.ubibike.services.WifiDirectService;

public class ChatActivity extends BaseDrawerActivity {

    public static final String TAG = "ubibike";
    private SimWifiP2pSocket mCliSocket = null;
    private String otherIP = null;
    private String otherUsername = null;
    private String username = null;

    private long lastUpdated = 0;

    @Override
    protected int getPosition() {
        return -1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        otherIP = (String) getIntent().getSerializableExtra("OtherIP");
        otherUsername = (String) getIntent().getSerializableExtra("OtherUsername");
        username = UserLoginData.getUser(getBaseContext()).getUsername();
        Log.d(TAG, "onCreate: otherIP:" + otherIP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        WifiDirectService.getInstance().setChatListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        WifiDirectService.getInstance().setChatListener(null);
    }

    public void sendMessage(View view) {
        String input = ((TextView) findViewById(R.id.chatTextInput)).getText().toString();
        String content = "[Protocol]MESSAGE " + username + " " + input;
        new SendMessageTask().executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR,
                content, otherIP);
        DatabaseManager.getInstance(this).insertMessage(username, input);
        updateChat();
    }

    public void updateChat() {
        Cursor res =  DatabaseManager.getInstance(this).getMessagesSinceTime(username, otherUsername, lastUpdated);
        lastUpdated = System.currentTimeMillis();
        Log.d(TAG, "updateChat: " + res.getCount());
        if(res.moveToFirst()) {
            do {
                String sender = res.getString(res.getColumnIndex(DatabaseManager.USERNAME));
                String content = res.getString(res.getColumnIndex(DatabaseManager.CONTENT));
                final String line = "\n\n" + (sender.equals(username) ? "Me" : sender) + ": " + content;
                final TextView output = (TextView) findViewById(R.id.chatTextShow);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        output.append(line);
                    }
                });

            } while (res.moveToNext());
        }
    }

    public class SendMessageTask extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... msg) {
            Log.d(TAG, "SendMessageTask: sending message: <heheh>");
            try {
                mCliSocket = new SimWifiP2pSocket(msg[1],
                        Integer.parseInt(getString(R.string.port)));

                mCliSocket.getOutputStream().write((msg[0] + "\n").getBytes());
                BufferedReader sockIn = new BufferedReader(
                        new InputStreamReader(mCliSocket.getInputStream()));
                String response = sockIn.readLine();
                Log.d(TAG, "exchangeIdTask: received response: " + response);
                publishProgress(response, msg[1]);

                mCliSocket.close();


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mCliSocket = null;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }

        @Override
        protected void onPostExecute(Void result) {
        }

    }
}
