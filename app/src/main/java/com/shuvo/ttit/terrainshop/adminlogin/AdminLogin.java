package com.shuvo.ttit.terrainshop.adminlogin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.adminlogin.reports.ReportPage;
import com.shuvo.ttit.terrainshop.login.Login;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static com.shuvo.ttit.terrainshop.connection.OracleConnection.createConnection;

public class AdminLogin extends AppCompatActivity {

    TextInputEditText user;
    TextInputEditText pass;

    TextView login_failed;

    Button login;

    String usrName = "";
    String password = "";

    private Boolean conn = false;
    private Boolean infoConnected = false;
    private Boolean connected = false;

    private Connection connection;
    private AsyncTask mTask;

    LinearLayout fullLayout;
    CircularProgressIndicator circularProgressIndicator;

    String NAME = "";
    String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        fullLayout = findViewById(R.id.admin_login_full_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_admin_log_in);
        circularProgressIndicator.setVisibility(View.GONE);

        user = findViewById(R.id.user_name_given_admin_log_in);
        pass = findViewById(R.id.password_given_admin_log_in);

        login_failed = findViewById(R.id.email_pass_miss_admin_login);

        login = findViewById(R.id.admin_log_in_button);

        pass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        Log.i("Let see", "Come here");
                        pass.clearFocus();
                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        closeKeyBoard();

                        return false; // consume.
                    }
                }
                return false;
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeKeyBoard();

                login_failed.setVisibility(View.GONE);
                usrName = user.getText().toString();
                password = pass.getText().toString();

                if (!usrName.isEmpty() && !password.isEmpty()) {


                    mTask = new CheckLogin().execute();


                } else {
                    Toast.makeText(getApplicationContext(), "Please Give UserName and Password", Toast.LENGTH_SHORT).show();
                }

            }
        });

        closeKeyBoard();
        user.clearFocus();
        pass.clearFocus();
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(user.getWindowToken(), 0);
    }

    @Override
    public boolean onTouchEvent (MotionEvent event){
        closeKeyBoard();
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        if (mTask != null) {
            if (mTask.getStatus().toString().equals("RUNNING")) {
                System.out.println("can't exit");
            } else {
                finish();
            }
        }
        else {
            finish();
        }
    }

    private void closeKeyBoard () {
        View view = this.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

    public class CheckLogin extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            circularProgressIndicator.setVisibility(View.VISIBLE);
            fullLayout.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                LoginQuery();
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

                if (!userId.equals("-1")) {
                    if (infoConnected) {

                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), ReportPage.class);
                        intent.putExtra("NAME OF ADMIN",NAME);
                        intent.putExtra("ID OF ADMIN",userId);
                        startActivity(intent);
                        finish();

                    } else {
                        mTask = new CheckLogin().execute();
                    }

                } else {
                    login_failed.setVisibility(View.VISIBLE);
                }

                conn = false;
                connected = false;
                infoConnected = false;


            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(AdminLogin.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new CheckLogin().execute();
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public void LoginQuery () {
        try {
            this.connection = createConnection();

//            userInfoLists = new ArrayList<>();
//            userDesignations = new ArrayList<>();
//            isApproved = 0;
//            isLeaveApproved = 0;

            NAME = "";

            Statement stmt = connection.createStatement();

            userId = "";

            ResultSet rs = stmt.executeQuery("select VALIDATE_USER_DB('" + usrName + "',HAMID_ENCRYPT_DESCRIPTION_PACK.HEDP_ENCRYPT('" + password + "')) val from dual\n");

            while (rs.next()) {
                userId = rs.getString(1);
            }
            rs.close();

            if (!userId.equals("-1")) {

                ResultSet resultSet = stmt.executeQuery("Select USR_NAME, USR_FNAME, USR_LNAME, USR_EMAIL, USR_CONTACT, USR_EMP_ID, USR_ID FROM ISP_USER\n" +
                        "where USR_ID = " + userId + "\n");

                while (resultSet.next()) {
                    NAME = resultSet.getString(2) + " " + resultSet.getString(3);
                }
                resultSet.close();
                infoConnected =  true;
            }

            connected = true;

            connection.close();

        } catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }

    }
}