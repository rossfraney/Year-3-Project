package com.example.ross.opendrive;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.util.Properties;

public class AlarmSound extends Main2Activity implements View.OnClickListener {

    private String alarmSound;
    private String TAG = "ALARM_SOUND_CONTEXT";
    private Button alarmSoundButton;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_sound);

        alarmSoundButton = (Button) findViewById(R.id.alarmSoundButton);
        alarmSoundButton.setOnClickListener(this);

        //Spinner is the dropdown list/menu
        spinner = (Spinner) findViewById(R.id.alarmSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                R.array.alarm_sounds, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //Listener for user's choice
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                final int choice = parent.getSelectedItemPosition();

                if (choice == 0) {
                    alarmSound = "sed -i -e 's#/home/pi/securiPi/soundFiles/.*mp3#/home/pi/securiPi/" +
                            "soundFiles/FireAlarm.mp3#g' /home/pi/securiPi/pi_surveillance.py";

                    Toast.makeText(getBaseContext(), "Alarm: Standard Alarm",
                            Toast.LENGTH_LONG).show();
                }

                if (choice == 1) {
                    alarmSound = "sed -i -e 's#/home/pi/securiPi/soundFiles/.*mp3#/home/pi/securiPi/" +
                            "soundFiles/Dog.mp3#g' /home/pi/securiPi/pi_surveillance.py";

                    Toast.makeText(getBaseContext(), "Alarm: Dog Barking",
                            Toast.LENGTH_LONG).show();
                }

                if (choice == 2) {
                    alarmSound = "sed -i -e 's#/home/pi/securiPi/soundFiles/.*mp3#/home/pi/securiPi/" +
                            "soundFiles/Lullaby.mp3#g' /home/pi/securiPi/pi_surveillance.py";

                    Toast.makeText(getBaseContext(), "Alarm: Lullaby",
                            Toast.LENGTH_LONG).show();
                }

                if (choice == 3) {
                    alarmSound = "sed -i -e 's#/home/pi/securiPi/soundFiles/.*mp3#/home/pi/securiPi/" +
                            "soundFiles/Siren.mp3#g' /home/pi/securiPi/pi_surveillance.py";
                    Toast.makeText(getBaseContext(), "Alarm: Police Siren", Toast.LENGTH_LONG).show();
                } else {
                    return;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getBaseContext(), "Alarm Unchanged", Toast.LENGTH_LONG).show();
            }
        });
    }

    //SSH Connections
    private void makeSelection(){
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
            ((ChannelExec) channelssh).setCommand(alarmSound);
            channelssh.setInputStream(null);
            ((ChannelExec) channelssh).setErrStream(System.err);

            channelssh.connect();

            //exec here

            channelssh.disconnect();
            session.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if(v == alarmSoundButton){
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    makeSelection();
                }
            });
            t.start();
            finish();
        }
    }
}
