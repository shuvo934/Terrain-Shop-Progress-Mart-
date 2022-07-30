package com.shuvo.ttit.terrainshop.signup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rosemaryapp.amazingspinner.AmazingSpinner;
import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.checkout.CheckOut;
import com.shuvo.ttit.terrainshop.checkout.lists.UpazilaList;
import com.shuvo.ttit.terrainshop.login.list.UserInfoList;
import com.shuvo.ttit.terrainshop.signup.lists.DivisionList;
import com.shuvo.ttit.terrainshop.signup.lists.ThanaList;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static com.shuvo.ttit.terrainshop.connection.OracleConnection.createConnection;

public class SignUp extends AppCompatActivity {

    TextInputLayout fullNameLay, addressLay, mobNumblay, emailLay, passwordLay;
    TextInputEditText fullName, address, mobNUmb, email, password;
    AmazingSpinner division;
    TextView divisionLay;

    ArrayList<DivisionList> divisionLists;
    String div_id = "";

    AmazingSpinner thana;
    TextInputLayout thanaLay;
    TextView nothana;

    String dist_id = "";
    String dd_id = "";

    ArrayList<ThanaList> thanaLists;

    Button signUp;

    TextView goToLogin;

    CircularProgressIndicator circularProgressIndicator;
    LinearLayout fullLayout;

    String name = "";
    String addrr = "";
    String numb= "";
    String mail = "";
    String pass = "";

    String ad_id = "";
    String ad_code_id = "";
    String ad_al2_id = "";
    String al2_code = "";
    String ad_code = "";

    private Boolean conn = false;
    private Boolean connected = false;

    private boolean numberCheck = false;

    Connection connection;
    private AsyncTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fullNameLay = findViewById(R.id.user_name_sign_up);
        addressLay = findViewById(R.id.address_sign_up);
        mobNumblay = findViewById(R.id.mobile_number_sign_up);
        emailLay = findViewById(R.id.mail_sign_up);
        passwordLay = findViewById(R.id.password_sign_up);

        fullName = findViewById(R.id.user_name_given_sign_up);
        address = findViewById(R.id.address_given_sign_up);
        mobNUmb = findViewById(R.id.mobile_number_given_sign_up);
        email = findViewById(R.id.mail_given_sign_up);
        password = findViewById(R.id.password_given);

        signUp = findViewById(R.id.sign_up_button);

        fullLayout = findViewById(R.id.sign_up_full_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_sign_up);
        circularProgressIndicator.setVisibility(View.GONE);

        goToLogin = findViewById(R.id.login_text);

        division = findViewById(R.id.division_spinner_sign_up);
        divisionLay = findViewById(R.id.spinner_layout_division_sign_up);
        divisionLay.setVisibility(View.GONE);

        thana = findViewById(R.id.location_spinner_sign_up);
        thanaLay = findViewById(R.id.spinner_layout_location_sign_up);
        thanaLay.setEnabled(false);
        nothana = findViewById(R.id.no_location_sign_up);
        nothana.setVisibility(View.GONE);

        divisionLists = new ArrayList<>();
        thanaLists = new ArrayList<>();

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //for Email validation check
        email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        Log.i("Let see", "Come here");
                        if (!email.getText().toString().isEmpty()) {
                            if(!email.getText().toString().contains("@")) {
                                emailLay.setHelperText("Invalid Email Address");
                            } else {
                                emailLay.setHelperText("");
                            }
                        }
                        else {
                            emailLay.setHelperText("Email address must be given");
                            emailLay.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
                        }

                        return false; // consume.
                    }
                }
                return false;
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    if (!email.getText().toString().isEmpty()) {
                        if(!email.getText().toString().contains("@")) {
                            emailLay.setHelperText("Invalid Email Address");
                        } else {
                            emailLay.setHelperText("");
                        }
                    }
                    else {
                        emailLay.setHelperText("Email address must be given");
                        emailLay.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
                    }
                }
            }
        });

        // checking user name is given or not
        fullName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        Log.i("Let see", "Come here");
                        if(!fullName.getText().toString().isEmpty()) {
                            fullNameLay.setHelperText("");
                        } else {
                            fullNameLay.setHelperText("Name must be given");
                            fullNameLay.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
                        }

                        return false; // consume.
                    }
                }
                return false;
            }
        });

        fullName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    if(!fullName.getText().toString().isEmpty()) {
                        fullNameLay.setHelperText("");
                    } else {
                        fullNameLay.setHelperText("Name must be given");
                        fullNameLay.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
                    }
                }
            }
        });

        // checking password is given or not
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        Log.i("Let see", "Come here");
                        if(!password.getText().toString().isEmpty()) {
                            passwordLay.setHelperText("");
                        } else {
                            passwordLay.setHelperText("Password must be given");
                            passwordLay.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
                        }

                        password.clearFocus();
                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        closeKeyBoard();

                        return false; // consume.
                    }
                }
                return false;
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    if(!password.getText().toString().isEmpty()) {
                        passwordLay.setHelperText("");
                    } else {
                        passwordLay.setHelperText("Password must be given");
                        passwordLay.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
                    }
                }
            }
        });

        // checking phone is given or not
        mobNUmb.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        Log.i("Let see", "Come here");
                        if(!mobNUmb.getText().toString().isEmpty()) {
                            if(mobNUmb.getText().toString().length() < 11) {
                                mobNumblay.setHelperText("Phone Number is nor correct");
                            } else {
                                mobNumblay.setHelperText("");
                            }
                        } else {
                            mobNumblay.setHelperText("Phone number must be given");
                            mobNumblay.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
                        }

                        return false; // consume.
                    }
                }
                return false;
            }
        });

        mobNUmb.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    if(!mobNUmb.getText().toString().isEmpty()) {
                        if(mobNUmb.getText().toString().length() < 11) {
                            mobNumblay.setHelperText("Phone Number is nor correct");
                        } else {
                            mobNumblay.setHelperText("");
                        }
                    } else {
                        mobNumblay.setHelperText("Phone number must be given");
                        mobNumblay.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
                    }
                }
            }
        });

        // checking address is given or not
        address.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        Log.i("Let see", "Come here");
                        if(!address.getText().toString().isEmpty()) {
                            addressLay.setHelperText("");
                        } else {
                            addressLay.setHelperText("Address must be given");
                            addressLay.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
                        }

                        return false; // consume.
                    }
                }
                return false;
            }
        });

        address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    if(!address.getText().toString().isEmpty()) {
                        addressLay.setHelperText("");
                    } else {
                        addressLay.setHelperText("Address must be given");
                        addressLay.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
                    }
                }
            }
        });

        division.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                thanaLay.setEnabled(false);
                thana.setText("");
                div_id = "";
                dist_id = "";
                dd_id = "";
                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < divisionLists.size(); j++) {
                    if (name.equals(divisionLists.get(j).getDivName())) {
                        div_id = (divisionLists.get(j).getDivId());
                    }
                }
                System.out.println(name);
                System.out.println(div_id);
                nothana.setVisibility(View.GONE);
                if (div_id.isEmpty()) {
                    divisionLay.setText("Please Select Division");
                    divisionLay.setVisibility(View.VISIBLE);
                }
                else {
                    divisionLay.setText("");
                    divisionLay.setVisibility(View.GONE);
                    mTask = new ThanaCheck().execute();
                }


            }
        });

        thana.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                dd_id = "";
                dist_id = "";
                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < thanaLists.size(); j++) {
                    if (name.equals(thanaLists.get(j).getThanaName())) {
                        dd_id = (thanaLists.get(j).getDdId());
                        dist_id = thanaLists.get(j).getDist_id();
                    }
                }
                System.out.println(dd_id);
                nothana.setVisibility(View.GONE);

            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyBoard();
                name = fullName.getText().toString();
                mail = email.getText().toString();
                pass = password.getText().toString();
                numb = mobNUmb.getText().toString();
                addrr = address.getText().toString();

                if (name.isEmpty() || mail.isEmpty() || pass.isEmpty() || numb.isEmpty() || addrr.isEmpty() || div_id.isEmpty() || dist_id.isEmpty() || dd_id.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Fill Up The Required Field",Toast.LENGTH_SHORT).show();
                    if (!div_id.isEmpty() && dd_id.isEmpty()) {
                        nothana.setText("Please Select Location");
                        nothana.setVisibility(View.VISIBLE);
                    }
                }
                else if (numb.length() < 11) {
                    Toast.makeText(getApplicationContext(), "Phone Number is not correct",Toast.LENGTH_SHORT).show();
                }
                else if (!mail.contains("@")) {
                    Toast.makeText(getApplicationContext(), "Please Give Proper Email Address",Toast.LENGTH_SHORT).show();
                }
                else {
                    System.out.println(pass);
                    mTask = new Check().execute();
                }
            }
        });

        mTask = new CheckDivision().execute();

    }

    @Override
    public boolean onTouchEvent (MotionEvent event){
        closeKeyBoard();
        return super.onTouchEvent(event);
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
        View view = this.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else {
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
    public class CheckDivision extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            circularProgressIndicator.setVisibility(View.VISIBLE);
            fullLayout.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                DivisionQuery();
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

                ArrayList<String> type2 = new ArrayList<>();
                for(int i = 0; i < divisionLists.size(); i++) {
                    type2.add(divisionLists.get(i).getDivName());
                }
                ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type2);

                division.setAdapter(arrayAdapter2);



            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection or Slow Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(SignUp.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mTask = new CheckDivision().execute();
                        dialog.dismiss();
                    }
                });
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        finish();
                    }
                });
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class ThanaCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            circularProgressIndicator.setVisibility(View.VISIBLE);
            fullLayout.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                UpazilaQuery();
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

                thanaLay.setEnabled(true);

                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < thanaLists.size(); i++) {
                    type.add(thanaLists.get(i).getThanaName());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                thana.setAdapter(arrayAdapter);

                conn = false;
                connected = false;

            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection or Slow Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(SignUp.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mTask = new ThanaCheck().execute();
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class Check extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            circularProgressIndicator.setVisibility(View.VISIBLE);
            fullLayout.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                SignUpQuery();
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

                if (!numberCheck) {
                    Toast.makeText(getApplicationContext(), "This Phone Number is already Registered", Toast.LENGTH_SHORT).show();
                    mobNumblay.setHelperText("This Phone Number is already Registered");
                } else {
                    Toast.makeText(getApplicationContext(), "SignUp successful, Please login to continue", Toast.LENGTH_SHORT).show();
                    finish();
                }



            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(SignUp.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mTask = new Check().execute();
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public void SignUpQuery () {

        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            Statement stmt = connection.createStatement();

            String checkNumb = "";

            ResultSet rs = stmt.executeQuery("Select ad_code from acc_dtl\n" +
                    "where ad_phone = '"+numb+"'");

            while (rs.next()) {
                checkNumb = rs.getString(1);
            }
            rs.close();

            numberCheck = checkNumb.isEmpty();

            if (numberCheck) {

                ResultSet rs1 = stmt.executeQuery("SELECT NVL(MAX(AD_ID),1)+1 FROM ACC_DTL");

                while (rs1.next()) {
                    ad_id = rs1.getString(1);
                }
                rs1.close();

                ResultSet rs2 = stmt.executeQuery("SELECT NVL(MAX(AD_CODE_ID),0)+1 \n" +
                        "\tFROM ACC_DTL\n" +
                        "\tWHERE ACC_DTL.AD_FLAG=6");

                while (rs2.next()) {
                    ad_code_id = rs2.getString(1);
                }
                rs2.close();

                ResultSet rs3 = stmt.executeQuery("SELECT AL2_ID,AL2_CODE --INTO B,A\n" +
                        "\tFROM ACC_LEVEL2\n" +
                        "\tWHERE ACC_LEVEL2.AL2_FLAG=6");

                while (rs3.next()) {
                    ad_al2_id = rs3.getString(1);
                    al2_code = rs3.getString(2);
                }

                rs3.close();

                ResultSet rs4 = stmt.executeQuery("SELECT '"+al2_code+"' ||LTRIM(TO_CHAR("+ad_code_id+",'000000')) FROM DUAL");

                while (rs4.next()) {
                    ad_code = rs4.getString(1);
                }
                rs4.close();

                String ad_flag = "6";

                stmt.executeUpdate("Insert into ACC_DTL(AD_ID, AD_CODE_ID, AD_AL2_ID, AD_CODE, AD_NAME, AD_EMAIL, AD_PASSWORD, AD_VERIFY_CODE, AD_ADDRESS, AD_PHONE, AD_WEB_ADDRESS, AD_FLAG, AD_DIV_ID,AD_DIST_ID,AD_DD_ID)\n" +
                        "values ("+ad_id+", "+ad_code_id+", "+ad_al2_id+",'"+ad_code+"', '"+name+"', '"+mail+"','"+pass+"','"+pass+"', '"+addrr+"','"+numb+"', NULL,"+ad_flag+", "+div_id+","+dist_id+","+dd_id+")");

                String aad_id = "";

                ResultSet resultSet = stmt.executeQuery("SELECT NVL(MAX(AAD_ID),1)+1 FROM ACC_ATTENTION_DTL");

                while (resultSet.next()) {
                    aad_id = resultSet.getString(1);
                }
                resultSet.close();

                stmt.executeUpdate("INSERT INTO ACC_ATTENTION_DTL(AAD_ID, AAD_CONTACT_PERSON, AAD_AD_ID, AAD_EMAIL, AAD_CONTACT_NO, AAD_ACTIVE_CON_FLAG, AAD_ECOMMERCE_FLAG)\n" +
                        "VALUES("+aad_id+", '"+name+"', "+ad_id+", '"+mail+"', '"+numb+"', 1,1)");

            }

            connected = true;

            connection.close();

        } catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

    public void DivisionQuery () {

        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            Statement stmt = connection.createStatement();

            divisionLists = new ArrayList<>();

            ResultSet rs1 = stmt.executeQuery("SELECT DIVISION.DIV_ID P_DIV_ID, DIVISION.DIV_NAME FROM DIVISION WHERE DIV_ACTIVE_FLAG=1 ORDER BY DIVISION.DIV_ID");

            while (rs1.next()) {
                divisionLists.add(new DivisionList(rs1.getString(1),rs1.getString(2)));
            }

            rs1.close();


            connected = true;

            connection.close();

        } catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

    public void UpazilaQuery () {

        try {
            this.connection = createConnection();

            thanaLists = new ArrayList<>();

            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("Select DD_ID, DD_THANA_NAME, DD_DIST_ID from district_dtl where DD_ACTIVE_FLAG = 1 \n" +
                    "AND DD_DIST_ID IN (SELECT DIST_ID FROM DISTRICT WHERE DIST_DIV_ID = "+div_id+" AND DIST_ACTIVE_FLAG = 1) order by dd_id");

            while (rs.next()) {
                thanaLists.add(new ThanaList(rs.getString(1),rs.getString(2),rs.getString(3)));
            }

            rs.close();

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