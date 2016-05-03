package pt.ulisboa.tecnico.cmu.ubibike.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.ulisboa.tecnico.cmu.ubibike.R;
import pt.ulisboa.tecnico.cmu.ubibike.data.UserLoginData;

public class ChatActivity extends BaseDrawerActivity {

    public static final String TAG = "ubibike";
    private SimWifiP2pSocket mCliSocket = null;
    private String otherIP = null;
    private String username = null;

    @Override
    protected int getPosition() {
        return -1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        otherIP = (String) getIntent().getSerializableExtra("OtherIP");
        username = UserLoginData.getUser(getBaseContext()).getUsername();
        Log.d(TAG, "onCreate: otherIP:" + otherIP);
    }

    public void sendMessage(View view) {
        TextView input = (TextView) findViewById(R.id.chatTextInput);
        String content = "[Protocol]MESSAGE " + username + " " + input.getText().toString();
        new SendMessageTask().executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR,
                content, otherIP);
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
