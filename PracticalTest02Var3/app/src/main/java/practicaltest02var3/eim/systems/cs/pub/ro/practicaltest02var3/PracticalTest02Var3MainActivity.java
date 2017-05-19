package practicaltest02var3.eim.systems.cs.pub.ro.practicaltest02var3;

import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PracticalTest02Var3MainActivity extends AppCompatActivity {


    // server widgets
    // server widgets
    private EditText sPortText      = null;
    private Button   sConnectButton = null;
    private ServerThread sThread;

    // client widgets
    private EditText cAddressText  = null;
    private EditText cPortText     = null;
    private EditText cWordText     = null;
    private EditText cResultText   = null;
    private Button   cSearchButton = null;
    private ClientThread cThread;

    private SConnectButtonListener sConnectButtonListener = new SConnectButtonListener();
    private class SConnectButtonListener implements Button.OnClickListener {

        @Override
        public void onClick(View v) {
            // start server
            String serverPort = sPortText.getText().toString();

            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            sThread = new ServerThread(Integer.parseInt(serverPort));
            if (sThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            sThread.start();


            Log.i(Constants.TAG, "starting server on " + serverPort);

        }
    };

    private CSearchButtonListener cSearchButtonListener = new CSearchButtonListener();
    public class CSearchButtonListener implements Button.OnClickListener {

        private ClientThread clientThread;

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            String clientAddr    = cAddressText.getText().toString();
            String clientPortStr = cPortText.getText().toString();

            if (clientAddr == null || clientPortStr == null || clientAddr.length() == 0 || clientPortStr.length() == 0) {
                Toast.makeText(getApplicationContext(), "Provide server data -- address and port", Toast.LENGTH_LONG).show();
                return;
            }

            int clientPort = Integer.parseInt(clientPortStr);

            if (sThread == null || !sThread.isAlive()) {
                Log.e(Constants.TAG, "[main activity] there is no server to connect to.");
                return;
            }

            String word = cWordText.getText().toString();

            if (word == null || word.length() == 0) {
                Toast.makeText(getApplicationContext(), "Provide client data -- word", Toast.LENGTH_LONG).show();
                return;
            }

            Log.i(Constants.TAG,  "[main activity] ready to start client thread");

            cResultText.setText("");
            clientThread = new ClientThread(word, clientAddr, clientPort, cResultText);
            clientThread.start();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_var3_main);


        // server bindings
        sPortText = (EditText)findViewById(R.id.sPort);
        sConnectButton = (Button)findViewById(R.id.sButton);

        // client bindings
        cAddressText = (EditText)findViewById(R.id.cAdress);
        cPortText = (EditText)findViewById(R.id.cPort);
        cWordText = (EditText)findViewById(R.id.cWord);
        cResultText = (EditText)findViewById(R.id.cResult);
        cSearchButton = (Button)findViewById(R.id.cSearch);

        // add listeners
        sConnectButton.setOnClickListener(sConnectButtonListener);
        cSearchButton.setOnClickListener(cSearchButtonListener);

        sPortText.setText("1237");
        cAddressText.setText("127.0.0.1");
        cPortText.setText("1237");


    }

    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (sThread != null) {
            sThread.stopThread();
        }
        super.onDestroy();
    }
}
