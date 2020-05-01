package com.parkingapp.homeactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class UscitaUtente extends Activity {

    Button conferma=null;
    Button annulla= null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conferma_uscita_utente);

        conferma=findViewById(R.id.btConfermaUscitaUtente);
        annulla=findViewById(R.id.btAnnullaUscitaUtente);

        annulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getString(R.string.MAIN_TO_HOME));
                startActivity(i);
            }
        });
    }
}
