package com.example.ross.opendrive;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.util.Properties;

public class setSshId extends Main2Activity {

    private EditText eText;
    private Button btn;
    private static String str;
    private static String Success;

    //Creates button with a listener to register above editText and set String str to result
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_ssh_id);
        eText = (EditText) findViewById(R.id.edittext);
        tv1.setText("Enter Your Pi's IP Address and tap'TEST'");
        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(v == btn) {
                    str = eText.getText().toString();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("IP", str);
                    editor.apply();
                    ((EditText) findViewById(R.id.edittext)).setText(str);
                    myHost = str;
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            tester();
                        }
                    });
                    t.start();
                    try {
                        t.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(Success != null){
                        showMessage("Connection Successful");
                    }else{
                        showMessage("Unsuccessful");
                    }
                    finish();
                }
            }
        });
    }

    //getter
    public void tester(){
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession("pi", myHost, 22);
            session.setPassword("raspberry");
            // Avoid asking for key confirmation
            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            session.setConfig(prop);
            session.connect();
            Success = "Connection Successfully Saved";

            Channel channelssh = session.openChannel("exec");
            channelssh.setInputStream(null);
            ((ChannelExec) channelssh).setErrStream(System.err);

            channelssh.connect();
            channelssh.disconnect();
            session.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

