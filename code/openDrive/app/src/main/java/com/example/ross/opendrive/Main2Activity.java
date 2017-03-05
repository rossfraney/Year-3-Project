package com.example.ross.opendrive;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityBuilder;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import android.widget.ToggleButton;


import java.io.InputStream;

public class Main2Activity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private GoogleApiClient mGoogleApiClient;
    private Button openButton;
    private Button emergencyCall;
    private Button textNeighbour;
    private Button startCamera;
    private Button stopCamera;
    //private WebView webView;
    private static final String TAG = "BaseDriveActivity";
    protected static final int REQUEST_CODE_RESOLUTION = 1;
    private static final int REQUEST_CODE_DELETER = 2;
    private static final int REQUEST_CODE_OPENER = 3;
    private static String myHost = "192.168.43.50";


    public String neighboursNum;
    /**
     * File that is selected with the open file activity.
     */
    private DriveId mSelectedFileDriveId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        openButton = (Button) findViewById(R.id.openButton);
        openButton.setOnClickListener(this);

        emergencyCall = (Button) findViewById(R.id.emergencyCall);
        emergencyCall.setOnClickListener(this);

        textNeighbour = (Button) findViewById(R.id.textNeighbour);
        textNeighbour.setOnClickListener(this);

        startCamera = (Button) findViewById(R.id.startCamera);
        startCamera.setOnClickListener(this);

        stopCamera = (Button) findViewById(R.id.stopCamera);
        stopCamera.setOnClickListener(this);
        ToggleButton toggleButton = (ToggleButton) findViewById((R.id.toggleButton));
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    showMessage("Notifications Enabled");
                    MyFirebaseMessagingService.notis = true;
                } else{
                    showMessage("Notifications Disabled");
                    MyFirebaseMessagingService.notis = false;
                }
            }
        });

        /*webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString("Android WebView");
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setDomStorageEnabled(true);

        webView.setVisibility(View.INVISIBLE);*/

        //FirebaseMessaging.getInstance().subscribeToTopic("test");
        FirebaseInstanceId.getInstance().getToken();

    }

    /**
     * Called when activity gets visible. A connection to Drive services need to
     * be initiated as soon as the activity is visible. Registers
     * {@code ConnectionCallbacks} and {@code OnConnectionFailedListener} on the
     * activities itself.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RESOLUTION && resultCode == RESULT_OK) {
            mGoogleApiClient.connect();
        }
        if (requestCode == REQUEST_CODE_OPENER && resultCode == RESULT_OK) {
            mSelectedFileDriveId = data.getParcelableExtra(
                    OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
            open();
        }
        if (requestCode == REQUEST_CODE_DELETER && resultCode == RESULT_OK) {
            mSelectedFileDriveId = data.getParcelableExtra(
                    OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
            delete();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    //.addScope(Drive.SCOPE_APPFOLDER) // required for App Folder sample
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "GoogleApiClient connected");
        TextView tv1 = (TextView) findViewById(R.id.textView1);
        tv1.setText("Welcome Back!");
        showMessage("Registered for notifications");

    }


    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "GoogleApiClient connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
            return;
        }
        try {
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }


    @Override
    public void onClick(View view) {
            if(view == openButton) {
                /*Toast.makeText(this, "Opening Drive...", Toast.LENGTH_SHORT).show();
                IntentSender intentSender = Drive.DriveApi
                        .newOpenFileActivityBuilder()
                        .setMimeType(new String[]{"image/jpeg"})
                        .build(getGoogleApiClient());
                try {
                    startIntentSenderForResult(intentSender, REQUEST_CODE_OPENER, null, 0, 0, 0);
                } catch (IntentSender.SendIntentException e) {
                    Log.w(TAG, "Unable to send intent", e);
                }*/
                openImageOptions();
            }

            if(view == textNeighbour) {
                neighboursNum = setNeighbour.getNum();
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(Uri.parse("smsto:" + neighboursNum));
                sendIntent.putExtra("sms_body", "Security Alert: Hi, could you please check my home as there has been action recorded on my security camera. Thanks!");
                try {
                    startActivity(sendIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    ex.printStackTrace();
                }
            }

            if(view == emergencyCall) {

                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    showMessage("You do not have permission");
                    return;
                }
                emergencyServicesCheck();
            }

            if(view == startCamera) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSch jsch = new JSch();
                            Session session = jsch.getSession("pi", myHost, 22);
                            session.setPassword("raspberry");

                            // Avoid asking for key confirmation
                            Properties prop = new Properties();
                            prop.put("StrictHostKeyChecking", "no");
                            session.setConfig(prop);

                            Log.d(TAG, "SSH Connecting");
                            session.connect();
                            Log.d(TAG, "SSH connected");


                            Channel channelssh = session.openChannel("exec");
                            ((ChannelExec) channelssh).setCommand("python /home/pi/securiPi/pi_surveillance.py --conf /home/pi/securiPi/conf.json ");
                           // ((ChannelExec) channelssh).setCommand("pkill -f python");
                            channelssh.setInputStream(null);
                            ((ChannelExec) channelssh).setErrStream(System.err);
                            InputStream in = channelssh.getInputStream();

                            channelssh.connect();
                            byte[] tmp = new byte[1024];
                            while (true)
                            {
                                while (in.available() > 0)
                                {
                                    int i = in.read(tmp, 0, 1024);
                                    if (i < 0)
                                        break;
                                    System.out.print(new String(tmp, 0, i));
                                }
                                if (channelssh.isClosed())
                                {
                                    System.out.println("exit-status: " + channelssh.getExitStatus());
                                    break;
                                }
                                try
                                {
                                    Thread.sleep(1000);
                                }
                                catch (Exception ee)
                                {
                                }
                            }

                            channelssh.disconnect();
                            session.disconnect();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                t.start();
                //findViewById(R.id.webView).setVisibility(View.VISIBLE);
                //open connection to website hosting stream
            }
        if(view == stopCamera){
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSch jsch = new JSch();
                        Session session = jsch.getSession("pi", myHost, 22);
                        session.setPassword("raspberry");

                        // Avoid asking for key confirmation
                        Properties prop = new Properties();
                        prop.put("StrictHostKeyChecking", "no");
                        session.setConfig(prop);

                        Log.d(TAG, "SSH Connecting");
                        session.connect();
                        Log.d(TAG, "SSH connected");


                        Channel channelssh = session.openChannel("exec");
                        ((ChannelExec) channelssh).setCommand("pkill -f python");
                        channelssh.setInputStream(null);
                        ((ChannelExec) channelssh).setErrStream(System.err);
                        InputStream in = channelssh.getInputStream();

                        channelssh.connect();
                        byte[] tmp = new byte[1024];
                        while (true)
                        {
                            while (in.available() > 0)
                            {
                                int i = in.read(tmp, 0, 1024);
                                if (i < 0)
                                    break;
                                System.out.print(new String(tmp, 0, i));
                            }
                            if (channelssh.isClosed())
                            {
                                System.out.println("exit-status: " + channelssh.getExitStatus());
                                break;
                            }
                            try
                            {
                                Thread.sleep(1000);
                            }
                            catch (Exception ee)
                            {
                            }
                        }

                        channelssh.disconnect();
                        session.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        }
    }

    public void emergencyServicesCheck() {
        String number = "0861921718";
        Uri call = Uri.parse("tel:" + number);
        final Intent surf = new Intent(Intent.ACTION_CALL, call);
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Emergency Dial")
                .setMessage("Are you sure you want to call Emergency Services?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(surf);
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    public void openImageOptions() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("View Images")
                .setMessage("Open Drive in-app or open Drive.google.com?")
                .setPositiveButton("Open Drive", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showMessage("Opening Drive..");
                        IntentSender intentSender = Drive.DriveApi
                                .newOpenFileActivityBuilder()
                                .setMimeType(new String[]{"image/jpeg"})
                                .build(getGoogleApiClient());
                        try {
                            startIntentSenderForResult(intentSender, REQUEST_CODE_OPENER, null, 0, 0, 0);
                        } catch (IntentSender.SendIntentException e) {
                            Log.w(TAG, "Unable to send intent", e);
                        }
                    }

                })
                .setNegativeButton("Open in browser", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/drive"));
                        startActivity(browserIntent);
                        //findViewById(R.id.imageViewpic).setVisibility(View.INVISIBLE);
                        //webView.setVisibility(View.VISIBLE);
                        //webView.loadUrl("https://www.google.com/drive/");
                    }
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.deletefiles, menu); //your file name
        inflater.inflate(R.menu.signout, menu);
        inflater.inflate(R.menu.setneighbour, menu);
        inflater.inflate(R.menu.alarmsound, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.signOut:
                finish();
                signOut();
                startActivity(new Intent(this, LoggedOut.class));
                return true;

            case R.id.deleteFiles:
                showMessage("Opening Drive..");
                IntentSender intentSender = Drive.DriveApi
                        .newOpenFileActivityBuilder()
                        .setMimeType(new String[]{"image/jpeg"})
                        .build(getGoogleApiClient());
                try {
                    startIntentSenderForResult(intentSender, REQUEST_CODE_DELETER, null, 0, 0, 0);
                } catch (IntentSender.SendIntentException e) {
                    Log.w(TAG, "Unable to send intent", e);
                }
                return true;

            case R.id.setNeighbour:
                startActivity(new Intent(this, setNeighbour.class));
                return true;

            case R.id.alarmSound:
                startActivity(new Intent(this, AlarmSound.class));

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void open() {
        DriveFile driveFile = mSelectedFileDriveId.asDriveFile();
        driveFile.open(getGoogleApiClient(), DriveFile.MODE_READ_ONLY, null)
                .setResultCallback(driveContentsCallback);
        mSelectedFileDriveId = null;
    }

    private ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback =
            new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        showMessage("Error while opening the file contents");
                        return;
                    }
                    DriveContents contents = result.getDriveContents();
                    InputStream is = contents.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    ImageView imageView = (ImageView) findViewById(R.id.imageViewpic);
                    imageView.setImageBitmap(bitmap);
                    showMessage("Working");
                }
            };

    private void delete(){
        DriveFile driveFile = mSelectedFileDriveId.asDriveFile();
        driveFile.open(getGoogleApiClient(), DriveFile.MODE_READ_ONLY, null)
                .setResultCallback(driveContentsCallback1);
        mSelectedFileDriveId = null;
    }

    private ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback1 =
            new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        showMessage("Error while opening the file contents");
                        return;
                    }
                    DriveContents contents = result.getDriveContents();
                    contents.getDriveId().asDriveFile().delete(mGoogleApiClient);
                    ImageView imageView = (ImageView) findViewById(R.id.imageViewpic);
                    imageView.setImageBitmap(null);
                    showMessage("Delete Working");
                }
            };

    private void signOut() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.clearDefaultAccountAndReconnect().setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    mGoogleApiClient.disconnect();
                }
            });
        }
    }

    public static String getHost(){
        return myHost;
    }

}