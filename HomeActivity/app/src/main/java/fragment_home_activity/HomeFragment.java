package fragment_home_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parkingapp.homeactivity.R;

import static mist.Variabili.salvaDestinazione;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button bttAvvia=null;
    TextView tvScegliCittà=null;
    Button btMenu=null;
    ListView lvCittàDestinazione=null;
    TextView avviso1=null;
    TextView avviso2=null;
    Boolean premuto=false; //Parametro per capire lo stato del menu con le scelte delle città


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bttAvvia=view.findViewById(R.id.bttAvviaHomeFragment);
        tvScegliCittà=view.findViewById(R.id.tvCittàDestinazione);
        avviso1=view.findViewById(R.id.tvAvviso1);
        avviso2=view.findViewById(R.id.tvAvviso2);
        lvCittàDestinazione=view.findViewById(R.id.lvListaCittàDestinazione);
        btMenu=view.findViewById(R.id.btMenuCittàDestinazioneHomeFragment);

        //Gestisco la visibilità menu a tendina con le città da scegliere
        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(premuto==false)
                {
                    lvCittàDestinazione.setVisibility(View.VISIBLE);
                    premuto=true;
                }
                else
                {
                    lvCittàDestinazione.setVisibility(View.INVISIBLE);
                    premuto=false;
                }
            }
        });

        //avvia il task principale dell'app
        bttAvvia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context= getContext();
                String testo=tvScegliCittà.getText().toString();
                salvaDestinazione(context, testo);

         //       if(!testo.equals(" ----")) //Primo controllo sulla scelta della destinazione
       //         {
                    avviso1.setVisibility(View.INVISIBLE);//Se la verifica è rispettata lo rimetto invisibile
                    //Scrivere secondo controllo non appena si gestiranno le mappe scaricate


                    //Passo all'activity di esecuzione
                    Intent i = new Intent(getString(R.string.HOME_FRAGMENT_TO_ESECUZIONE));
                    startActivity(i);
          /*      }
                else
                {
                    avviso1.setVisibility(View.VISIBLE);
                } */

            }
        });

    }
}
