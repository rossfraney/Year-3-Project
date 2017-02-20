package com.example.ross.opendrive;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.google.android.gms.drive.events.ChangeEvent;
import com.google.android.gms.drive.events.ChangeListener;
import com.google.android.gms.drive.events.DriveEventService;
import com.example.ross.opendrive.MyDriveEventService;

import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class Main2Activity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private GoogleApiClient mGoogleApiClient;
    private Button openButton;
    private Button signOutButton;
    private static final String TAG = "BaseDriveActivity";
    public static final String EXISTING_FOLDER_ID = "0Bx3C-rSZ8nsRSFo0MFJlRERhSW8";
    public static final String EXISTING_FILE_ID = "0Bx3C-rSZ8nsRaUpzTVFrSDJRMTQ";
    protected static final String EXTRA_ACCOUNT_NAME = "account_name";
    protected static final int REQUEST_CODE_RESOLUTION = 1;
    protected static final int NEXT_AVAILABLE_REQUEST_CODE = 2;
    private static final int REQUEST_CODE_OPENER = 3;
    private DriveFolder driveFolder;
    final private Object mSubscriptionStatusLock = new Object();

    //private Bitmap mBitmapToSave;
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

        signOutButton = (Button) findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(this);
        findViewById(R.id.signOutButton).setVisibility(View.GONE);
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
        if (requestCode == REQUEST_CODE_OPENER && resultCode == RESULT_OK){
            showMessage("Working");
            mSelectedFileDriveId = data.getParcelableExtra(
                    OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
            open();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addScope(Drive.SCOPE_APPFOLDER) // required for App Folder sample
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
        findViewById(R.id.signOutButton).setVisibility(View.VISIBLE);
        toggle();
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
        if (view == openButton) {
            Drive.DriveApi.requestSync(getGoogleApiClient()).setResultCallback(syncCallback);
            Toast.makeText(this, "Working up til this  point", Toast.LENGTH_SHORT).show();

            // Let the user pick an mp4 or a jpeg file if there are
            //getID();
            // no files selected by the user.
            IntentSender intentSender = Drive.DriveApi
                    .newOpenFileActivityBuilder()
                    //.setMimeType(new String[]{ "image/jpeg" })
                    .build(getGoogleApiClient());

            showMessage("id: " + driveFolder);
            try {
                startIntentSenderForResult(intentSender, REQUEST_CODE_OPENER, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                Log.w(TAG, "Unable to send intent", e);
            }
        }

        if (view == signOutButton) {
            finish();
            signOut();
            startActivity(new Intent(this, LoggedOut.class) );
        }
    }

    final private ResultCallback<com.google.android.gms.common.api.Status> syncCallback = new ResultCallback<com.google.android.gms.common.api.Status>() {
        @Override
        public void onResult(Status status) {
            if (!status.getStatus().isSuccess()) {
                showMessage(status.toString());
            } else {
                showMessage("updated");
            }
        }
    };
    private void open(){
        DriveFile driveFile =  mSelectedFileDriveId.asDriveFile();
        driveFile.open(getGoogleApiClient(), DriveFile.MODE_READ_ONLY, null)
                .setResultCallback(driveContentsCallback);
        mSelectedFileDriveId = null;
    }

    private ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback =
            new ResultCallback<DriveApi.DriveContentsResult>()  {
                @Override
                public void onResult(DriveApi.DriveContentsResult result){
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

    final private ChangeListener changeListener = new ChangeListener() {
        @Override
        public void onChange(ChangeEvent event) {
            Log.d(TAG, "changes.");
        }
    };

    private void toggle() {
        new Thread(new Runnable() {
            public void run() {
                driveFolder = Drive.DriveApi.getRootFolder(mGoogleApiClient);
                driveFolder.addChangeListener(getGoogleApiClient(), changeListener);
                Log.d(TAG, "Starting to listen to the file changes on: " + driveFolder.toString());
            }
        }).start();
    }



}
