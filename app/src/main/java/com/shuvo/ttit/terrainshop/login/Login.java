package com.shuvo.ttit.terrainshop.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.firstpage.ChoicePage;
import com.shuvo.ttit.terrainshop.homepage.HomePage;
import com.shuvo.ttit.terrainshop.login.list.UserInfoList;
import com.shuvo.ttit.terrainshop.signup.SignUp;
import com.shuvo.ttit.terrainshop.waitbar.WaitProgress;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static com.shuvo.ttit.terrainshop.connection.OracleConnection.createConnection;
import static com.shuvo.ttit.terrainshop.firstpage.ChoicePage.loginDone;

public class Login extends AppCompatActivity {

    TextView goToSignUp;
    TextInputEditText mob;
    TextInputEditText pass;
    TextView forgotPassword;

    TextView login_failed;

    Button login;

    CheckBox checkBox;

    String mobile = "";
    String password = "";

    WaitProgress waitProgress = new WaitProgress();
    private String message = null;
    private Boolean conn = false;
    private Boolean infoConnected = false;
    private Boolean connected = false;

    private Boolean getConn = false;
    private Boolean getConnected = false;

    private Connection connection;
    private AsyncTask mTask;

    public static final String MyPREFERENCES = "UserPassESHOP" ;
    public static final String user_emp_code = "nameKey";
    public static final String user_password = "passKey";
    public static final String checked = "trueFalse";

    public static final String LOGIN_ACTIVITY_FILE = "LOGIN_ACTIVITY_FILE_ESHOP";

    public static final String AD_ID = "AD_ID";
    public static final String AD_CODE = "AD_CODE";
    public static final String AD_NAME = "AD_NAME";
    public static final String AD_ADDRESS = "AD_ADDRESS";
    public static final String AD_PHONE = "AD_PHONE";
    public static final String AD_EMAIL = "AD_EMAIL";
    public static final String AD_DIV_ID = "AD_DIV_ID";
    public static final String AD_DIST_ID = "AD_DIST_ID";
    public static final String AD_DD_ID = "AD_DD_ID";
    public static final String AD_DIV_NAME = "AD_DIV_NAME";
    public static final String AD_DIST_NAME = "AD_DIST_NAME";
    public static final String AD_DD_NAME = "AD_DD_NAME";
    public static final String LOGIN_TF = "TRUE_FALSE";

    SharedPreferences sharedpreferences;

    SharedPreferences sharedLogin;

    String getUserName = "";
    String getPassword = "";
    boolean getChecked = false;

    public static ArrayList<UserInfoList> userInfoLists = new ArrayList<>();;

    LinearLayout fullLayout;
    CircularProgressIndicator circularProgressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userInfoLists = new ArrayList<>();

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        fullLayout = findViewById(R.id.login_full_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_log_in);
        circularProgressIndicator.setVisibility(View.GONE);

        mob = findViewById(R.id.mobile_number_given_log_in);
        pass = findViewById(R.id.password_given_log_in);
        checkBox = findViewById(R.id.remember_checkbox);

        login_failed = findViewById(R.id.email_pass_miss);
        goToSignUp = findViewById(R.id.sign_up_text);

        login = findViewById(R.id.log_in_button);

        forgotPassword = findViewById(R.id.forgot_password);

        sharedLogin = getSharedPreferences(LOGIN_ACTIVITY_FILE,MODE_PRIVATE);

        sharedpreferences = getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);
        getUserName = sharedpreferences.getString(user_emp_code,null);
        getPassword = sharedpreferences.getString(user_password,null);
        getChecked = sharedpreferences.getBoolean(checked,false);

        if (getUserName != null) {
            mob.setText(getUserName);
        }
        if (getPassword != null) {
            pass.setText(getPassword);
        }
        checkBox.setChecked(getChecked);


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
                mobile = mob.getText().toString();
                password = pass.getText().toString();

                if (!mobile.isEmpty() && !password.isEmpty()) {


                    mTask = new CheckLogin().execute();


                } else {
                    Toast.makeText(getApplicationContext(), "Please Give Mobile number and Password", Toast.LENGTH_SHORT).show();
                }

            }
        });

        goToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        closeKeyBoard();
        mob.clearFocus();
        pass.clearFocus();
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(mob.getWindowToken(), 0);


    }

    @Override
    public boolean onTouchEvent (MotionEvent event){
        closeKeyBoard();
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
//        boolean wasCalledFromBackgroundThread = (Thread.currentThread().getId() != 1);
//        if (wasCalledFromBackgroundThread) {
//            System.out.println(Thread.currentThread().getId());
//        }
//        else {
//            finish();
//        }
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

    @SuppressLint("StaticFieldLeak")
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
                    message = "Internet Connected";
                }

            } else {
                conn = false;
                message = "Not Connected";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            circularProgressIndicator.setVisibility(View.GONE);
            fullLayout.setVisibility(View.VISIBLE);
            if (conn) {

                infoConnected = userInfoLists.size() != 0;

                if (infoConnected) {

                    if (checkBox.isChecked()) {
                        System.out.println("Remembered");
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.remove(user_emp_code);
                        editor.remove(user_password);
                        editor.remove(checked);
                        editor.putString(user_emp_code,mobile);
                        editor.putString(user_password,password);
                        editor.putBoolean(checked,true);
                        editor.apply();
                        editor.commit();

                        SharedPreferences.Editor editor1 = sharedLogin.edit();
                        editor1.remove(AD_ID);
                        editor1.remove(AD_CODE);
                        editor1.remove(AD_NAME);
                        editor1.remove(AD_ADDRESS);
                        editor1.remove(AD_PHONE);
                        editor1.remove(AD_EMAIL);
                        editor1.remove(AD_DIV_ID);
                        editor1.remove(AD_DIST_ID);
                        editor1.remove(AD_DD_ID);
                        editor1.remove(AD_DIV_NAME);
                        editor1.remove(AD_DIST_NAME);
                        editor1.remove(AD_DD_NAME);
                        editor1.remove(LOGIN_TF);

                        editor1.putString(AD_ID, userInfoLists.get(0).getAd_id());
                        editor1.putString(AD_CODE, userInfoLists.get(0).getAd_code());
                        editor1.putString(AD_NAME, userInfoLists.get(0).getAd_name());
                        editor1.putString(AD_ADDRESS, userInfoLists.get(0).getAd_address());
                        editor1.putString(AD_PHONE, userInfoLists.get(0).getAd_phone());
                        editor1.putString(AD_EMAIL, userInfoLists.get(0).getAd_email());
                        editor1.putString(AD_DIV_ID,userInfoLists.get(0).getAd_div_id());
                        editor1.putString(AD_DIST_ID,userInfoLists.get(0).getAd_dist_id());
                        editor1.putString(AD_DD_ID,userInfoLists.get(0).getAd_dd_id());
                        editor1.putString(AD_DIV_NAME,userInfoLists.get(0).getAd_div_name());
                        editor1.putString(AD_DIST_NAME,userInfoLists.get(0).getAd_dist_name());
                        editor1.putString(AD_DD_NAME,userInfoLists.get(0).getAd_thana_name());
                        editor1.putBoolean(LOGIN_TF,true);

                        editor1.apply();
                        editor1.commit();
                    }
                    else {
                        System.out.println("Not Remembered");
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.remove(user_emp_code);
                        editor.remove(user_password);
                        editor.remove(checked);

                        editor.apply();
                        editor.commit();

                        SharedPreferences.Editor editor1 = sharedLogin.edit();
                        editor1.remove(AD_ID);
                        editor1.remove(AD_CODE);
                        editor1.remove(AD_NAME);
                        editor1.remove(AD_ADDRESS);
                        editor1.remove(AD_PHONE);
                        editor1.remove(AD_EMAIL);
                        editor1.remove(AD_DIV_ID);
                        editor1.remove(AD_DIST_ID);
                        editor1.remove(AD_DD_ID);
                        editor1.remove(AD_DIV_NAME);
                        editor1.remove(AD_DIST_NAME);
                        editor1.remove(AD_DD_NAME);
                        editor1.remove(LOGIN_TF);

                        editor1.apply();
                        editor1.commit();
                    }

//                        user.setText("");
//                        pass.setText("");
//                        checkBox.setChecked(false);

                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(Login.this, HomePage.class);
//                    startActivity(intent);
                    loginDone = true;
                    finish();

                }
                else {
                    login_failed.setVisibility(View.VISIBLE);
                }

                conn = false;
                connected = false;
                infoConnected = false;


            } else {
                //Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(Login.this)
                        .setMessage("Slow Internet or Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mTask = new CheckLogin().execute();
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public void LoginQuery () {

        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            userInfoLists = new ArrayList<>();

            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("select ad_id, ad_code, ad_name, ad_address, ad_phone, ad_email, ad_div_id, ad_dist_id, ad_dd_id, \n" +
                    "(Select div_name from division where division.div_id = acc_dtl.ad_div_id) as div_name,\n" +
                    "(Select dist_name from district where district.dist_id = acc_dtl.ad_dist_id) as dist_name,\n" +
                    "(Select DD_THANA_NAME from district_dtl where district_dtl.dd_id = acc_dtl.ad_dd_id) as thana_name\n" +
                    "from acc_dtl\n" +
                    "where ad_phone = '"+mobile+"'\n" +
                    "and ad_password = '"+password+"'");

            while (rs.next()) {
                userInfoLists.add(new UserInfoList(rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getString(6),
                        rs.getString(7),rs.getString(8),rs.getString(9),
                        rs.getString(10),rs.getString(11),rs.getString(12)));
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