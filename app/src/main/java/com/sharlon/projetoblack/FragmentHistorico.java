package com.sharlon.projetoblack;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHistorico extends Fragment {

    @SuppressLint("StaticFieldLeak")
    static TextView txtTreino;
    CalendarView calendario;
    FirebaseAcitivity firebaseAcitivity = new FirebaseAcitivity();


    public FragmentHistorico() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_historico, container, false);

        txtTreino = view.findViewById(R.id.txt_historico);
        txtTreino.setVisibility(View.INVISIBLE);
        calendario = view.findViewById(R.id.calendario);


        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String data = year +"/"+ month +"/"+ dayOfMonth;

                firebaseAcitivity.pegarHistorico(data);
                txtTreino.setVisibility(View.VISIBLE);
            }
        });


        return view;
    }

}
