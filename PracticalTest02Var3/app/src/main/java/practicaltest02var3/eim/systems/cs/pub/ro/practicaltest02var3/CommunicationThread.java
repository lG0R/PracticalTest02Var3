package practicaltest02var3.eim.systems.cs.pub.ro.practicaltest02var3;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by igor on 19.05.2017.
 */

public class CommunicationThread extends Thread {
    private ServerThread serverThread;
    public ServerThread getServerThread() {
        return serverThread;
    }

    public void setServerThread(ServerThread serverThread) {
        this.serverThread = serverThread;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.setServerThread(serverThread);
        this.setSocket(socket);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run()
    {
        Log.e(Constants.TAG,  "[COMMUNICATION THREAD] communication thread started.");
        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

            if (bufferedReader != null && printWriter != null) {
                String word = bufferedReader.readLine();

                Log.i(Constants.TAG, "[COMMUNICATION THREAD] word: " +  word);

                if (word == null || word.length() == 0)
                {
                    Log.e(Constants.TAG, "[Communication thread] error getting info from the server.");
                    return;
                }

                Log.i(Constants.TAG, "[communication thread] getting info from web server.");
                HashMap<String, String> data = serverThread.getData();

                if (data.containsKey(word)) {
                    Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the cache...");
                    String resu = data.get(word);
                    printWriter.println(resu);
                   // weatherForecastInformation = data.get(city);
                }
                else {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(Constants.WEB_SERVICE_ADDRESS);
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair(Constants.QUERY_ATTRIBUTE, word));
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                    httpPost.setEntity(urlEncodedFormEntity);
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    String pageSourceCode = httpClient.execute(httpPost, responseHandler);

                    if (pageSourceCode == null) {
                        Log.e(Constants.TAG, "[communication thread] null page source code");
                        return;
                    }

                    Log.e(Constants.TAG, "da-----------------------" + pageSourceCode);

                    serverThread.setData(word, pageSourceCode);
                    printWriter.println(pageSourceCode);

                    //Log.e(Constants.TAG, "laaaaaaaaaaaaaa" + pageSourceCode);
                }

            } else {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            }

            socket.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
