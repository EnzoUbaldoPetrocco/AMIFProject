package com.parkingapp.homeactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import mist.Variabili;

public class LogInActivity extends Activity {

    Button bttOk=null;
    ImageButton bttBack=null;
    TextView tvErrore=null;
    EditText usernameLogin=null;
    EditText passwordLogin=null;
    ProgressBar progressBarLogin=null;
    CheckBox checkBoxLogin=null;
    Context context=this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Intent i= getIntent();
        bttBack=findViewById(R.id.bttBackLogin_to_LoginSignin); //Per tornare indiero alla schermata scelta login/signin
        bttOk=findViewById(R.id.bttOkLogin);//Confermare login
        tvErrore=findViewById(R.id.tvErroreUsernameOrPassword_Login);
        usernameLogin=findViewById(R.id.etUsername);
        passwordLogin=findViewById(R.id.etPassword);
        progressBarLogin=findViewById(R.id.pbLogin);
        checkBoxLogin=findViewById(R.id.cbCheckBox_Login);

        bttOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String username=usernameLogin.getText().toString();
               String password=passwordLogin.getText().toString();



                //Verifico eventuali username o password errati prima di mandare i controlli al sever
               if(!username.equals("") && !password.equals("") && username.length()<=25 && password.length()<=25)
               {
                tvErrore.setVisibility(View.INVISIBLE);



                //In seguito all'autenticazione, prima di passare alla HomeActivity salvo lo stato della checkbox
                   Variabili.salvaRicordaUtente(context, checkBoxLogin.isChecked());

                   Intent i = new Intent(getString(R.string.MAIN_TO_HOME));
                   startActivity(i);
               }
               else
               {
                   tvErrore.setVisibility(View.VISIBLE);
               }


            }
        });

        bttBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getString(R.string.MAIN_TO_LOGSIGN)); //Torno alla schermata di prima (Login/Signin)
                startActivity(intent);
            }
        });
    }
}
