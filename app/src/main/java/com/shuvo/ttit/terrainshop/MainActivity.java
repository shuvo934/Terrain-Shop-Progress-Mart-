package com.shuvo.ttit.terrainshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.shuvo.ttit.terrainshop.firstpage.ChoicePage;
import com.shuvo.ttit.terrainshop.homepage.HomePage;

public class MainActivity extends AppCompatActivity {

    private final Handler mHandler = new Handler();

    SharedPreferences sharedPreferences;
    boolean loginfile = false;

    public static final String LOGIN_ACTIVITY_FILE = "LOGIN_ACTIVITY_FILE_ESHOP";
//    public static final String AD_ID = "AD_ID";
//    public static final String AD_CODE = "AD_CODE";
//    public static final String AD_NAME = "AD_NAME";
//    public static final String AD_ADDRESS = "AD_ADDRESS";
//    public static final String AD_PHONE = "AD_PHONE";
//    public static final String AD_EMAIL = "AD_EMAIL";
    public static final String LOGIN_TF = "TRUE_FALSE";

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(LOGIN_ACTIVITY_FILE, MODE_PRIVATE);

//        SharedPreferences.Editor editor1 = sharedPreferences.edit();
//        editor1.remove(AD_ID);
//        editor1.remove(AD_CODE);
//        editor1.remove(AD_NAME);
//        editor1.remove(AD_ADDRESS);
//        editor1.remove(AD_PHONE);
//        editor1.remove(AD_EMAIL);
//        editor1.remove(LOGIN_TF);
//
//        editor1.apply();
//        editor1.commit();

        loginfile = sharedPreferences.getBoolean(LOGIN_TF,false);

        System.out.println(loginfile);

        enableFileAccess();
    }

    private void goToActivityMap() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (loginfile) {
                    Intent intent = new Intent(MainActivity.this, HomePage.class);
                    startActivity(intent);
                    showSystemUI();
                    finish();
                } else {
                    Intent intent = new Intent(MainActivity.this, ChoicePage.class);
                    startActivity(intent);
                    showSystemUI();
                    finish();
                }
            }
        }, 3000);
    }

    private void enableFileAccess() {
        if (Build.VERSION.SDK_INT >= 33) {
            int REQUEST_CODE_PERMISSION_STORAGE = 100;
            String[] permission = {
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

            for (String str : permission) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    this.requestPermissions(permission, REQUEST_CODE_PERMISSION_STORAGE);
                    return;
                }
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                goToActivityMap();
            }
        }
        else {
            int REQUEST_CODE_PERMISSION_STORAGE = 100;
            String[] permission = {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

            for (String str : permission) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    this.requestPermissions(permission, REQUEST_CODE_PERMISSION_STORAGE);
                    return;
                }
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                goToActivityMap();
            }
        }
    }
//    private void enableFileAccess() {
//
//        if (Build.VERSION.SDK_INT >= 23) {
//            int REQUEST_CODE_PERMISSION_STORAGE = 100;
//            String[] permission = new String[0];
//            permission = new String[]{
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//            };
//
//
//
//            for (String str : permission) {
//                System.out.println(str);
//                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
//                    System.out.println(str);
//                    this.requestPermissions(permission, REQUEST_CODE_PERMISSION_STORAGE);
//                    return;
//                }
//
//            }
//
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
//                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                goToActivityMap();
//            }
//
//
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                goToActivityMap();
                // Permission is granted. Continue the action or workflow
                // in your app.
            } else {
                System.exit(0);
                // Explain to the user that the feature is unavailable because
                // the features requires a permission that the user has denied.
                // At the same time, respect the user's decision. Don't link to
                // system settings in an effort to convince the user to change
                // their decision.
            }
        }
    }
}