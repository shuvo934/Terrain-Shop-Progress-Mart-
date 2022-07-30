package com.shuvo.ttit.terrainshop.checkout.vouchers;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
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
import com.shuvo.ttit.terrainshop.checkout.vouchers.adapters.VoucherAdapter;
import com.shuvo.ttit.terrainshop.checkout.vouchers.lists.VoucherDetailsList;
import com.shuvo.ttit.terrainshop.checkout.vouchers.lists.VoucherList;
import com.shuvo.ttit.terrainshop.signup.lists.DivisionList;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

import static com.shuvo.ttit.terrainshop.checkout.CheckOut.sub_total;
import static com.shuvo.ttit.terrainshop.checkout.CheckOut.delivery_fee;
import static com.shuvo.ttit.terrainshop.checkout.CheckOut.voucherAmount;
import static com.shuvo.ttit.terrainshop.checkout.CheckOut.voucherCode;
import static com.shuvo.ttit.terrainshop.connection.OracleConnection.createConnection;

public class Voucher extends AppCompatActivity implements VoucherAdapter.ClickedItem {


    TextInputEditText voucherText;
    TextInputLayout voucherLay;
    MaterialButton apply;
    ImageView close;

    TextView noVoucher;
    RecyclerView voucherView;
    VoucherAdapter voucherAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<VoucherList> voucherLists;

    CircularProgressIndicator circularProgressIndicator;
    RelativeLayout fullLayout;

    Connection connection;
    private Boolean conn = false;
    private Boolean connected = false;

    String div_id = "";
    String voucher_text = "";
    String disType = "";
    String disValue = "";

    boolean voucherOk = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);

        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.white));

        voucherText = findViewById(R.id.voucher_given_voucher_generate);
        voucherLay = findViewById(R.id.voucher_text_layout_voucher_generate);

        apply = findViewById(R.id.voucher_apply_button_voucher_generate);
        close = findViewById(R.id.close_logo_voucher_generate);

        voucherView = findViewById(R.id.voucher_list_view);

        noVoucher = findViewById(R.id.no_voucher_msg_vouchers);
        noVoucher.setVisibility(View.GONE);

        fullLayout = findViewById(R.id.voucher_design_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_voucher);
        circularProgressIndicator.setVisibility(View.GONE);

        voucherLists = new ArrayList<>();

        voucherView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        voucherView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        div_id = intent.getStringExtra("DIVISION ID");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voucher_text = Objects.requireNonNull(voucherText.getText()).toString();
                if (!voucher_text.isEmpty()) {

                    new VoucherCheck().execute();
                } else {
                    voucherLay.setHelperText("Please give your voucher code first.");
                }
            }
        });

        new Check().execute();

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

    @Override
    public void onCategoryClicked(int CategoryPosition) {

        String v_code = voucherLists.get(CategoryPosition).getVoucher_code();
        voucherText.setText(v_code);
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

                if (voucherLists.size() == 0) {
                    noVoucher.setVisibility(View.VISIBLE);
                }
                else {
                    noVoucher.setVisibility(View.GONE);
                }

                voucherAdapter = new VoucherAdapter(voucherLists,Voucher.this, Voucher.this);
                voucherView.setAdapter(voucherAdapter);




            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection or Slow Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(Voucher.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new Check().execute();
                        dialog.dismiss();
                    }
                });

            }
        }
    }

    public void Query () {

        try {
            this.connection = createConnection();


            voucherLists = new ArrayList<>();

            Statement stmt = connection.createStatement();


            ResultSet rs1 = stmt.executeQuery("SELECT DPCM_ID, DPCM_DIV_ID, DPCM_PROMOTIONAL_NAME, DPCM_PROMO_EFFECTIVE_DATE, DPCM_PROMO_EFECTIVE_TO_DATE \n" +
                    "FROM DELIVERY_PROMO_CHARGE_MST\n" +
                    "WHERE DPCM_DIV_ID = "+div_id+"\n" +
                    "AND TRUNC(SYSDATE) BETWEEN TRUNC(DPCM_PROMO_EFFECTIVE_DATE) AND TRUNC(DPCM_PROMO_EFECTIVE_TO_DATE)");

            while (rs1.next()) {
                ArrayList<VoucherDetailsList> voucherDetailsLists = new ArrayList<>();
                String dpcm_id = rs1.getString(1);

                voucherLists.add(new VoucherList(rs1.getString(1),rs1.getString(2),rs1.getString(3),rs1.getString(4),
                        rs1.getString(5),voucherDetailsLists));
            }

            rs1.close();

            for (int i = 0; i < voucherLists.size(); i++) {
                String dpcm_id = voucherLists.get(i).getDpcm_id();

                ArrayList<VoucherDetailsList> voucherDetailsLists = new ArrayList<>();

                ResultSet resultSet = stmt.executeQuery("SELECT DPCD_ID, DPCD_DPCM_ID, DPCD_INVOICE_COMP_FROM_AMT, DPCD_DISCOUNT_TYPE, DPCD_DISCOUNT_VALUE, DPCD_INVOICE_COMP_TO_AMT\n" +
                        "FROM DELIVERY_PROMO_CHARGE_DTL\n" +
                        "WHERE DPCD_DPCM_ID = "+dpcm_id+"");

                while (resultSet.next()) {
                    voucherDetailsLists.add(new VoucherDetailsList(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),
                            resultSet.getString(4),resultSet.getString(5),resultSet.getString(6)));
                }
                resultSet.close();

                voucherLists.get(i).setVoucherDetailsLists(voucherDetailsLists);

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


    public class VoucherCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            circularProgressIndicator.setVisibility(View.VISIBLE);
            fullLayout.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                VoucherQuery();
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

                if (voucherOk) {
                    voucherOk = false;
                    if (disType != null && disValue != null) {
                        if (!disType.isEmpty() && !disValue.isEmpty()) {
                            if (disType.contains("%")) {
                                double voucher = (double) (delivery_fee * Double.parseDouble(disValue)/100);
                                voucherAmount = (int) voucher;
                            }
                            else if (disType.contains("SR")) {
                                double voucher = Double.parseDouble(disValue);
                                voucherAmount = (int) voucher;
                            }

                            voucherCode = voucher_text;
                            finish();
//                            CheckOut.VoucherCheck();
                        }
                        else {
                            voucherLay.setHelperText("Could not meet required condition to use this voucher.");
                        }
                    }
                    else {
                        voucherLay.setHelperText("Could not meet required condition to use this voucher.");
                    }
                }
                else {
                    voucherLay.setHelperText("Wrong voucher code.");
                }

            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection or Slow Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(Voucher.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new VoucherCheck().execute();
                        dialog.dismiss();
                    }
                });

            }
        }
    }

    public void VoucherQuery () {

        try {
            this.connection = createConnection();

            Statement stmt = connection.createStatement();

            String dpcm_id = "";

            ResultSet rs1 = stmt.executeQuery("Select DPCM_ID FROM DELIVERY_PROMO_CHARGE_MST WHERE DPCM_PROMOTIONAL_NAME = '"+voucher_text+"' and DPCM_DIV_ID = "+div_id+" AND TRUNC(SYSDATE) BETWEEN TRUNC(DPCM_PROMO_EFFECTIVE_DATE) AND TRUNC(DPCM_PROMO_EFECTIVE_TO_DATE)");

            while (rs1.next()) {
                dpcm_id = rs1.getString(1);
            }
            rs1.close();


            if (dpcm_id != null) {
                if (!dpcm_id.isEmpty()) {
                    voucherOk = true;
                    ResultSet resultSet = stmt.executeQuery("SELECT DPCD_ID, DPCD_DPCM_ID, DPCD_INVOICE_COMP_FROM_AMT, DPCD_DISCOUNT_TYPE, DPCD_DISCOUNT_VALUE, DPCD_INVOICE_COMP_TO_AMT\n" +
                            "FROM DELIVERY_PROMO_CHARGE_DTL\n" +
                            "WHERE DPCD_DPCM_ID = "+dpcm_id+"\n" +
                            "AND "+sub_total+" BETWEEN DPCD_INVOICE_COMP_FROM_AMT AND DPCD_INVOICE_COMP_TO_AMT");

                    while (resultSet.next()) {

                        disType = resultSet.getString(4);
                        disValue = resultSet.getString(5);
                    }
                    resultSet.close();
                }
                else {
                    voucherOk = false;
                }
            }
            else {
                voucherOk = false;
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