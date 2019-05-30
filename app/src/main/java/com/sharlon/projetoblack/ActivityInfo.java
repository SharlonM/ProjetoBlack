package com.sharlon.projetoblack;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ActivityInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        System.out.println("Todos os outros commits foram de teste, apartir de agora comeca a contar");
        TextView info = findViewById(R.id.txtInfo);
    }
}
