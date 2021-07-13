package com.cw.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "jcw";
    RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRelativeLayout = new RelativeLayout(this);
        Log.d(TAG, "---------- onCreate: ---------- " + mRelativeLayout);

        Intent intent = new Intent();
        intent.setClass(this, GloabKeyLinstenService.class);
        startService(intent);

        Log.d(TAG, "---------- onCreate: ---------- " + mRelativeLayout);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "---------- onNewIntent: ---------- " + mRelativeLayout);

        super.onNewIntent(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "---------- onKeyDown: ---------- " + keyCode);

        printInstalledCertificates();

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "---------- onStart: ----------" + mRelativeLayout);
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "---------- onRestart: ---------- " + mRelativeLayout);
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "---------- onResume: ---------- " + mRelativeLayout);
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "---------- onPause: ----------" + mRelativeLayout);
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "---------- onStop: ---------- " + mRelativeLayout);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "---------- onDestroy: ---------- " + mRelativeLayout);
        super.onDestroy();
    }

    public void printInstalledCertificates() {
        try {
            KeyStore ks = KeyStore.getInstance("AndroidCAStore");

            if (ks != null) {
                ks.load(null, null);
                Enumeration<String> aliases = ks.aliases();
                DateFormat format = new SimpleDateFormat("yyyy/MM/dd");

                int count = 0;
                while (aliases.hasMoreElements()) {

                    String alias = (String) aliases.nextElement();

                    java.security.cert.X509Certificate cert = (java.security.cert.X509Certificate) ks.getCertificate(alias);
//                    //To print System Certs only
//                    if(cert.getIssuerDN().getName().contains("system")){
//                        Log.d(TAG, "1 printInstalledCertificates: " + cert.getIssuerDN().getName());
//                    }
//
//                    //To print User Certs only
//                    if(cert.getIssuerDN().getName().contains("user")){
//                        Log.d(TAG, "2 printInstalledCertificates: " + cert.getIssuerDN().getName());
//                    }

                    ++count;

                    //To print all certs
                    Log.d(TAG, "颁发给: \r\n"
                            + cert.getIssuerX500Principal() + "\r\n"
                            + "序列号 :" + cert.getSerialNumber()
                            + "\r\n\r\n"
                            + "颁发者: \r\n"
                            + cert.getSubjectX500Principal()
                            + "\r\n\r\n"
                            + "有效期: \r\n"
                            + "颁发时间: " + format.format(cert.getNotBefore()) + "\r\n"
                            + "有效期至: " + format.format(cert.getNotAfter()) + "\r\n"
                    );
                }

                Log.d(TAG, "printInstalledCertificates: count->" + count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (java.security.cert.CertificateException e) {
            e.printStackTrace();
        }
    }
}