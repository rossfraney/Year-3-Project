package com.example.ross.opendrive;

import android.app.Service;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * Created by Ross on 21-Feb-17.
 * used to receive the notification to the app
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIDService";

    //Called when the app deletes token or server side deleted
    @Override
    public void onTokenRefresh() {
        //get updated token
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "New Token: " + token);
        registerToken(token);
    }

    //Sending POST to php script to add user to SQL DB
    private void registerToken(String token) {
        Log.d(TAG, "register is calling");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token", token)
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.100.243/fcm/myregister.php") // WAMP LOCALHOST Server
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
