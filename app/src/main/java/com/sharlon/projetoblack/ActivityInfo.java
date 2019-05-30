package com.sharlon.projetoblack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ActivityInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        System.out.println("teste");
    }
}
