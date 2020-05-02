package RichiesteConferma;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.parkingapp.homeactivity.HomeActivity;
import com.parkingapp.homeactivity.R;

import fragment_home_activity.HomeFragment;
import fragment_home_activity.MappeScaricate;
import fragment_home_activity.Parcheggio;
import fragment_home_activity.PromemoriaNotifica;
import fragment_home_activity.ScaricaMappa;
import fragment_home_activity.Utente;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.Console;


public class RichiestaConferma extends AppCompatActivity {

    Button bttConferma=null;
    Button bttAnnulla=null;

    MenuItem menuItem=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.richiesta_conferma);

        final Intent intent= getIntent();

        bttConferma=findViewById(R.id.btConfermaUscitaUtente);
        bttAnnulla=findViewById(R.id.btAnnullaUscitaUtente);

        final String _s= intent.getStringExtra(getString(R.string.Parcheggio));


        bttAnnulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i= new Intent(getString(R.string.MAIN_TO_HOME));
                startActivity(i);
            }
        });

        bttConferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                //A seconda della stringa che gli si passa questa activity esegue
                //un preciso task

                switch (_s)
                {
                    case "RitornoAScaricaMappa":

                        break;

                    case "RitornoAMappeScaricate":

                        break;

                    case "Parcheggio":
                        Log.i("OH FRATE", "OH FRATE!");
                        break;

                    case "RitornoAPromemoriaNotifica":

                        break;

                    case "RitornoAUtente":

                        break;

                    case "RitornoAHomeFragment":

                        break;

                    default:
                        Intent i= new Intent(getString(R.string.MAIN_TO_HOME));
                        startActivity(i);
                }



            }
        });

    }
}
