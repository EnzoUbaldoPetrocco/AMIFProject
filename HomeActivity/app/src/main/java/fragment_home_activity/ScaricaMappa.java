package fragment_home_activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parkingapp.homeactivity.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScaricaMappa#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScaricaMappa extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView tvMappe=null;
    Button btMenuMappe=null;
    ListView listaMappe=null;
    ProgressBar progressBar=null;
    Button btScarica=null;

    Boolean premuto=false; //Variabile per capire lo stato del pulsante per vedere menu con mappe da scaricare


    public ScaricaMappa() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScaricaMappa.
     */
    // TODO: Rename and change types and number of parameters
    public static ScaricaMappa newInstance(String param1, String param2) {
        ScaricaMappa fragment = new ScaricaMappa();
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
        return inflater.inflate(R.layout.fragment_scarica_mappa, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btMenuMappe=view.findViewById(R.id.btMenuMappeDaScaricare);
        btScarica=view.findViewById(R.id.btConfermaMappaDaScaricare);
        progressBar=view.findViewById(R.id.pbMappeDaScaricare);
        listaMappe=view.findViewById(R.id.lvListaMappeDaScaricare);
        tvMappe=view.findViewById(R.id.tvMappeDaScaricare);




    }
}
