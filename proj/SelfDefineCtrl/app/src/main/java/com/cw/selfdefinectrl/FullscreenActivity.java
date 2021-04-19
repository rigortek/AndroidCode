package com.cw.selfdefinectrl;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
        final LineItemView lineItemViewOne = (LineItemView)findViewById(R.id.one);
        final LineItemView lineItemViewTwo = (LineItemView)findViewById(R.id.two);
        final LineItemView lineItemViewThree = (LineItemView)findViewById(R.id.three);


        if (null != submitButton) {
            submitButton.setText("No default text");
            submitButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        Toast.makeText(FullscreenActivity.this, "Button gain focus", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


        // 方法一：代码中设定属性
//        lineItemViewOne.setTvLeftText("First Title").setTvRightText("First content").setIvArrowVisibility(true);
//        lineItemViewTwo.setTvLeftText("Second Title").setTvRightText("Second content").setIvArrowVisibility(false);
//        lineItemViewThree.setTvLeftText("Third Title, hide content").setTvRightText("").setIvArrowVisibility(true);

        // 方法二：XML中设定属性

        ////////////////////////////////////////////////////////////////////////////////////////
        setOnClickListener(lineItemViewOne, lineItemViewTwo, lineItemViewThree);
        setOnFocusChangeListener(lineItemViewOne, lineItemViewTwo, lineItemViewThree);

        ////////////////////////////////////////////////////////////////////////////////////////
    }

    private void setOnFocusChangeListener(final LineItemView lineItemViewOne,
                                          final LineItemView lineItemViewTwo,
                                          final LineItemView lineItemViewThree) {

        lineItemViewOne.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    String notice = "item 1 gain focus";
                    Toast.makeText(FullscreenActivity.this, notice, Toast.LENGTH_SHORT).show();
                }
            }
        });

        lineItemViewTwo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    String notice = "item 2 gain focus";
                    Toast.makeText(FullscreenActivity.this, notice, Toast.LENGTH_SHORT).show();
                }
            }
        });

        lineItemViewThree.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            private Drawable drawable;
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    String notice = "item 3 gain focus";
                    Toast.makeText(FullscreenActivity.this, notice, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setOnClickListener(final LineItemView lineItemViewOne,
                                    final LineItemView lineItemViewTwo,
                                    final LineItemView lineItemViewThree) {
        lineItemViewOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FullscreenActivity.this,
                        lineItemViewOne.getLeftText()  + " : " + lineItemViewOne.getRightText(), Toast.LENGTH_SHORT).show();
            }
        });

        lineItemViewTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FullscreenActivity.this,
                        lineItemViewTwo.getLeftText()  + " : " + lineItemViewTwo.getRightText(), Toast.LENGTH_SHORT).show();
            }
        });

        lineItemViewThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FullscreenActivity.this,
                        lineItemViewThree.getLeftText()  + " : " + lineItemViewThree.getRightText(), Toast.LENGTH_SHORT).show();
            }
        });
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