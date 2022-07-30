package com.shuvo.ttit.terrainshop.firstpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shuvo.ttit.terrainshop.MainActivity;
import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.homepage.HomePage;
import com.shuvo.ttit.terrainshop.login.Login;

public class ChoicePage extends AppCompatActivity {

    Button goLogin;
    Button goMart;
    TextView or;

    SharedPreferences sharedPreferences;
    boolean loginfile = false;
    public static boolean loginDone = false;

    public static final String LOGIN_ACTIVITY_FILE = "LOGIN_ACTIVITY_FILE_ESHOP";
    public static final String LOGIN_TF = "TRUE_FALSE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_page);
        goLogin = findViewById(R.id.go_to_login);
        goMart = findViewById(R.id.go_to_homepage);
        or = findViewById(R.id.or_text_extra);
        sharedPreferences = getSharedPreferences(LOGIN_ACTIVITY_FILE, MODE_PRIVATE);
        loginfile = sharedPreferences.getBoolean(LOGIN_TF,false);

        if (!loginfile) {
            loginfile = loginDone;
        }

        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChoicePage.this, Login.class);
                startActivity(intent);
            }
        });

        goMart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginfile) {
                    Intent intent = new Intent(ChoicePage.this, HomePage.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(ChoicePage.this, HomePage.class);
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences = getSharedPreferences(LOGIN_ACTIVITY_FILE, MODE_PRIVATE);
        loginfile = sharedPreferences.getBoolean(LOGIN_TF,false);

        if (!loginfile) {
            loginfile = loginDone;
        }

        if (loginfile) {
            goLogin.setVisibility(View.GONE);
            or.setVisibility(View.GONE);
        } else {
            goLogin.setVisibility(View.VISIBLE);
            or.setVisibility(View.VISIBLE);
        }


    }
}