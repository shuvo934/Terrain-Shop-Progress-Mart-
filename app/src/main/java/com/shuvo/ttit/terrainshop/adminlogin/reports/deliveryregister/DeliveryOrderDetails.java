package com.shuvo.ttit.terrainshop.adminlogin.reports.deliveryregister;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.adminlogin.reports.deliveryregister.adapters.DeliveryOrderDetailsAdapter;
import com.shuvo.ttit.terrainshop.adminlogin.reports.deliveryregister.lists.DeliveryDetailsList;
import com.shuvo.ttit.terrainshop.adminlogin.reports.salesOrder.SalesOrderDetails;
import com.shuvo.ttit.terrainshop.adminlogin.reports.salesOrder.adapters.SalesOrderDetailsAdapter;
import com.shuvo.ttit.terrainshop.adminlogin.reports.salesOrder.lists.SalesOrderDetailList;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import oracle.jdbc.driver.OracleTypes;

import static com.shuvo.ttit.terrainshop.connection.OracleConnection.createConnection;

public class DeliveryOrderDetails extends AppCompatActivity {

    RelativeLayout dodBar;

    RelativeLayout fullLayout;
    CircularProgressIndicator circularProgressIndicator;

    Connection connection;
    private Boolean conn = false;
    private Boolean connected = false;

    private AsyncTask mTask;

    TextView deliveryOrderNo;
    TextView deliveryDate;
    TextView salesOrderNo;
    TextView salesOrderDate;
    TextView clientName;
    TextView clientCode;
    TextView clientEmail;
    TextView contactNo;
    TextView tDAddress;
    TextView tDLocation;
    TextView deliveryCharge;
    TextView totalInvoiceAmount;

    String sm_id = "";
    String delivery_no = "";
    String delivery_date = "";
    String order_no = "";
    String order_date = "";
    String client_name = "";
    String client_code = "";
    String contact_no = "";
    String client_email = "";
    String d_address = "";
    String d_location = "";
    String delivery_charge = "";
    String total_invoice_amnt = "";

    RecyclerView itemView;
    RecyclerView.LayoutManager layoutManager;
    DeliveryOrderDetailsAdapter deliveryOrderDetailsAdapter;
    ArrayList<DeliveryDetailsList> deliveryDetailsLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_order_details);

        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.back_color));

        dodBar = findViewById(R.id.delivery_order_details_action_bar);
        fullLayout = findViewById(R.id.delivery_order_details_design_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_delivery_order_details);
        circularProgressIndicator.setVisibility(View.GONE);

        deliveryOrderNo = findViewById(R.id.delivery_order_no_from_dr);
        deliveryDate = findViewById(R.id.dr_date_from_dr);
        salesOrderNo = findViewById(R.id.sales_order_no_from_dr);
        salesOrderDate = findViewById(R.id.so_date_from_dr);
        clientName = findViewById(R.id.dr_client_name_from_dr);
        clientCode = findViewById(R.id.dr_client_code_from_dr);
        contactNo = findViewById(R.id.dr_contact_no_from_dr);
        clientEmail = findViewById(R.id.dr_email_from_dr);
        tDAddress = findViewById(R.id.target_delivery_address_from_dr);
        tDLocation = findViewById(R.id.target_delivery_location_from_dr);
        totalInvoiceAmount = findViewById(R.id.total_invoice_amount_in_delivery_order_details);
        deliveryCharge = findViewById(R.id.delivery_charge_from_dr);

        itemView = findViewById(R.id.delivery_order_details_report_view);

        deliveryDetailsLists = new ArrayList<>();

        itemView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        itemView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(itemView.getContext(),DividerItemDecoration.VERTICAL);
        itemView.addItemDecoration(dividerItemDecoration);

        Intent intent = getIntent();

        sm_id = intent.getStringExtra("SM_ID");
        delivery_no = intent.getStringExtra("DELIVERY NO");
        delivery_date = intent.getStringExtra("DELIVERY DATE");
        order_no = intent.getStringExtra("ORDER NO");
        order_date = intent.getStringExtra("ORDER DATE");

        client_name = intent.getStringExtra("CLIENT NAME");
        client_code = intent.getStringExtra("CLIENT CODE");
        contact_no = intent.getStringExtra("CONTACT NO");
        client_email = intent.getStringExtra("EMAIL");

        d_address = intent.getStringExtra("DELIVERY ADDRESS");
        d_location = intent.getStringExtra("LOCATION");
        total_invoice_amnt = intent.getStringExtra("TOTAL INVOICE AMOUNT");
        delivery_charge = intent.getStringExtra("DELIVERY CHARGE");

        deliveryOrderNo.setText(delivery_no);
        deliveryDate.setText(delivery_date);
        salesOrderNo.setText(order_no);
        salesOrderDate.setText(order_date);

        clientName.setText(client_name);
        clientCode.setText(client_code);
        contactNo.setText(contact_no);
        clientEmail.setText(client_email);

        tDAddress.setText(d_address);
        tDLocation.setText(d_location);
        String ch = delivery_charge + " BDT";
        deliveryCharge.setText(ch);
        totalInvoiceAmount.setText(total_invoice_amnt);

        dodBar.setOnClickListener(new View.OnClickListener() {
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


                deliveryOrderDetailsAdapter = new DeliveryOrderDetailsAdapter(deliveryDetailsLists, DeliveryOrderDetails.this);
                itemView.setAdapter(deliveryOrderDetailsAdapter);

                conn = false;
                connected = false;


            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection or Slow Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(DeliveryOrderDetails.this)
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

    public void Query() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            deliveryDetailsLists = new ArrayList<>();

            CallableStatement callableStatement1 = connection.prepareCall("begin ? := GET_INVOICE_DTL_REGISTER_LIST(?); end;");
            callableStatement1.setInt(2,Integer.parseInt(sm_id));

            callableStatement1.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement1.execute();

            ResultSet resultSet1 = (ResultSet) callableStatement1.getObject(1);

            while (resultSet1.next()) {

                deliveryDetailsLists.add(new DeliveryDetailsList(resultSet1.getString(1), resultSet1.getString(2), resultSet1.getString(3),
                        resultSet1.getString(4),resultSet1.getString(5),resultSet1.getString(6),
                        resultSet1.getString(7),resultSet1.getString(8),resultSet1.getString(9),
                        resultSet1.getString(10),resultSet1.getString(11),resultSet1.getString(12),
                        resultSet1.getString(13),resultSet1.getString(14),resultSet1.getString(15),
                        resultSet1.getString(16),resultSet1.getString(17),resultSet1.getString(18),
                        resultSet1.getString(19)));

            }
            resultSet1.close();

            callableStatement1.close();

            connected = true;

            connection.close();

        }
        catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}