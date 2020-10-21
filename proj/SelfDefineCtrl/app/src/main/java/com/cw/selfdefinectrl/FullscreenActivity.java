package com.cw.selfdefinectrl;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cw.derivefromctrl.SubmitButton;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    SubmitButton submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);
        submitButton = findViewById(R.id.deriveFromCtrlButton);
        if (null != submitButton) {
            submitButton.setText("No default text");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}