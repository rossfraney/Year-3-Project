package com.example.ross.opendrive;

/**
 * Created by Ross on 24-Feb-17.
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;



public class sendPost extends Main2Activity {
    private static UUID u = UUID.randomUUID();
    private static String uuid = u.toString();
    private static String json;
    private static HttpURLConnection httpcon;
    private static final String TAG = "PostActivity";

    public static void POST() throws JSONException {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "https://www.googleapis.com/drive/v3/changes/watch";
                String data = null;
                String result = null;
                    //Connect
                try {
                    httpcon = (HttpURLConnection) ((new URL(url).openConnection()));
                    httpcon.setDoOutput(true);
                    httpcon.setRequestProperty("Content-Type", "application/json");
                    httpcon.setRequestProperty("Accept", "application/json");
                    httpcon.setRequestMethod("POST");
                    httpcon.connect();

                    //obj
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("id", uuid);
                    jsonObject.accumulate("type", "web_hook");
                    jsonObject.accumulate("address", "https://rossfraney3.000webhostapp.com/");


                    data = jsonObject.toString();

                    //Write
                    OutputStream os = httpcon.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(data);
                    Log.d(TAG, "Sent Post to Google :D");
                    writer.close();
                    os.close();

                    //Read
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(),"UTF-8"));

                    String line = null;
                    StringBuilder sb = new StringBuilder();

                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    br.close();
                    result = sb.toString();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }); t.start();
    }
}
