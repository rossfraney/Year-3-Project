package com.example.ross.opendrive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class setNeighbour extends Main2Activity {

    private EditText eText;
    private Button btn;
    private static String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_neighbour);
        eText = (EditText) findViewById(R.id.edittext);
        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(v == btn) {
                    str = eText.getText().toString();
                    neighboursNum = str;
                    showMessage("New Number: " + neighboursNum);
                    finish();
                }
            }
        });
    }

    public static String getNum(){
        return str;
    }
}
