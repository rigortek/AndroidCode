package com.cw.rxandroidsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cw.rxandroidsample.observerpattern.CallerCenter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CallerCenter.test();
    }
}