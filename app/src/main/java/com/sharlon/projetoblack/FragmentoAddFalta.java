package com.sharlon.projetoblack;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.sharlon.projetoblack.classes.Usuarios;
import com.vicmikhailau.maskededittext.MaskedEditText;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentoAddFalta extends Fragment {

    FirebaseAcitivity firebaseAcitivity = new FirebaseAcitivity();

    public FragmentoAddFalta() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_falta, container, false);
        final MaskedEditText edtData = view.findViewById(R.id.edtData);

        SimpleDateFormat formatData = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
        Date date = new Date();
        edtData.setText(formatData.format(date));


        Button btnFalta = view.findViewById(R.id.btnFalta);
        btnFalta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Objects.requireNonNull(edtData.getText()).toString().isEmpty()) {

                    String aux = edtData.getText().toString();

                    String[] data1 = aux.split("/");
                    String ano = data1[0];
                    String mes = data1[1];
                    String dia = data1[2];
                    mes = mes.replace("0", "");
                    int mes1 = Integer.parseInt(mes) - 1;
                    mes = String.valueOf(mes1);

                    String data = ano + "/" + mes + "/" + dia;

                    // atualizar o historico
                    String novoHistorico = edtData.getText() + ": FALTA";

                    firebaseAcitivity.updateHistorico(novoHistorico, data);
                    Usuarios.toast(getContext(),"Falta Registrada");

                }else{
                    Usuarios.toast(getContext(),"Preencha a data");
                }

            }
        });


        return view;
    }

}
