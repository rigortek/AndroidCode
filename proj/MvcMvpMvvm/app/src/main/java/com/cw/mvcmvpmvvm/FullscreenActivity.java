package com.cw.mvcmvpmvvm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cw.mvcmvpmvvm.mvc.MvcActivity;
import com.cw.mvcmvpmvvm.mvp.MvpActivity;
import com.cw.mvcmvpmvvm.mvvm.MvvmActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    private Button mvc;
    private Button mvp;
    private Button mvvm;
    private View.OnClickListener onClickListener;

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mvc:
                    startTargetActivity(MvcActivity.class);
                    break;
                case R.id.mvp:
                    startTargetActivity(MvpActivity.class);
                    break;
                case R.id.mvvm:
                    startTargetActivity(MvvmActivity.class);
                    break;

                default:
                    break;
            }
        }

        private void startTargetActivity(Class<?> cls) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setClass(FullscreenActivity.this, cls);
            FullscreenActivity.this.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        mvc = findViewById(R.id.mvc);
        mvp = findViewById(R.id.mvp);
        mvvm = findViewById(R.id.mvvm);

        onClickListener = new MyOnClickListener();
        mvc.setOnClickListener(onClickListener);
        mvp.setOnClickListener(onClickListener);
        mvvm.setOnClickListener(onClickListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

}