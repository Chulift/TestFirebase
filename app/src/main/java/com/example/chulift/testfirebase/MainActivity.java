package com.example.chulift.testfirebase;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CALL_PHONE},
                Settings.REQUEST_CALL_PERMISSION
                );
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new HeartRateDetailFragment())
                .commit();
    }

}
