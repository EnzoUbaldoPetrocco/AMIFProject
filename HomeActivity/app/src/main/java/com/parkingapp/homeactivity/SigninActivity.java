package com.parkingapp.homeactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class SigninActivity extends Activity {

    Button btRegistrati=null;
    EditText etUsername=null;
    EditText etPassword=null;
    EditText etCittàProvenienza=null;
    EditText etPasswordConferma=null;
    ImageButton imBackBottone=null;
    ProgressBar pbProgressBarSignin=null;
    TextView tvErrorePassword=null;
    TextView tvErroreUsername=null;
    TextView tvErroreUsernameOrPassword=null;
    TextView tvErroreLunghezza=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        Intent i= getIntent();

        btRegistrati=findViewById(R.id.bttRegistrati);
        etPassword=findViewById(R.id.etSignin_Password);
        etPasswordConferma=findViewById(R.id.etSignin_Password_Conferma);
        etCittàProvenienza=findViewById(R.id.etCittàProvenienza);
        imBackBottone=findViewById(R.id.bttBackSignin_to_LoginSignin);
        pbProgressBarSignin=findViewById(R.id.pbSignin);
        etUsername= findViewById(R.id.etSignin_Username);
        tvErrorePassword=findViewById(R.id.tvErrorePassword_Signin);
        tvErroreUsername=findViewById(R.id.tvErroreUsername_Signin);
        tvErroreUsernameOrPassword=findViewById(R.id.tvErroreUsernameOrPassword_Signin);
        tvErroreLunghezza=findViewById(R.id.tvErroreLunghezza);





        imBackBottone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getString(R.string.MAIN_TO_LOGSIGN)); //Torno alla schermata di prima (Login/Signin)
                startActivity(intent);
            }
        });

        btRegistrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password= etPassword.getText().toString();
                String password_conferma= etPasswordConferma.getText().toString();
                String username= etUsername.getText().toString();

                if(!password.equals("") && !username.equals("")) {

                    tvErroreUsernameOrPassword.setVisibility(View.INVISIBLE);

                    if (!password.equals(password_conferma)) {
                        tvErrorePassword.setVisibility(View.VISIBLE);

                        if(username.length()<=25 && password.length()<=25)
                        {
                            tvErroreLunghezza.setVisibility(View.INVISIBLE); //Fine controlli, inzio body






                        }
                        else
                        {
                            tvErroreLunghezza.setVisibility(View.VISIBLE);
                        }
                    }

                    else {
                        tvErrorePassword.setVisibility(View.INVISIBLE);
                    }
                }
                else
                {
                    tvErroreUsernameOrPassword.setVisibility(View.VISIBLE);
                }
            }
        });


    }
}
