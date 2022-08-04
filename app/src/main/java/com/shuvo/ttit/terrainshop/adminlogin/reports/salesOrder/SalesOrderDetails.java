package com.shuvo.ttit.terrainshop.adminlogin.reports.salesOrder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.adminlogin.reports.salesOrder.adapters.SalesOrderAdapter;
import com.shuvo.ttit.terrainshop.adminlogin.reports.salesOrder.adapters.SalesOrderDetailsAdapter;
import com.shuvo.ttit.terrainshop.adminlogin.reports.salesOrder.lists.SalesOrderDetailList;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;

import oracle.jdbc.driver.OracleTypes;

import static com.shuvo.ttit.terrainshop.connection.OracleConnection.createConnection;

public class SalesOrderDetails extends AppCompatActivity {

    RelativeLayout sodBar;

    RelativeLayout fullLayout;
    CircularProgressIndicator circularProgressIndicator;

    Connection connection;
    private Boolean conn = false;
    private Boolean connected = false;

    private AsyncTask mTask;

    TextView salesOrderNo;
    TextView salesOrderDate;
    TextView clientName;
    TextView clientCode;
    TextView clientEmail;
    TextView contactNo;
    TextView tDAddress;
    TextView tDLocation;
    TextView eDDate;
    TextView dTime;
    TextView dStatus;
    TextView totalOrderAmount;
    TextView deliveryCharge;
    TextView totalAmount;

    String som_id = "";
    String order_no = "";
    String order_date = "";
    String client_name = "";
    String client_code = "";
    String contact_no = "";
    String client_email = "";
    String d_address = "";
    String d_location = "";
    String d_date = "";
    String d_time = "";
    String d_status = "";
    String total_order_amnt = "";
    String delivery_charge = "";
    String total_amnt = "";

    RecyclerView itemView;
    RecyclerView.LayoutManager layoutManager;
    SalesOrderDetailsAdapter salesOrderDetailsAdapter;
    ArrayList<SalesOrderDetailList> salesOrderDetailLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_order_details);

        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.back_color));

        sodBar = findViewById(R.id.sales_order_details_action_bar);
        fullLayout = findViewById(R.id.sales_order_details_design_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_sales_order_details);
        circularProgressIndicator.setVisibility(View.GONE);

        salesOrderNo = findViewById(R.id.sales_order_no_from_so);
        salesOrderDate = findViewById(R.id.so_date_from_so);
        clientName = findViewById(R.id.so_client_name_from_so);
        clientCode = findViewById(R.id.so_client_code_from_so);
        contactNo = findViewById(R.id.so_contact_no_from_so);
        clientEmail = findViewById(R.id.so_email_from_so);
        tDAddress = findViewById(R.id.target_delivery_address_from_so);
        tDLocation = findViewById(R.id.target_delivery_location_from_so);
        eDDate = findViewById(R.id.so_expected_delivery_date_from_so);
        dTime = findViewById(R.id.so_expected_delivery_time_from_so);
        dStatus = findViewById(R.id.so_delivery_status_from_so);
        totalOrderAmount = findViewById(R.id.total_order_amount_in_sales_order_details);
        deliveryCharge = findViewById(R.id.delivery_charge_in_sales_order_details);
        totalAmount = findViewById(R.id.total_amount_in_sales_order_details);
        itemView = findViewById(R.id.sales_order_details_report_view);

        salesOrderDetailLists = new ArrayList<>();

        itemView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(SalesOrderDetails.this);
        itemView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(itemView.getContext(),DividerItemDecoration.VERTICAL);
        itemView.addItemDecoration(dividerItemDecoration);

        Intent intent = getIntent();

        som_id = intent.getStringExtra("SOM_ID");
        order_no = intent.getStringExtra("ORDER NO");
        order_date = intent.getStringExtra("ORDER DATE");
        client_name = intent.getStringExtra("CLIENT NAME");
        client_code = intent.getStringExtra("CLIENT CODE");
        contact_no = intent.getStringExtra("CONTACT NO");
        client_email = intent.getStringExtra("EMAIL");
        d_address = intent.getStringExtra("DELIVERY ADDRESS");
        d_location = intent.getStringExtra("LOCATION");
        d_date = intent.getStringExtra("DELIVERY DATE");
        d_time = intent.getStringExtra("DELIVERY TIME");
        d_status = intent.getStringExtra("DELIVERY STATUS");
        total_order_amnt = intent.getStringExtra("TOTAL ORDER AMOUNT");
        delivery_charge = intent.getStringExtra("DELIVERY CHARGE");
        total_amnt = intent.getStringExtra("TOTAL AMOUNT");

        salesOrderNo.setText(order_no);
        salesOrderDate.setText(order_date);
        clientName.setText(client_name);
        clientCode.setText(client_code);
        contactNo.setText(contact_no);
        clientEmail.setText(client_email);
        tDAddress.setText(d_address);
        tDLocation.setText(d_location);
        eDDate.setText(d_date);
        dTime.setText(d_time);
        if (d_status.equals("DELIVERED")) {
            dStatus.setText(d_status);
            dStatus.setTextColor(Color.parseColor("#FF018786"));
        }
        else {
            dStatus.setText(d_status);
            dStatus.setTextColor(Color.parseColor("#b2bec3"));
        }

        totalOrderAmount.setText(total_order_amnt);
        deliveryCharge.setText(delivery_charge);
        totalAmount.setText(total_amnt);

        sodBar.setOnClickListener(new View.OnClickListener() {
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


                salesOrderDetailsAdapter = new SalesOrderDetailsAdapter(salesOrderDetailLists, SalesOrderDetails.this);
                itemView.setAdapter(salesOrderDetailsAdapter);

                conn = false;
                connected = false;


            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection or Slow Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(SalesOrderDetails.this)
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

            salesOrderDetailLists = new ArrayList<>();

            CallableStatement callableStatement1 = connection.prepareCall("begin ? := GET_SALES_ORDER_DTL_LIST(?); end;");
            callableStatement1.setInt(2,Integer.parseInt(som_id));

            callableStatement1.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement1.execute();

            ResultSet resultSet1 = (ResultSet) callableStatement1.getObject(1);

            while (resultSet1.next()) {

                salesOrderDetailLists.add(new SalesOrderDetailList(resultSet1.getString(1), resultSet1.getString(2), resultSet1.getString(3),
                        resultSet1.getString(4),resultSet1.getString(5),resultSet1.getString(6),
                        resultSet1.getString(7),resultSet1.getString(8),resultSet1.getString(9),
                        resultSet1.getString(10),resultSet1.getString(11),resultSet1.getString(12),
                        resultSet1.getString(13),resultSet1.getString(14),resultSet1.getString(15),
                        resultSet1.getString(16)));

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