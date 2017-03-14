package com.example.ross.opendrive;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Ross on 21-Feb-17.
 * used to get user tokens
 */
public class  MyFirebaseMessagingService extends FirebaseMessagingService{
    private static final String TAG = "MyFirebaseMsgService";
    public static boolean notis = true;
    //static String id;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(notis) {
            sendNotification(remoteMessage.getData().get("message"));
        }
        else
            Log.d(TAG, "Notifications toggled off");
    }

    
    //display notification
    private void sendNotification(String body) {
        Intent intent = new Intent(this, Main2Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0/*request code*/, intent, PendingIntent.FLAG_ONE_SHOT);

        //set sound of notification
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); //could change to alarm?

        NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Motion Detected!")
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notifiBuilder.build());
    }
}


