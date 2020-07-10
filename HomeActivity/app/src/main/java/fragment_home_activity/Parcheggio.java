package fragment_home_activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parkingapp.homeactivity.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Parcheggio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Parcheggio extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button bttElimina=null;
    Button bttMostraSullaMappa=null;
    TextView tvLuogoParcheggio=null;
    TextView tvOrarioParcheggio=null;

    public Parcheggio() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Parcheggio.
     */
    // TODO: Rename and change types and number of parameters
    public static Parcheggio newInstance(String param1, String param2) {
        Parcheggio fragment = new Parcheggio();
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
        return inflater.inflate(R.layout.fragment_parcheggio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bttElimina= view.findViewById(R.id.btEliminaParcheggio);
        bttMostraSullaMappa=view.findViewById(R.id.btMostraSullaMappa);
        tvLuogoParcheggio=view.findViewById(R.id.tvPosizioneParcheggio);
        tvOrarioParcheggio=view.findViewById(R.id.tvOrarioParcheggio);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("PARCHEGGIO", Context.MODE_PRIVATE);
        final String luogo_parcheggio=sharedPreferences.getString("PARCHEGGIO", "Nessun parcheggio salvato");
        sharedPreferences=getActivity().getSharedPreferences("ORARIO_PARCHEGGIO", Context.MODE_PRIVATE);
        String orario_parcheggio=sharedPreferences.getString("ORARIO_PARCHEGGIO", "");

        tvLuogoParcheggio.setText(luogo_parcheggio);
        tvOrarioParcheggio.setText(orario_parcheggio);

        bttElimina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(luogo_parcheggio.equals("Nessun parcheggio salvato"))
                {
                    Toast.makeText(getContext(), "Nessun parcheggio salvato", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent i = new Intent(getString(R.string.ELIMINA_PARCHEGGIO));
                    startActivity(i);
                }
            }
        });

        bttMostraSullaMappa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(luogo_parcheggio.equals("Nessun parcheggio salvato"))
                {
                    Toast.makeText(getContext(), "Nessun parcheggio da mostrare", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent i = new Intent(getString(R.string.FRAGMENT_PARCHEGGIO_TO_MOSTRA_SULLA_MAPPA));
                    startActivity(i);
                }
            }
        });
    }
}
