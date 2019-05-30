package com.sharlon.projetoblack;


import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sharlon.projetoblack.classes.Usuarios;

import java.util.Objects;

public class FirebaseAcitivity extends AppCompatActivity {

    //BANCO DE DADOS AQUI

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);

        inicializarFireBase();


    }

    public void inicializarFireBase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Usuarios");
    }

    public void criarUsuario(Usuarios usuarios) {
        inicializarFireBase();
        databaseReference.child(usuarios.getId()).setValue(usuarios);
    }

    public void existeUser(final Usuarios u) {
        inicializarFireBase();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    if (dataSnapshot.child(u.getId()).exists()) {
                        // usuario existe
                        System.out.println("usuario existente");
                    } else {
                        // criar usuario
                        criarUsuario(u);
                        System.out.println("usuario criado");
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void excluirHistorico(){
        inicializarFireBase();
        databaseReference.child(firebaseUser.getUid()).child("historico").setValue("");
    }

    public void updateHistorico(String historico, String data){
        inicializarFireBase();
        databaseReference.child(firebaseUser.getUid()).child(data).setValue(historico);
    }

    public void pegarHistorico(final String data) {
        inicializarFireBase();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Usuarios u = snapshot.getValue(Usuarios.class);

                        assert u != null;
                        if (u.getId().equals(firebaseUser.getUid())) {

                            if (snapshot.child(data).exists()){

                                String teste =  Objects.requireNonNull(snapshot.child(data).getValue()).toString();
                                FragmentHistorico.txtTreino.setText(teste);

                            }else{
                                FragmentHistorico.txtTreino.setText("Nada registrado nesse dia");
                            }
                        }

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
