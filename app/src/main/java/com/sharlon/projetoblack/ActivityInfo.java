package com.sharlon.projetoblack;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

public class ActivityInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        VideoView video = findViewById(R.id.videoView);

        Uri uri = Uri.parse("Aqui vai a uri do video para exibir");

        video.setVideoURI(uri);

        video.setMediaController(new MediaController(this));


    }
}
