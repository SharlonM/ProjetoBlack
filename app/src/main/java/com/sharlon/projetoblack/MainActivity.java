package com.sharlon.projetoblack;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.sharlon.projetoblack.classes.Usuarios;

public class MainActivity extends AppCompatActivity {

    ImageButton btnLogin,btnInfo,btnLoca;
    FloatingActionButton btnFeed;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFeed = findViewById(R.id.floatingActionButton);
        btnInfo = findViewById(R.id.img_info);
        btnLoca = findViewById(R.id.img_gps);
        btnLogin = findViewById(R.id.img_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent i = new Intent(MainActivity.this, ActivityLogin.class);
                    startActivity(i);
                }else{
                    Intent i = new Intent(MainActivity.this,UserActivity.class);
                    startActivity(i);
                }
            }
        });

        btnLoca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,ActivityLozalizacao.class);
                startActivity(i);
            }
        });

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,ActivityInfo.class);
                startActivity(i);
            }
        });

        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,ActivityFeedback.class);
                startActivity(i);
            }
        });
    }

}
