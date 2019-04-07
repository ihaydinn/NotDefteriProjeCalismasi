package com.ismailhakkiaydin.notsepetiapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class MainActivity extends AppCompatActivity {

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        backgroundResminiYerlestir();

    }

    private void backgroundResminiYerlestir() {

        ImageView background = findViewById(R.id.iv_background);
        Glide.with(this)
                .load(R.drawable.back)
                .apply(new RequestOptions().centerCrop())
                .into(background);

    }
}
