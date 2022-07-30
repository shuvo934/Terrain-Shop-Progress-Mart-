package com.shuvo.ttit.terrainshop.profile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.checkout.CheckOut;
import com.shuvo.ttit.terrainshop.checkout.lists.DistrictList;
import com.shuvo.ttit.terrainshop.orderComplete.OrderCompleted;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static com.shuvo.ttit.terrainshop.cart.OrderCart.myCartList;
import static com.shuvo.ttit.terrainshop.connection.OracleConnection.createConnection;
import static com.shuvo.ttit.terrainshop.login.Login.userInfoLists;

public class UpdateProfile extends AppCompatActivity {

    ImageView close;
    TextView actionbarText;

    RelativeLayout fullLayout;
    CircularProgressIndicator circularProgressIndicator;

    TextInputLayout textInputLayout;
    TextInputEditText editText;
    MaterialButton save;

    String text = "";
    String updateInfo = "";
    String ad_id = "";

    Connection connection;
    private Boolean conn = false;
    private Boolean connected = false;

    public static final String LOGIN_ACTIVITY_FILE = "LOGIN_ACTIVITY_FILE_ESHOP";

    public static final String AD_NAME = "AD_NAME";
    public static final String AD_ADDRESS = "AD_ADDRESS";
    public static final String AD_EMAIL = "AD_EMAIL";
    SharedPreferences sharedLogin;
    private AsyncTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.back_color));

        sharedLogin = getSharedPreferences(LOGIN_ACTIVITY_FILE,MODE_PRIVATE);

        close = findViewById(R.id.close_logo_update_profile);
        actionbarText = findViewById(R.id.name_of_the_update_attribute);

        fullLayout = findViewById(R.id.update_profile_full_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_update_profile);
        circularProgressIndicator.setVisibility(View.GONE);

        textInputLayout = findViewById(R.id.update_text_layout_update_profile);
        editText = findViewById(R.id.update_text_given_update_profile);
        save = findViewById(R.id.save_button_update_profile);

        ad_id = userInfoLists.get(0).getAd_id();

        Intent intent = getIntent();
        text = intent.getStringExtra("ATTRIBUTE");
        updateInfo = intent.getStringExtra("WHICH TO UPDATE");

        String action = "UPDATE " + updateInfo;
        actionbarText.setText(action);

        textInputLayout.setHint(updateInfo);

        editText.setText(text);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTask != null) {
                    if (mTask.getStatus().toString().equals("RUNNING")) {
                        Toast.makeText(getApplicationContext(),"Please wait while loading",Toast.LENGTH_SHORT).show();
                    } else {
                        finish();
                    }
                }
                else {
                    finish();
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = editText.getText().toString();
                if (!text.isEmpty()) {
                    mTask = new UpdateCheck().execute();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please give information to update",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mTask != null) {
            if (mTask.getStatus().toString().equals("RUNNING")) {
                Toast.makeText(getApplicationContext(),"Please wait while loading",Toast.LENGTH_SHORT).show();
            } else {
                finish();
            }
        }
        else {
            finish();
        }
    }

    public boolean isConnected () {
        boolean connected = false;
        boolean isMobile = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    public boolean isOnline () {

        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    public class UpdateCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            circularProgressIndicator.setVisibility(View.VISIBLE);
            fullLayout.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                UpdateQuery();
                if (connected) {
                    conn = true;
                }

            } else {
                conn = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            circularProgressIndicator.setVisibility(View.GONE);
            fullLayout.setVisibility(View.VISIBLE);

            if (conn) {

                SharedPreferences.Editor editor1 = sharedLogin.edit();
                switch (updateInfo) {
                    case "NAME":
                        userInfoLists.get(0).setAd_name(text);
                        editor1.remove(AD_NAME);
                        editor1.putString(AD_NAME, userInfoLists.get(0).getAd_name());
                        editor1.apply();
                        editor1.commit();
                        break;
                    case "EMAIL":
                        userInfoLists.get(0).setAd_email(text);
                        editor1.remove(AD_EMAIL);
                        editor1.putString(AD_EMAIL, userInfoLists.get(0).getAd_email());
                        editor1.apply();
                        editor1.commit();
                        break;
                    case "ADDRESS":
                        userInfoLists.get(0).setAd_address(text);
                        editor1.remove(AD_ADDRESS);
                        editor1.putString(AD_ADDRESS, userInfoLists.get(0).getAd_address());
                        editor1.apply();
                        editor1.commit();
                        break;
                }
                Toast.makeText(getApplicationContext(), "Successfully Updated",Toast.LENGTH_SHORT).show();
                finish();

            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(UpdateProfile.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mTask = new UpdateCheck().execute();
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public void UpdateQuery () {

        try {
            this.connection = createConnection();

            Statement stmt = connection.createStatement();

            switch (updateInfo) {
                case "NAME":
                    stmt.executeUpdate("UPDATE ACC_DTL SET AD_NAME = '"+ text +"' WHERE AD_ID = "+ ad_id +"");
                    break;
                case "EMAIL":
                    stmt.executeUpdate("UPDATE ACC_DTL SET ad_email = '"+ text +"' WHERE AD_ID = "+ ad_id +"");
                    break;
                case "ADDRESS":
                    stmt.executeUpdate("UPDATE ACC_DTL SET ad_address = '"+ text +"' WHERE AD_ID = "+ ad_id +"");
                    break;
            }


            stmt.close();

            connected = true;

            connection.close();

        } catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }

    }
}