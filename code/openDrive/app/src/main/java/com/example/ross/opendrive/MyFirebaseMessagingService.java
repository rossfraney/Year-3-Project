package com.example.ross.opendrive;

import android.app.Service;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Ross on 21-Feb-17.
 * used to get user tokens
 */
public class  MyFirebaseMessagingService extends FirebaseMessagingService{
    private static final String TAG = "MyFirebaseMsgService";
    public static boolean notis = true;
    static String id;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(notis) {
            //id = remoteMessage.getData().get("message");
            //Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); //could change to alarm?
            sendNotification(remoteMessage.getData().get("message"));
            /*try {
                JSch jsch = new JSch();
                Session session = jsch.getSession("pi", Main2Activity.getHost(), 22);
                session.setPassword("raspberry");

                // Avoid asking for key confirmation
                Properties prop = new Properties();
                prop.put("StrictHostKeyChecking", "no");
                session.setConfig(prop);

                Log.d(TAG, "SSH Connecting");
                session.connect();
                Log.d(TAG, "SSH connected");


                Channel channelssh = session.openChannel("exec");
                ((ChannelExec) channelssh).setCommand("omxplayer -o local /home/pi/securiPi/soundFiles/Dog.mp3");
                channelssh.setInputStream(null);
                ((ChannelExec) channelssh).setErrStream(System.err);
                //InputStream in = channelssh.getInputStream();

                channelssh.connect();

                //exec here

                channelssh.disconnect();
                session.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }*/
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


