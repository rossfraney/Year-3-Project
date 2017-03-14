package com.example.ross.opendrive;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.InputStream;
import java.util.Properties;

public class Main2Activity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private GoogleApiClient mGoogleApiClient;
    private Button openButton;
    private Button emergencyCall;
    private Button textNeighbour;
    private Button startCamera;
    private Button stopCamera;
    private static final String TAG = "BaseDriveActivity";
    protected static final int REQUEST_CODE_RESOLUTION = 1;
    private static final int REQUEST_CODE_DELETER = 2;
    private static final int REQUEST_CODE_OPENER = 3;
    public String MyPREFERENCES;
    public String lastKey = "placeholderid";
    public String token;
    public String myHost = "192.168.43.50";
    public TextView tv1;
    public static String neighboursNum = "0861921718";

    SharedPreferences sharedPreferences;

    /**
     * File that is selected with the open file activity.
     */
    private DriveId mSelectedFileDriveId;
    private int Flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //welcome message
        tv1 = (TextView) findViewById(R.id.textView1);
        tv1.setText("Welcome");

        //button opens the drive
        openButton = (Button) findViewById(R.id.openButton);
        openButton.setOnClickListener(this);


        //calls emergency services button
        emergencyCall = (Button) findViewById(R.id.emergencyCall);
        emergencyCall.setOnClickListener(this);

        //button brings user to text message pre written
        textNeighbour = (Button) findViewById(R.id.textNeighbour);
        textNeighbour.setOnClickListener(this);

        //camera armed on raspberry Pi (SSH)
        startCamera = (Button) findViewById(R.id.startCamera);
        startCamera.setOnClickListener(this);

        //stops camera
        stopCamera = (Button) findViewById(R.id.stopCamera);
        stopCamera.setOnClickListener(this);

        //User will not receive notifications if toggle button is off.
        ToggleButton toggleButton = (ToggleButton) findViewById((R.id.toggleButton));
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showMessage("Notifications Enabled");
                    MyFirebaseMessagingService.notis = true;
                } else {
                    showMessage("Notifications Disabled");
                    MyFirebaseMessagingService.notis = false;
                }
            }
        });


        //Generating Token for registering user with firebase for notifications
        token = FirebaseInstanceId.getInstance().getToken();
    }

     /*
     Called when activity gets visible. Registers
     code ConnectionCallbacks and OnConnectionFailedListener on the
     activity itself.
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


    //If activity opened and user is not logged in, build a connection
    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        mGoogleApiClient.connect();
        //Flag = 1;
    }

    //If App is closed but not shut down
    @Override
    protected void onPause() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onPause();
        Flag = 1;
    }

    //google is connected to google services
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "GoogleApiClient connected");
        tv1.setText("Welcome Back!");
        if(Flag == 0)
            showMessage("Registered for notifications");

        //Build an SSH Connection and update notification in Pi token in case it has changed
        //In separate thread as it involves network activity
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
                    ((ChannelExec) channelssh).setCommand("sed -i -e 's/" + lastKey + "/" +
                            token + "/g' /home/pi/securiPi/notify.py");
                    channelssh.setInputStream(null);
                    ((ChannelExec) channelssh).setErrStream(System.err);

                    channelssh.connect();
                    //exec here
                    channelssh.disconnect();
                    session.disconnect();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                lastKey = token;
            }
        });
        t.start();
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
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0)
                    .show();
            return;
        }
        try {
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }


    //Button Click Handlers
    @Override
    public void onClick(View view) {
        if (view == openButton) {
            openImageOptions(); //if user hits the open button
        }

        //Changes neighbour's number variable and pre-creates the text message
        if (view == textNeighbour) {
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
            sendIntent.setData(Uri.parse("smsto:" + sharedPreferences.getString("Num", null)));
            sendIntent.putExtra("sms_body", "Security Alert: Hi, could you please check " +
                    "my home as there has been action recorded on my security camera. Thanks!");
            try {
                startActivity(sendIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        //if 999 button pressed, check if they are sure
        if (view == emergencyCall) {
            emergencyServicesCheck();
        }

        //SSH Connection to arm the camera
        if (view == startCamera) {
            showMessage("Camera Armed");
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
                        ((ChannelExec) channelssh).setCommand("python /home/pi/securiPi/" +
                                "pi_surveillance.py --conf /home/pi/securiPi/conf.json ");
                        channelssh.setInputStream(null);
                        ((ChannelExec) channelssh).setErrStream(System.err);

                        channelssh.connect();
                        channelssh.disconnect();
                        session.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        }

        //connection to disarm the camera's motion detection
        if (view == stopCamera) {
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

                        channelssh.connect();
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

    //Make Sure user wants to call emergency services
    public void emergencyServicesCheck() {
        String number = "999";
        Uri call = Uri.parse("tel:" + number);
        final Intent surf = new Intent(Intent.ACTION_CALL, call);
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            showMessage("Please enable permissions in your phone's settings");
            return;
        }
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Emergency Dial")
                .setMessage("Are you sure you want to call Emergency Services?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(surf);
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    //Open images in browser or through the app itself
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
                            startIntentSenderForResult(intentSender, REQUEST_CODE_OPENER,
                                    null, 0, 0, 0);
                        } catch (IntentSender.SendIntentException e) {
                            Log.w(TAG, "Unable to send intent", e);
                        }
                    }

                })
                .setNegativeButton("Open in browser", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.drive.google.com"));
                        startActivity(browserIntent);
                    }
                })
                .show();
    }

    //option menu initiation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pisetup, menu);
        inflater.inflate(R.menu.deletefiles, menu); //your file name
        inflater.inflate(R.menu.setneighbour, menu);
        inflater.inflate(R.menu.alarmsound, menu);
        inflater.inflate(R.menu.signout, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.signOut: //sign out user after finishing current activity
                finish();
                signOut();
                startActivity(new Intent(this, LoggedOut.class));
                return true;

            case R.id.setIp:
                startActivity(new Intent(this, setSshId.class));
                return true;

            case R.id.deleteFiles: //Open drive where select works as a delete button
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

            case R.id.setNeighbour: //Change neighbour's number
                startActivity(new Intent(this, setNeighbour.class));
                return true;

            case R.id.alarmSound: //Choose the sound for the pi to play when cam triggered
                startActivity(new Intent(this, AlarmSound.class));

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //Opening the Drive in app
    private void open() {
        DriveFile driveFile = mSelectedFileDriveId.asDriveFile();
        driveFile.open(getGoogleApiClient(), DriveFile.MODE_READ_ONLY, null)
                .setResultCallback(driveContentsCallback);
        mSelectedFileDriveId = null;
    }

    //Call back to return a bitmap of the image selected and display it
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
                }
            };

    //Deleting a file in app
    private void delete(){
        DriveFile driveFile = mSelectedFileDriveId.asDriveFile();
        driveFile.open(getGoogleApiClient(), DriveFile.MODE_READ_ONLY, null)
                .setResultCallback(driveContentsCallback1);
        mSelectedFileDriveId = null;
    }

    //call back to clear the image view on the main page and delete
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
            mGoogleApiClient.clearDefaultAccountAndReconnect()
                    .setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    mGoogleApiClient.disconnect();
                }
            });
        }
    }

    //public static String getHost(){
      //  return myHost;
    //}

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

}