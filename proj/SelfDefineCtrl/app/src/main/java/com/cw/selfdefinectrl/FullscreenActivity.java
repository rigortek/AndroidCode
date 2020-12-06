package com.cw.selfdefinectrl;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cw.derivefromctrl.SubmitButton;
import com.cw.derivefromlayout.LineItemView;

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

        LineItemView lineItemViewOne = (LineItemView)findViewById(R.id.one);
        LineItemView lineItemViewTwo = (LineItemView)findViewById(R.id.two);
        LineItemView lineItemViewThree = (LineItemView)findViewById(R.id.three);


        lineItemViewOne.setTvLeftText("First Title").setTvRightText("First content").setIvArrayVisibility(true);

        lineItemViewTwo.setTvLeftText("Second Title").setTvRightText("Second content").setIvArrayVisibility(false);

        lineItemViewThree.setTvLeftText("Third Title, hide content").setTvRightText("").setIvArrayVisibility(true);
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