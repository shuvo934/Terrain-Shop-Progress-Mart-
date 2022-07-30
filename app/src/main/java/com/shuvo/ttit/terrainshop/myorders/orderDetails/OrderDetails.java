package com.shuvo.ttit.terrainshop.myorders.orderDetails;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.myorders.adpaters.OrderDetailsAdapter;
import com.shuvo.ttit.terrainshop.myorders.lists.OrderDetailsList;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static com.shuvo.ttit.terrainshop.connection.OracleConnection.createConnection;

public class OrderDetails extends AppCompatActivity {

    TextView orderNo;
    String order_no = "";

    TextView address;
    String d_address = "";

    TextView oDate;
    String o_date = "";

    TextView dDate;
    String d_date = "";

    TextView dTime;
    String d_time = "";

    TextView deliveryCharge;

    TextView dStatus;
    String d_status = "";

    RecyclerView odsView;
    RecyclerView.LayoutManager layoutManager;
    OrderDetailsAdapter orderDetailsAdapter;

    ArrayList<OrderDetailsList> orderDetailsLists;

    TextView subTotal;
    int sub_total = 0;

    TextView totalPrice;
    String total_price = "";

    Connection connection;
    private Boolean conn = false;
    private Boolean connected = false;
    String som_id = "";
    String delivery_charge = "";

    RelativeLayout layout;
    CircularProgressIndicator circularProgressIndicator;

    RelativeLayout orderDBar;
    private AsyncTask mTask;
    ScrollView orderScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.back_color));

        orderNo = findViewById(R.id.order_no_order_details);
        address = findViewById(R.id.delivery_address_order_details);
        oDate = findViewById(R.id.order_date_order_details);
        dDate = findViewById(R.id.delivery_date_order_details);
        dTime = findViewById(R.id.delivery_time_order_details);
        dStatus = findViewById(R.id.order_tracking_details);

        odsView = findViewById(R.id.order_summary_list_view_order_details);

        subTotal = findViewById(R.id.subtotal_of_order_summary_order_details);

        totalPrice = findViewById(R.id.total_price_of_order_order_details);

        layout = findViewById(R.id.order_details_full_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_orders_details);
        circularProgressIndicator.setVisibility(View.GONE);

        orderDBar = findViewById(R.id.my_orders_details_action_bar);

        deliveryCharge = findViewById(R.id.delivery_fee_total_order_details);

        orderScroll = findViewById(R.id.order_details_scroll_view);

        orderDetailsLists = new ArrayList<>();

        Intent intent = getIntent();
        order_no = intent.getStringExtra("ORDER NO");
        d_address = intent.getStringExtra("DELIVERY ADDRESS");
        d_date = intent.getStringExtra("DELIVERY DATE");
        o_date = intent.getStringExtra("ORDER DATE");
        som_id = intent.getStringExtra("SOM ID");
        delivery_charge = intent.getStringExtra("DELIVERY FEE");
        d_time = intent.getStringExtra("DELIVERY TIME");
        d_status = intent.getStringExtra("DELIVERY STATUS");

        String orno = "ORDER NO: \n" + order_no;
        orderNo.setText(orno);
        address.setText(d_address);
        dDate.setText(d_date);
        oDate.setText(o_date);
        String dc = "৳ " + delivery_charge;
        deliveryCharge.setText(dc);
        dTime.setText(d_time);
        d_status = "ORDER " +d_status;
        dStatus.setText(d_status);

        odsView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        odsView.setLayoutManager(layoutManager);

        orderDBar.setOnClickListener(new View.OnClickListener() {
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

    public class Check extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            circularProgressIndicator.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
            orderScroll.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                OrderHistoryQuery();
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
            layout.setVisibility(View.VISIBLE);

            if (conn) {

                orderScroll.setVisibility(View.VISIBLE);
                orderDetailsAdapter = new OrderDetailsAdapter( OrderDetails.this,orderDetailsLists);
                odsView.setAdapter(orderDetailsAdapter);

                sub_total= 0;
                for (int i = 0; i < orderDetailsLists.size() ; i++) {
                    sub_total = sub_total + Integer.parseInt(orderDetailsLists.get(i).getOrder_rate());
                }


                String st = "৳ " + String.valueOf(sub_total);
                subTotal.setText(st);

                sub_total = sub_total + Integer.parseInt(delivery_charge);
                total_price = "৳ " + String.valueOf(sub_total);
                totalPrice.setText(total_price);




            }else {
                orderScroll.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(OrderDetails.this)
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

    public void OrderHistoryQuery () {

        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            Statement stmt = connection.createStatement();
            orderDetailsLists = new ArrayList<>();

            ResultSet rs = stmt.executeQuery("SELECT SOD_ITEM_ID, SOD_QTY,SOD_DEFINE_RATE,\n" +
                    "(SOD_DEFINE_RATE - (CASE WHEN SOD_DISCOUNT_TYPE is NULL then 0 \n" +
                    "else \n" +
                    "(CASE WHEN SOD_DISCOUNT_TYPE = '%' then SOD_DEFINE_RATE * SOD_DISCOUNT_VALUE/100 \n" +
                    "else\n" +
                    "SOD_DISCOUNT_VALUE end) \n" +
                    "end)) as CARD_RATE,\n" +
                    "((SOD_DEFINE_RATE - (CASE WHEN SOD_DISCOUNT_TYPE is NULL then 0 \n" +
                    "else \n" +
                    "(CASE WHEN SOD_DISCOUNT_TYPE = '%' then SOD_DEFINE_RATE * SOD_DISCOUNT_VALUE/100 \n" +
                    "else\n" +
                    "SOD_DISCOUNT_VALUE end) \n" +
                    "end)) * SOD_QTY) as TOTAL,\n" +
                    "(SELECT IEM_NAME\n" +
                    "FROM ITEM_ECOM_MST\n" +
                    "WHERE iem_id = (Select max(iem_id) from ITEM_ECOM_MST WHERE IEM_ITEM_ID = SOD_ITEM_ID)) NAME,SOD_DISCOUNT_TYPE, \n" +
                    "(SELECT IEM_THUMB\n" +
                    "FROM ITEM_ECOM_MST\n" +
                    "WHERE iem_id = (Select max(iem_id) from ITEM_ECOM_MST WHERE IEM_ITEM_ID = SOD_ITEM_ID)) PIC,ITEM_PACK.GET_ITEM_UNIT(SOD_ITEM_ID)\n" +
                    "FROM SALES_ORDER_DTL\n" +
                    "WHERE SOD_SOM_ID = "+som_id+"");

            while (rs.next()) {
                Blob b=rs.getBlob(8);
                byte[] barr =b.getBytes(1,(int)b.length());
                Bitmap bitmap = BitmapFactory.decodeByteArray(barr,0,barr.length);
                orderDetailsLists.add(new OrderDetailsList(rs.getString(1),rs.getString(2),rs.getString(3),
                        rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),bitmap,rs.getString(9)));
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