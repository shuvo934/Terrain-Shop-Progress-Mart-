package com.shuvo.ttit.terrainshop.profile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;
import com.rosemaryapp.amazingspinner.AmazingSpinner;
import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.checkout.CheckOut;
import com.shuvo.ttit.terrainshop.checkout.lists.DistrictList;
import com.shuvo.ttit.terrainshop.checkout.lists.TimeSlotList;
import com.shuvo.ttit.terrainshop.checkout.lists.UpazilaList;
import com.shuvo.ttit.terrainshop.signup.SignUp;
import com.shuvo.ttit.terrainshop.signup.lists.DivisionList;
import com.shuvo.ttit.terrainshop.signup.lists.ThanaList;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static com.shuvo.ttit.terrainshop.connection.OracleConnection.createConnection;
import static com.shuvo.ttit.terrainshop.login.Login.userInfoLists;

public class UpdateLocation extends AppCompatActivity {

    ImageView close;

    RelativeLayout fullLayout;
    CircularProgressIndicator circularProgressIndicator;

    MaterialButton save;

    AmazingSpinner division;
    TextView noDivision;

    ArrayList<DivisionList> divisionLists;
    String div_id = "";

    AmazingSpinner thana;
    TextInputLayout thanaLay;
    TextView nothana;

    String dist_id = "";
    String dd_id = "";

    String user_div_name = "";
    String user_dist_name = "";
    String user_dd_name = "";

    ArrayList<ThanaList> thanaLists;

    String ad_id = "";

    Connection connection;
    private Boolean conn = false;
    private Boolean connected = false;

    public static final String LOGIN_ACTIVITY_FILE = "LOGIN_ACTIVITY_FILE_ESHOP";

    public static final String AD_DIV_ID = "AD_DIV_ID";
    public static final String AD_DIST_ID = "AD_DIST_ID";
    public static final String AD_DD_ID = "AD_DD_ID";
    public static final String AD_DIV_NAME = "AD_DIV_NAME";
    public static final String AD_DIST_NAME = "AD_DIST_NAME";
    public static final String AD_DD_NAME = "AD_DD_NAME";

    SharedPreferences sharedLogin;
    private AsyncTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_location);

        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.back_color));

        sharedLogin = getSharedPreferences(LOGIN_ACTIVITY_FILE,MODE_PRIVATE);

        close = findViewById(R.id.close_logo_update_location);
        fullLayout = findViewById(R.id.update_location_full_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_update_location);
        circularProgressIndicator.setVisibility(View.GONE);
        save = findViewById(R.id.save_button_update_location);

        division = findViewById(R.id.division_spinner_update_location);
        noDivision = findViewById(R.id.spinner_layout_division_update_location);
        noDivision.setVisibility(View.GONE);

        thana = findViewById(R.id.location_spinner_update_location);
        thanaLay = findViewById(R.id.spinner_layout_location_update_location);
        thanaLay.setEnabled(false);
        nothana = findViewById(R.id.no_location_update_location);
        nothana.setVisibility(View.GONE);

        divisionLists = new ArrayList<>();
        thanaLists = new ArrayList<>();

        ad_id = userInfoLists.get(0).getAd_id();

        div_id = userInfoLists.get(0).getAd_div_id();
        dist_id = userInfoLists.get(0).getAd_dist_id();
        dd_id = userInfoLists.get(0).getAd_dd_id();

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
                    noDivision.setText("Please Select Division");
                    noDivision.setVisibility(View.VISIBLE);
                }
                else {
                    noDivision.setText("");
                    noDivision.setVisibility(View.GONE);
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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (div_id != null) {
                    if (!div_id.isEmpty()) {
                        if (dd_id != null) {
                            if (!dd_id.isEmpty()) {
                                if (dist_id != null) {
                                    if (!dist_id.isEmpty()) {
                                        mTask = new UpdateCheck().execute();
                                    }
                                    else {
                                        nothana.setText("Please Select Location Again");
                                        nothana.setVisibility(View.VISIBLE);
                                    }
                                }
                                else {
                                    nothana.setText("Please Select Location Again");
                                    nothana.setVisibility(View.VISIBLE);
                                }
                            }
                            else {
                                nothana.setText("Please Select Location");
                                nothana.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(),"Please give information to update",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            nothana.setText("Please Select Location");
                            nothana.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(),"Please give information to update",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        noDivision.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(),"Please give information to update",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    noDivision.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"Please give information to update",Toast.LENGTH_SHORT).show();
                }
            }
        });


        mTask = new Check().execute();
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

                Query();
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

                conn = false;
                connected = false;

                if (div_id != null) {
                    if (!div_id.isEmpty()) {
                        String divName = "";
                        for (int i = 0; i < divisionLists.size(); i++) {
                            if (div_id.equals(divisionLists.get(i).getDivId())) {
                                divName = divisionLists.get(i).getDivName();
                            }
                        }
                        division.setText(divName);

                        thanaLay.setEnabled(true);

                        if (dd_id != null) {
                            if (!dd_id.isEmpty()) {
                                String upazilaName = "";
                                for (int i = 0; i < thanaLists.size(); i++) {
                                    if (dd_id.equals(thanaLists.get(i).getDdId())) {
                                        upazilaName = thanaLists.get(i).getThanaName();
                                    }
                                }
                                thana.setText(upazilaName);
                            }
                        }

                        ArrayList<String> type = new ArrayList<>();
                        for(int i = 0; i < thanaLists.size(); i++) {
                            type.add(thanaLists.get(i).getThanaName());
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                        thana.setAdapter(arrayAdapter);

                    }
                    else {
                        thanaLay.setEnabled(false);
                        if (dd_id != null) {
                            if (!dd_id.isEmpty()) {
                                String upazilaName = "";
                                for (int i = 0; i < thanaLists.size(); i++) {
                                    if (dd_id.equals(thanaLists.get(i).getDdId())) {
                                        upazilaName = thanaLists.get(i).getThanaName();
                                    }
                                }
                                thana.setText(upazilaName);
                            }
                        }
                        ArrayList<String> type = new ArrayList<>();
                        for(int i = 0; i < thanaLists.size(); i++) {
                            type.add(thanaLists.get(i).getThanaName());
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                        thana.setAdapter(arrayAdapter);
                    }
                }
                else {
                    thanaLay.setEnabled(false);
                    if (dd_id != null) {
                        if (!dd_id.isEmpty()) {
                            String upazilaName = "";
                            for (int i = 0; i < thanaLists.size(); i++) {
                                if (dd_id.equals(thanaLists.get(i).getDdId())) {
                                    upazilaName = thanaLists.get(i).getThanaName();
                                }
                            }
                            thana.setText(upazilaName);
                        }
                    }
                    ArrayList<String> type = new ArrayList<>();
                    for(int i = 0; i < thanaLists.size(); i++) {
                        type.add(thanaLists.get(i).getThanaName());
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                    thana.setAdapter(arrayAdapter);
                }

                ArrayList<String> type2 = new ArrayList<>();
                for(int i = 0; i < divisionLists.size(); i++) {
                    type2.add(divisionLists.get(i).getDivName());
                }
                ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type2);

                division.setAdapter(arrayAdapter2);


            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection or Slow Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(UpdateLocation.this)
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

                        mTask = new Check().execute();
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
                AlertDialog dialog = new AlertDialog.Builder(UpdateLocation.this)
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

                userInfoLists.get(0).setAd_div_id(div_id);
                userInfoLists.get(0).setAd_dist_id(dist_id);
                userInfoLists.get(0).setAd_dd_id(dd_id);
                userInfoLists.get(0).setAd_div_name(user_div_name);
                userInfoLists.get(0).setAd_dist_name(user_dist_name);
                userInfoLists.get(0).setAd_thana_name(user_dd_name);

                SharedPreferences.Editor editor1 = sharedLogin.edit();

                editor1.remove(AD_DIV_ID);
                editor1.remove(AD_DIST_ID);
                editor1.remove(AD_DD_ID);
                editor1.remove(AD_DIV_NAME);
                editor1.remove(AD_DIST_NAME);
                editor1.remove(AD_DD_NAME);


                editor1.putString(AD_DIV_ID,userInfoLists.get(0).getAd_div_id());
                editor1.putString(AD_DIST_ID,userInfoLists.get(0).getAd_dist_id());
                editor1.putString(AD_DD_ID,userInfoLists.get(0).getAd_dd_id());
                editor1.putString(AD_DIV_NAME,userInfoLists.get(0).getAd_div_name());
                editor1.putString(AD_DIST_NAME,userInfoLists.get(0).getAd_dist_name());
                editor1.putString(AD_DD_NAME,userInfoLists.get(0).getAd_thana_name());
                editor1.apply();
                editor1.commit();

                Toast.makeText(getApplicationContext(), "Successfully Updated",Toast.LENGTH_SHORT).show();
                finish();

            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(UpdateLocation.this)
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

    public void Query () {

        try {
            this.connection = createConnection();


            divisionLists = new ArrayList<>();
            thanaLists = new ArrayList<>();

            Statement stmt = connection.createStatement();

            ResultSet rs1 = stmt.executeQuery("SELECT DIVISION.DIV_ID P_DIV_ID, DIVISION.DIV_NAME FROM DIVISION WHERE DIV_ACTIVE_FLAG=1 ORDER BY DIVISION.DIV_ID");

            while (rs1.next()) {
                divisionLists.add(new DivisionList(rs1.getString(1),rs1.getString(2)));
            }

            rs1.close();

            if (div_id != null) {
                ResultSet rs;
                if (!div_id.isEmpty()) {
                    rs = stmt.executeQuery("Select DD_ID, DD_THANA_NAME, DD_DIST_ID from district_dtl where DD_ACTIVE_FLAG = 1 \n" +
                            "AND DD_DIST_ID IN (SELECT DIST_ID FROM DISTRICT WHERE DIST_DIV_ID = " + div_id + " AND DIST_ACTIVE_FLAG = 1) order by dd_id");

                }
                else {
                    rs = stmt.executeQuery("Select DD_ID, DD_THANA_NAME, DD_DIST_ID from district_dtl where DD_ACTIVE_FLAG = 1");

                }
                while (rs.next()) {
                    thanaLists.add(new ThanaList(rs.getString(1),rs.getString(2),rs.getString(3)));
                }
                rs.close();
            }
            else {
                ResultSet rs = stmt.executeQuery("Select DD_ID, DD_THANA_NAME, DD_DIST_ID from district_dtl where DD_ACTIVE_FLAG = 1");

                while (rs.next()) {
                    thanaLists.add(new ThanaList(rs.getString(1),rs.getString(2),rs.getString(3)));
                }
                rs.close();
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

    public void UpdateQuery () {

        try {
            this.connection = createConnection();

            Statement stmt = connection.createStatement();

            user_div_name = "";
            user_dist_name = "";
            user_dd_name = "";

            stmt.executeUpdate("UPDATE ACC_DTL SET AD_DIV_ID = "+div_id+", AD_DIST_ID = "+dist_id+", AD_DD_ID = "+dd_id+" WHERE AD_ID = "+ ad_id +"");

            ResultSet resultSet = stmt.executeQuery("Select div_name from division where div_id = "+div_id+"");

            while (resultSet.next()) {
                user_div_name = resultSet.getString(1);
            }
            resultSet.close();

            ResultSet resultSet1 = stmt.executeQuery("Select dist_name from district where dist_id = "+dist_id+"");

            while (resultSet1.next()) {
                user_dist_name = resultSet1.getString(1);
            }
            resultSet1.close();

            ResultSet resultSet2 = stmt.executeQuery("Select DD_THANA_NAME from district_dtl where dd_id = "+dd_id+"");

            while (resultSet2.next()) {
                user_dd_name = resultSet2.getString(1);
            }
            resultSet2.close();

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