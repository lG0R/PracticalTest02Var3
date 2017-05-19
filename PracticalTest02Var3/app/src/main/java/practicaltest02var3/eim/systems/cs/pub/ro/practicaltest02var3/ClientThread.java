package practicaltest02var3.eim.systems.cs.pub.ro.practicaltest02var3;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;
import android.widget.EditText;
/**
 * Created by igor on 19.05.2017.
 */

public class ClientThread extends Thread {

    String w, addr;
    int port;
    EditText edit;
    private Socket socket;

    public ClientThread(String word, String clientAddr, int clientPort,
                        EditText cResultText) {
        this.w = word;
        this.addr = clientAddr;
        this.port = clientPort;
        this.edit = cResultText;
    }

    @Override
    public void run()
    {
        try {
            socket = new Socket(addr, port);


            if (socket == null) {
                Log.e(Constants.TAG, "[Client Thread] couldn't create client socket");
                return;
            }


            BufferedReader clientReader = Utilities.getReader(socket);
            PrintWriter clientWriter = Utilities.getWriter(socket);

            if (clientReader == null || clientWriter == null)
                Log.e(Constants.TAG, "[Client thread] null buffers");

            clientWriter.println(w);
            clientWriter.flush();
            String result2;
            while ((result2 = clientReader.readLine()) != null) {
                final String finalizedWeateherInformation = result2;
                edit.post(new Runnable() {

                    @Override
                    public void run() {
                        edit.append(finalizedWeateherInformation + "\n");
                        //infoTextView.append(finalizedInfo + "\n");
                    }
                });
            }

//            //final String result = clientReader.readLine();
//            Log.i(Constants.TAG, "[client thread] " + result);
//            edit.post(new Runnable() {
//
//                @Override
//                public void run() {
//                    edit.setText(result);
//                }
//            });


        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(Constants.TAG,  "[Client Thread] couldn't create buffers");
        }
    }
}
