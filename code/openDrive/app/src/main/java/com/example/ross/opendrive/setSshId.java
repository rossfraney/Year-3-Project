package com.example.ross.opendrive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.InputStream;
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
        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(v == btn) {
                    str = eText.getText().toString();
                    myHost = str;
                    tester();
                    if(Success != null){
                        showMessage(Success);
                    }else{
                        showMessage("Connection Failed");
                    }
                    finish();
                }
            }
        });
    }

    //getter
    public static String getIp(){
        return str;
    }
    public void tester(){
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
                    session.connect();
                    Success = "Connection Successfully Saved";


                    Channel channelssh = session.openChannel("exec");
                    //((ChannelExec) channelssh).setCommand("python /home/pi/securiPi/pi_surveillance.py --conf /home/pi/securiPi/conf.json ");
                    channelssh.setInputStream(null);
                    ((ChannelExec) channelssh).setErrStream(System.err);
                    InputStream in = channelssh.getInputStream();

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

