package com.shuvo.ttit.terrainshop.myorders;

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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.checkout.CheckOut;
import com.shuvo.ttit.terrainshop.myorders.adpaters.OrderHistoryAdapter;
import com.shuvo.ttit.terrainshop.myorders.lists.OrderList;
import com.shuvo.ttit.terrainshop.myorders.orderDetails.OrderDetails;
import com.shuvo.ttit.terrainshop.orderComplete.OrderCompleted;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static com.shuvo.ttit.terrainshop.cart.OrderCart.myCartList;
import static com.shuvo.ttit.terrainshop.connection.OracleConnection.createConnection;
import static com.shuvo.ttit.terrainshop.login.Login.userInfoLists;

public class MyOrders extends AppCompatActivity implements OrderHistoryAdapter.ClickedItem {

    RecyclerView orderView;
    RecyclerView.LayoutManager layoutManager;
    OrderHistoryAdapter orderHistoryAdapter;

    RelativeLayout orderBar;
    CircularProgressIndicator circularProgressIndicator;

    Connection connection;
    private Boolean conn = false;
    private Boolean connected = false;
    String ad_id = "";

    ArrayList<OrderList> orderLists;
    TextView noOrder;
    private AsyncTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.back_color));

        orderBar = findViewById(R.id.my_orders_action_bar);
        orderView = findViewById(R.id.my_order_history_list_view);
        circularProgressIndicator = findViewById(R.id.progress_indicator_my_orders);
        circularProgressIndicator.setVisibility(View.GONE);
        noOrder = findViewById(R.id.no_order_msg_my_orders);
        noOrder.setVisibility(View.GONE);

        ad_id = userInfoLists.get(0).getAd_id();

        orderLists = new ArrayList<>();

        orderView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        orderView.setLayoutManager(layoutManager);

        orderBar.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onCategoryClicked(int CategoryPosition) {

        System.out.println("DWDADADADW");
        Intent intent = new Intent(MyOrders.this, OrderDetails.class);
        intent.putExtra("ORDER NO", orderLists.get(CategoryPosition).getSom_order_no());
        intent.putExtra("DELIVERY ADDRESS", orderLists.get(CategoryPosition).getDelivery_address());
        intent.putExtra("DELIVERY DATE", orderLists.get(CategoryPosition).getDelivery_date());
        intent.putExtra("ORDER DATE", orderLists.get(CategoryPosition).getSom_date());
        intent.putExtra("SOM ID",orderLists.get(CategoryPosition).getSom_id());
        intent.putExtra("DELIVERY FEE", orderLists.get(CategoryPosition).getDelivery_charge());
        intent.putExtra("DELIVERY TIME", orderLists.get(CategoryPosition).getDelivery_time());
        intent.putExtra("DELIVERY STATUS", orderLists.get(CategoryPosition).getOrder_tracking());
        startActivity(intent);
    }

    public class Check extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            circularProgressIndicator.setVisibility(View.VISIBLE);
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

            if (conn) {

                orderHistoryAdapter = new OrderHistoryAdapter(orderLists,MyOrders.this, MyOrders.this);
                orderView.setAdapter(orderHistoryAdapter);
                if (orderLists.size() == 0) {
                    noOrder.setVisibility(View.VISIBLE);
                }else {
                    noOrder.setVisibility(View.GONE);
                }


            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(MyOrders.this)
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
            orderLists = new ArrayList<>();

            ResultSet rs = stmt.executeQuery("Select SOM_ID, SOM_ORDER_NO, TO_CHAR(SOM_DATE,'DD-MON-YY'), SOM_ORDER_NO_ID, TO_CHAR(SOM_EDD,'DD-MON-YY'), \n" +
                    "SOM_DELIVERY_ADDRESS, SOM_DISCOUNT_TYPE, SOM_DISCOUNT_VALUE, SOM_ADVANCE_AMT, NVL(SOM_DELIVERY_CHARGE,0), \n" +
                    "(Select SUM((SOD_DEFINE_RATE - (CASE WHEN SOD_DISCOUNT_TYPE is NULL then 0 \n" +
                    "else \n" +
                    "(CASE WHEN SOD_DISCOUNT_TYPE = '%' then SOD_DEFINE_RATE * SOD_DISCOUNT_VALUE/100 \n" +
                    "else\n" +
                    "SOD_DISCOUNT_VALUE end) \n" +
                    "end)) * SOD_QTY) \n" +
                    "FROM SALES_ORDER_DTL Where SOD_SOM_ID = SOM_ID) as TOTAL,\n" +
                    "NVL((Select TSM_NAME FROM TIME_SLOT_MANAGER WHERE TSM_ID = SOM_TSM_ID),'NO TIME FOUND') D_TIME,\n" +
                    "(CASE WHEN SOM_ORDER_TRACKING = 1 THEN 'PLACED' WHEN SOM_ORDER_TRACKING = 2 THEN 'CONFIRMED' else NULL end) as ORDER_TRACKING\n" +
                    "FROM SALES_ORDER_MST\n" +
                    "WHERE SOM_AD_ID = "+ad_id+"\n" +
                    "order by SOM_ID desc");

            while (rs.next()) {
                orderLists.add(new OrderList(rs.getString(1),rs.getString(2),rs.getString(3),
                        rs.getString(4),rs.getString(5),rs.getString(6),
                        rs.getString(7),rs.getString(8),rs.getString(9),
                        rs.getString(10),rs.getString(11),rs.getString(12),rs.getString(13)));
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