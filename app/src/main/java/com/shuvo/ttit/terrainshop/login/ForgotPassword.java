package com.shuvo.ttit.terrainshop.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
import com.shuvo.ttit.terrainshop.profile.UpdateProfile;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static com.shuvo.ttit.terrainshop.connection.OracleConnection.createConnection;
import static com.shuvo.ttit.terrainshop.login.Login.userInfoLists;

public class ForgotPassword extends AppCompatActivity {

    ImageView close;
    RelativeLayout fullLayout;
    CircularProgressIndicator circularProgressIndicator;

    TextInputLayout textInputLayout;
    TextInputEditText editText;
    MaterialButton request;

    String mail = "";

    Connection connection;
    private Boolean conn = false;
    private Boolean connected = false;
    private Boolean mailOk = false;

    int count = 0;

    private AsyncTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.white));

        close = findViewById(R.id.close_logo_forgot_password);
        fullLayout = findViewById(R.id.forgot_password_full_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_forgot_password);
        circularProgressIndicator.setVisibility(View.GONE);

        textInputLayout = findViewById(R.id.update_text_layout_forgot_password);
        editText = findViewById(R.id.update_text_given_forgot_password);
        request = findViewById(R.id.request_button_forgot_password);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH ||
                        i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_NEXT || keyEvent != null && keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        keyEvent.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                    if (keyEvent == null || !keyEvent.isShiftPressed()) {
                        // the user is done typing.
                        Log.i("Let see", "Come here");
                        editText.clearFocus();
                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(textView.getWindowToken(), 0);

                        return false; // consume.
                    }
                }
                return false;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                textInputLayout.setHelperText("");
                if (editable.toString().isEmpty()) {
                    textInputLayout.setHint("Enter Email Address");
                }
                else {
                    textInputLayout.setHint("Email Address");
                }
            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closeKeyBoard();
                mail = editText.getText().toString();
                if (mail != null) {
                    textInputLayout.setHelperText("");
                    if (!mail.isEmpty()) {
                        textInputLayout.setHelperText("");
                        if (mail.contains("@") && mail.contains(".")) {
                            textInputLayout.setHelperText("");
                            mTask = new RecoveryCheck().execute();

                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Please give proper email address",Toast.LENGTH_SHORT).show();
                            textInputLayout.setHelperText("Please give proper email address");
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Please give email address before sending request",Toast.LENGTH_SHORT).show();
                        textInputLayout.setHelperText("Please give email address before sending request");
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please give email address before sending request",Toast.LENGTH_SHORT).show();
                    textInputLayout.setHelperText("Please give email address before sending request");
                }
            }
        });


    }

    private void closeKeyBoard () {
        View view = this.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
//        boolean wasCalledFromBackgroundThread = (Thread.currentThread().getId() != 1);
//        System.out.println(Thread.currentThread().getName());
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

//    @Override
//    protected void onStop() {
//        super.onStop();
//        System.out.println(mTask.getStatus().toString());
//
//        mTask.cancel(true);
//    }

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
    public class RecoveryCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            circularProgressIndicator.setVisibility(View.VISIBLE);
            fullLayout.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                CheckMailQuery();
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

                if (mailOk) {
                    AlertDialog dialog = new AlertDialog.Builder(ForgotPassword.this)
                            .setMessage("Please check your inbox. An email has been sent to the given email address.")
                            .setPositiveButton("OK",null)
                            .show();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                    Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                }
                else {
                    AlertDialog dialog = new AlertDialog.Builder(ForgotPassword.this)
                            .setMessage("Your given email address is not associated with any account. Please provide correct email address.")
                            .setPositiveButton("OK",null)
                            .show();
//                    dialog.setCanceledOnTouchOutside(false);
//                    dialog.setCancelable(false);
                    Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    textInputLayout.setHelperText("Please provide correct email address.");
                }

//                System.out.println(mTask.getStatus().toString());

            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(ForgotPassword.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mTask = new RecoveryCheck().execute();
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public void CheckMailQuery () {

        try {
            this.connection = createConnection();

            Statement stmt = connection.createStatement();

            ResultSet resultSet = stmt.executeQuery("Select count(*) from acc_dtl where AD_EMAIL = '"+mail+"'");

            while (resultSet.next()) {
                count = resultSet.getInt(1);
            }

            mailOk = count != 0;

            if (mailOk) {
                CallableStatement callableStatement1 = connection.prepareCall("{call ACC_REC_INFO_MAIL(?)}");
                callableStatement1.setString(1,(mail));
                callableStatement1.execute();

                callableStatement1.close();
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