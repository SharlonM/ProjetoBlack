package com.sharlon.projetoblack;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.vicmikhailau.maskededittext.MaskedEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentoAddTreino extends Fragment {


    public FragmentoAddTreino() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_treino,container,false);


        // programar as coisas
        final MaskedEditText edtData = view.findViewById(R.id.edtData);

        SimpleDateFormat formatData = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date date = new Date();
        edtData.setText(formatData.format(date));

        Spinner treinosPredefinidos = view.findViewById(R.id.treinosPredefinidos);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.treinosPredefinidos, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        treinosPredefinidos.setAdapter(adapter);

        return view;
    }

}
