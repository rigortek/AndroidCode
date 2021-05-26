package com.cw.rxandroidsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.cw.rxandroidsample.observerpattern.CallerCenter;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "jcw";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CallerCenter.test();

        Log.d(TAG, "onCreate: ");

        RxJavaSample sample = new RxJavaSample();
        sample.doRxJava();


        RxJavaOperatorSample rxJavaOperatorSample = new RxJavaOperatorSample();
//        rxJavaOperatorSample.mapOperator_String2Bitmap(this);
//        rxJavaOperatorSample.mapOperator_StringList2BitmapList(this);
        rxJavaOperatorSample.flatMapOperator();

    }
}