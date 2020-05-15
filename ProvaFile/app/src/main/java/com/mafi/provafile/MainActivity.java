package com.mafi.provafile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    TextView tv1=null;
    EditText tv2= null;
    Button btt=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1=findViewById(R.id.tv);
        tv2=findViewById(R.id.tv2);
        btt=findViewById(R.id.btt);


        File f = new File("/com.mafi.provafile.Variabili");


        tv2.setText(Mist.string);

        btt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String prova= tv.getText().toString();
                Mist.string=prova;

                tv.setText(prova);

            }
        });


    }
}
