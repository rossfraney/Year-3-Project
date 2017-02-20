package com.example.ross.opendrive;

import android.util.Log;

import com.google.android.gms.drive.events.ChangeEvent;
import com.google.android.gms.drive.events.DriveEventService;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by Ross on 20-Feb-17.
 */

public class MyDriveEventService extends DriveEventService{
    @Override
    public void onChange(ChangeEvent event){
        Log.d(TAG, event.toString());
    }
}
