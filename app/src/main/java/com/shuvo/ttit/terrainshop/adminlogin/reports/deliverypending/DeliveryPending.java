package com.shuvo.ttit.terrainshop.adminlogin.reports.deliverypending;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rosemaryapp.amazingspinner.AmazingSpinner;
import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.adminlogin.reports.deliverypending.adapters.DeliveryPendingAdapter;
import com.shuvo.ttit.terrainshop.adminlogin.reports.deliverypending.lists.DeliveryPendingList;
import com.shuvo.ttit.terrainshop.adminlogin.reports.salesOrder.OrderCollection;
import com.shuvo.ttit.terrainshop.adminlogin.reports.salesOrder.SalesOrderDetails;
import com.shuvo.ttit.terrainshop.adminlogin.reports.salesOrder.adapters.SalesOrderAdapter;
import com.shuvo.ttit.terrainshop.adminlogin.reports.salesOrder.lists.SalesOrderList;
import com.shuvo.ttit.terrainshop.signup.lists.DivisionList;
import com.shuvo.ttit.terrainshop.signup.lists.ThanaList;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import oracle.jdbc.driver.OracleTypes;

import static com.shuvo.ttit.terrainshop.connection.OracleConnection.createConnection;

public class DeliveryPending extends AppCompatActivity implements DeliveryPendingAdapter.ClickedItem {

    RelativeLayout deliveryPendingBar;

    RelativeLayout fullLayout;
    CircularProgressIndicator circularProgressIndicator;

    TextInputEditText beginDate;
    TextInputEditText endDate;
    TextView daterange;
    AmazingSpinner divisionSpinner;
    AmazingSpinner locationSpinner;
    TextInputLayout thanaLay;
    TextView noOrderMsg;
    TextView totalDeliveryPendingAmount;

    Connection connection;
    private Boolean conn = false;
    private Boolean connected = false;

    private AsyncTask mTask;

    String firstDate = "";
    String lastDate = "";
    String div_id = "";
    String dd_id = "";
    String div_Name = "";
    String thana_name = "";
    double total_del_pen_amnt = 0.0;
    private int mYear, mMonth, mDay;

    RecyclerView itemView;
    DeliveryPendingAdapter deliveryPendingAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<DeliveryPendingList> deliveryPendingLists;
    ArrayList<DivisionList> divisionLists;
    ArrayList<ThanaList> thanaLists;
    ArrayList<DeliveryPendingList> filteredList;

    private boolean isfiltered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_pending);

        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.back_color));

        deliveryPendingBar = findViewById(R.id.delivery_pending_action_bar);
        fullLayout = findViewById(R.id.delivery_pending_design_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_delivery_pending);
        circularProgressIndicator.setVisibility(View.GONE);

        beginDate = findViewById(R.id.begin_date_delivery_pending);
        endDate = findViewById(R.id.end_date_delivery_pending);
        daterange = findViewById(R.id.date_range_msg_delivery_pending);
        divisionSpinner = findViewById(R.id.division_name_dp_spinner);
        locationSpinner = findViewById(R.id.location_name_dp_spinner);
        thanaLay = findViewById(R.id.spinner_layout_location_name_dp);
        thanaLay.setEnabled(false);
        itemView = findViewById(R.id.delivery_pending_report_view);
        noOrderMsg = findViewById(R.id.no_delivery_pending_msg);

        totalDeliveryPendingAmount = findViewById(R.id.total_delivery_pending_amount);

        deliveryPendingLists = new ArrayList<>();
        divisionLists = new ArrayList<>();
        thanaLists = new ArrayList<>();
        filteredList = new ArrayList<>();

        itemView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        itemView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(itemView.getContext(),DividerItemDecoration.VERTICAL);
        itemView.addItemDecoration(dividerItemDecoration);

        //getting date
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM-yy",Locale.getDefault());

        firstDate = simpleDateFormat.format(c);
        firstDate = "01-"+firstDate;
        //firstDate = df.format(c);
        lastDate = df.format(c);

        beginDate.setText(firstDate);
        endDate.setText(lastDate);

        beginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(DeliveryPending.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            String monthName = "";
                            String dayOfMonthName = "";
                            String yearName = "";
                            month = month + 1;
                            if (month == 1) {
                                monthName = "JAN";
                            } else if (month == 2) {
                                monthName = "FEB";
                            } else if (month == 3) {
                                monthName = "MAR";
                            } else if (month == 4) {
                                monthName = "APR";
                            } else if (month == 5) {
                                monthName = "MAY";
                            } else if (month == 6) {
                                monthName = "JUN";
                            } else if (month == 7) {
                                monthName = "JUL";
                            } else if (month == 8) {
                                monthName = "AUG";
                            } else if (month == 9) {
                                monthName = "SEP";
                            } else if (month == 10) {
                                monthName = "OCT";
                            } else if (month == 11) {
                                monthName = "NOV";
                            } else if (month == 12) {
                                monthName = "DEC";
                            }

                            if (dayOfMonth <= 9) {
                                dayOfMonthName = "0" + String.valueOf(dayOfMonth);
                            } else {
                                dayOfMonthName = String.valueOf(dayOfMonth);
                            }
                            yearName  = String.valueOf(year);
                            yearName = yearName.substring(yearName.length()-2);
                            System.out.println(yearName);
                            System.out.println(dayOfMonthName);
                            beginDate.setText(dayOfMonthName + "-" + monthName + "-" + yearName);
                            firstDate = beginDate.getText().toString();
                            if (lastDate.isEmpty()) {
                                daterange.setVisibility(View.GONE);
                                mTask = new OrderCheck().execute();

                            } else {
                                Date bDate = null;
                                Date eDate = null;

                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());

                                try {
                                    bDate = sdf.parse(firstDate);
                                    eDate = sdf.parse(lastDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                if (bDate != null && eDate != null) {
                                    if (eDate.after(bDate)|| eDate.equals(bDate)) {
                                        //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                                        daterange.setVisibility(View.GONE);

                                        mTask = new OrderCheck().execute();

                                    }
                                    else {
                                        daterange.setVisibility(View.VISIBLE);
                                        beginDate.setText("");
                                    }
                                }
                            }

                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(DeliveryPending.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            String monthName = "";
                            String dayOfMonthName = "";
                            String yearName = "";
                            month = month + 1;
                            if (month == 1) {
                                monthName = "JAN";
                            } else if (month == 2) {
                                monthName = "FEB";
                            } else if (month == 3) {
                                monthName = "MAR";
                            } else if (month == 4) {
                                monthName = "APR";
                            } else if (month == 5) {
                                monthName = "MAY";
                            } else if (month == 6) {
                                monthName = "JUN";
                            } else if (month == 7) {
                                monthName = "JUL";
                            } else if (month == 8) {
                                monthName = "AUG";
                            } else if (month == 9) {
                                monthName = "SEP";
                            } else if (month == 10) {
                                monthName = "OCT";
                            } else if (month == 11) {
                                monthName = "NOV";
                            } else if (month == 12) {
                                monthName = "DEC";
                            }

                            if (dayOfMonth <= 9) {
                                dayOfMonthName = "0" + String.valueOf(dayOfMonth);
                            } else {
                                dayOfMonthName = String.valueOf(dayOfMonth);
                            }
                            yearName  = String.valueOf(year);
                            yearName = yearName.substring(yearName.length()-2);
                            System.out.println(yearName);
                            System.out.println(dayOfMonthName);
                            endDate.setText(dayOfMonthName + "-" + monthName + "-" + yearName);
                            lastDate = endDate.getText().toString();

                            if (firstDate.isEmpty()) {
                                daterange.setVisibility(View.GONE);
                                mTask = new OrderCheck().execute();

                            } else {
                                Date bDate = null;
                                Date eDate = null;

                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());

                                try {
                                    bDate = sdf.parse(firstDate);
                                    eDate = sdf.parse(lastDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                if (bDate != null && eDate != null) {
                                    if (eDate.after(bDate)|| eDate.equals(bDate)) {
                                        //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                                        daterange.setVisibility(View.GONE);

                                        mTask = new OrderCheck().execute();

                                    }
                                    else {
                                        daterange.setVisibility(View.VISIBLE);
                                        endDate.setText("");

                                    }
                                }
                            }

                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });

        divisionSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                thanaLay.setEnabled(false);
                locationSpinner.setText("");
                div_id = "";
                div_Name = "";
                thana_name = "";
                dd_id = "";
                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < divisionLists.size(); j++) {
                    if (name.equals(divisionLists.get(j).getDivName())) {
                        div_id = (divisionLists.get(j).getDivId());
                        if (div_id.isEmpty()) {
                            div_Name = "";
                        }
                        else {
                            div_Name = divisionLists.get(j).getDivName();
                        }
                    }
                }
                if (name.equals("...")) {
                    divisionSpinner.setText("");
                }

                filterCate(div_Name);
                System.out.println(name);
                System.out.println(div_id);

                mTask = new ThanaCheck().execute();

            }
        });

        locationSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                dd_id = "";
                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < thanaLists.size(); j++) {
                    if (name.equals(thanaLists.get(j).getThanaName())) {
                        dd_id = (thanaLists.get(j).getDdId());
                        if (dd_id.isEmpty()) {
                            thana_name = "";
                        }
                        else {
                            thana_name = thanaLists.get(j).getThanaName();
                        }
                    }
                }
                System.out.println(dd_id);

                if (name.equals("...")) {
                    locationSpinner.setText("");
                }

                filterSubCate(thana_name);
            }
        });

        deliveryPendingBar.setOnClickListener(new View.OnClickListener() {
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

    private void filterCate(String text) {

        filteredList = new ArrayList<>();
        for (DeliveryPendingList item : deliveryPendingLists) {
            if (thana_name.isEmpty()){
                if (item.getDivision_name().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add((item));
                    isfiltered = true;
                }
            } else {
                if (item.getLocation_name().toLowerCase().contains(thana_name.toLowerCase())) {
                    if (item.getDivision_name().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add((item));
                        isfiltered = true;
                    }
                }
            }
//            if (searchingName.isEmpty()) {
//                if (item.getDep_name().toLowerCase().contains(text.toLowerCase())) {
//                    filteredList.add((item));
//                }
//            } else {
//                if (item.getEmp_name().toLowerCase().contains(searchingName.toLowerCase())) {
//                    if (item.getDep_name().toLowerCase().contains(text.toLowerCase())) {
//                        filteredList.add((item));
//                    }
//                }
//            }

        }
        deliveryPendingAdapter.filterList(filteredList);
        totalOfAll();
    }

    private void filterSubCate(String text) {

        filteredList = new ArrayList<>();
        for (DeliveryPendingList item : deliveryPendingLists) {
            if (div_Name.isEmpty()) {
                if (item.getLocation_name().toLowerCase().contains(text.toLowerCase())){
                    filteredList.add((item));
                    isfiltered = true;
                }
            } else {
                if (item.getDivision_name().toLowerCase().contains(div_Name.toLowerCase())){
                    if (item.getLocation_name().toLowerCase().contains(text.toLowerCase())){
                        filteredList.add((item));
                        isfiltered = true;
                    }
                }
            }

        }
        deliveryPendingAdapter.filterList(filteredList);
        totalOfAll();
    }

    private void totalOfAll() {
        total_del_pen_amnt = 0.0;
        if (isfiltered) {
            for (DeliveryPendingList item : filteredList) {
                if (item.getPending_amnt() != null) {
                    total_del_pen_amnt = total_del_pen_amnt + Double.parseDouble(item.getPending_amnt());
                }

            }
            DecimalFormat formatter = new DecimalFormat("##,##,##,###");
            String formatted = formatter.format(total_del_pen_amnt);
            totalDeliveryPendingAmount.setText(formatted + " BDT");
        }
        else {
            for (DeliveryPendingList item : deliveryPendingLists) {
                if (item.getPending_amnt() != null) {
                    total_del_pen_amnt = total_del_pen_amnt + Double.parseDouble(item.getPending_amnt());
                }

            }
            DecimalFormat formatter = new DecimalFormat("##,##,##,###");
            String formatted = formatter.format(total_del_pen_amnt);
            totalDeliveryPendingAmount.setText(formatted + " BDT");
        }
    }

    @Override
    public void onCategoryClicked(int CategoryPosition) {

        if (isfiltered) {
            Intent intent = new Intent(DeliveryPending.this, SalesOrderDetails.class);
            intent.putExtra("SOM_ID", filteredList.get(CategoryPosition).getSom_id());
            intent.putExtra("ORDER NO", filteredList.get(CategoryPosition).getOrder_no());
            intent.putExtra("ORDER DATE", filteredList.get(CategoryPosition).getSom_date());
            intent.putExtra("CLIENT NAME", filteredList.get(CategoryPosition).getAd_name());
            intent.putExtra("CLIENT CODE", filteredList.get(CategoryPosition).getAd_code());
            intent.putExtra("CONTACT NO", filteredList.get(CategoryPosition).getContact_no());
            intent.putExtra("EMAIL", filteredList.get(CategoryPosition).getEmail());
            intent.putExtra("DELIVERY ADDRESS", filteredList.get(CategoryPosition).getDelivery_address());
            String location = filteredList.get(CategoryPosition).getLocation_name() + ", " + filteredList.get(CategoryPosition).getDivision_name();
            intent.putExtra("LOCATION", location);
            intent.putExtra("DELIVERY DATE", filteredList.get(CategoryPosition).getDelivery_date());
            intent.putExtra("DELIVERY TIME", filteredList.get(CategoryPosition).getExpected_time());
            intent.putExtra("DELIVERY STATUS", filteredList.get(CategoryPosition).getDelivery_status());
            intent.putExtra("TOTAL ORDER AMOUNT", filteredList.get(CategoryPosition).getOrder_amnt());
            intent.putExtra("DELIVERY CHARGE", filteredList.get(CategoryPosition).getDelivery_charge());

            int charge = Integer.parseInt(filteredList.get(CategoryPosition).getDelivery_charge());
            int amnt = Integer.parseInt(filteredList.get(CategoryPosition).getOrder_amnt());

            int tt = charge + amnt;

            String total = String.valueOf(tt);
            intent.putExtra("TOTAL AMOUNT", total);

            startActivity(intent);
        }
        else {

            Intent intent = new Intent(DeliveryPending.this, SalesOrderDetails.class);
            intent.putExtra("SOM_ID", deliveryPendingLists.get(CategoryPosition).getSom_id());
            intent.putExtra("ORDER NO", deliveryPendingLists.get(CategoryPosition).getOrder_no());
            intent.putExtra("ORDER DATE", deliveryPendingLists.get(CategoryPosition).getSom_date());
            intent.putExtra("CLIENT NAME", deliveryPendingLists.get(CategoryPosition).getAd_name());
            intent.putExtra("CLIENT CODE", deliveryPendingLists.get(CategoryPosition).getAd_code());
            intent.putExtra("CONTACT NO", deliveryPendingLists.get(CategoryPosition).getContact_no());
            intent.putExtra("EMAIL", deliveryPendingLists.get(CategoryPosition).getEmail());
            intent.putExtra("DELIVERY ADDRESS", deliveryPendingLists.get(CategoryPosition).getDelivery_address());
            String location = deliveryPendingLists.get(CategoryPosition).getLocation_name() + ", " + deliveryPendingLists.get(CategoryPosition).getDivision_name();
            intent.putExtra("LOCATION", location);
            intent.putExtra("DELIVERY DATE", deliveryPendingLists.get(CategoryPosition).getDelivery_date());
            intent.putExtra("DELIVERY TIME", deliveryPendingLists.get(CategoryPosition).getExpected_time());
            intent.putExtra("DELIVERY STATUS", deliveryPendingLists.get(CategoryPosition).getDelivery_status());
            intent.putExtra("TOTAL ORDER AMOUNT", deliveryPendingLists.get(CategoryPosition).getOrder_amnt());
            intent.putExtra("DELIVERY CHARGE", deliveryPendingLists.get(CategoryPosition).getDelivery_charge());

            int charge = Integer.parseInt(deliveryPendingLists.get(CategoryPosition).getDelivery_charge());
            int amnt = Integer.parseInt(deliveryPendingLists.get(CategoryPosition).getOrder_amnt());

            int tt = charge + amnt;

            String total = String.valueOf(tt);
            intent.putExtra("TOTAL AMOUNT", total);

            startActivity(intent);
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


                ArrayList<String> type2 = new ArrayList<>();
                for(int i = 0; i < divisionLists.size(); i++) {
                    type2.add(divisionLists.get(i).getDivName());
                }
                ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type2);

                divisionSpinner.setAdapter(arrayAdapter2);


                if (deliveryPendingLists.size() ==0) {
                    noOrderMsg.setVisibility(View.VISIBLE);
                }else {
                    noOrderMsg.setVisibility(View.GONE);
                }

                DecimalFormat formatter = new DecimalFormat("##,##,##,###");

                deliveryPendingAdapter = new DeliveryPendingAdapter(deliveryPendingLists, DeliveryPending.this,DeliveryPending.this);
                itemView.setAdapter(deliveryPendingAdapter);

                for (int i = 0; i < deliveryPendingLists.size(); i++) {
                    if (deliveryPendingLists.get(i).getPending_amnt() != null) {
                        total_del_pen_amnt = total_del_pen_amnt + Double.parseDouble(deliveryPendingLists.get(i).getPending_amnt());
                    }
                }

                String formatted = formatter.format(total_del_pen_amnt);
                totalDeliveryPendingAmount.setText(formatted + " BDT");

                conn = false;
                connected = false;


            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection or Slow Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(DeliveryPending.this)
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

                thanaLay.setEnabled(!div_id.isEmpty() && div_id != null);

                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < thanaLists.size(); i++) {
                    type.add(thanaLists.get(i).getThanaName());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                locationSpinner.setAdapter(arrayAdapter);

                conn = false;
                connected = false;

            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection or Slow Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(DeliveryPending.this)
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
    public class OrderCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            circularProgressIndicator.setVisibility(View.VISIBLE);
            fullLayout.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                OrderQuery();
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


                locationSpinner.setText("");
                thanaLay.setEnabled(false);
                divisionSpinner.setText("");
                div_id = "";
                dd_id = "";
                isfiltered = false;


                if (deliveryPendingLists.size() ==0) {
                    noOrderMsg.setVisibility(View.VISIBLE);
                }else {
                    noOrderMsg.setVisibility(View.GONE);
                }

                DecimalFormat formatter = new DecimalFormat("##,##,##,###");

                deliveryPendingAdapter = new DeliveryPendingAdapter(deliveryPendingLists, DeliveryPending.this,DeliveryPending.this);
                itemView.setAdapter(deliveryPendingAdapter);

                for (int i = 0; i < deliveryPendingLists.size(); i++) {
                    if (deliveryPendingLists.get(i).getPending_amnt() != null) {
                        total_del_pen_amnt = total_del_pen_amnt + Double.parseDouble(deliveryPendingLists.get(i).getPending_amnt());
                    }
                }

                String formatted = formatter.format(total_del_pen_amnt);
                totalDeliveryPendingAmount.setText(formatted + " BDT");

                conn = false;
                connected = false;

            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection or Slow Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(DeliveryPending.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mTask = new OrderCheck().execute();
                        dialog.dismiss();
                    }
                });

            }
        }
    }

    public void Query () {

        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            Statement stmt = connection.createStatement();

            divisionLists = new ArrayList<>();
            deliveryPendingLists = new ArrayList<>();

            if (firstDate.isEmpty()) {
                firstDate = null;
            }
            if (lastDate.isEmpty()) {
                lastDate = null;
            }

            divisionLists.add(new DivisionList("","..."));
            ResultSet rs1 = stmt.executeQuery("SELECT DIVISION.DIV_ID P_DIV_ID, DIVISION.DIV_NAME FROM DIVISION WHERE DIV_ACTIVE_FLAG=1 ORDER BY DIVISION.DIV_ID");

            while (rs1.next()) {
                divisionLists.add(new DivisionList(rs1.getString(1),rs1.getString(2)));
            }

            rs1.close();

            CallableStatement callableStatement = connection.prepareCall("begin ? := GET_SALES_ORDER_PENDING_LIST(?,?,?); end;");
            callableStatement.setString(2,firstDate);
            callableStatement.setString(3,lastDate);
            callableStatement.setString(4,null);
            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.execute();

            ResultSet resultSet1 = (ResultSet) callableStatement.getObject(1);

            while (resultSet1.next()) {

                int qty = resultSet1.getInt(21);
                int d_qty = resultSet1.getInt(22);
                String d_status = "" ;
                if (qty == d_qty) {
                    d_status = "DELIVERED";
                }
                else {
                    d_status = "PENDING";
                }

                deliveryPendingLists.add(new DeliveryPendingList(resultSet1.getString(1), resultSet1.getString(2),resultSet1.getString(3),
                        resultSet1.getString(4), resultSet1.getString(5),resultSet1.getString(6),
                        resultSet1.getString(7),resultSet1.getString(8), resultSet1.getString(9),
                        resultSet1.getString(10),resultSet1.getString(11),resultSet1.getString(12),
                        resultSet1.getString(13),resultSet1.getString(14),resultSet1.getString(15),
                        resultSet1.getString(16),resultSet1.getString(17),resultSet1.getString(18),
                        resultSet1.getString(19),resultSet1.getString(20),resultSet1.getString(21),
                        resultSet1.getString(22),resultSet1.getString(23),resultSet1.getString(24),d_status));
            }

            resultSet1.close();
            callableStatement.close();

            if (firstDate == null) {
                firstDate = "";
            }
            if (lastDate == null) {
                lastDate = "";
            }


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

            if (div_id.isEmpty()) {
                div_id = null;
            }

            thanaLists.add(new ThanaList("","...",""));

            ResultSet rs = stmt.executeQuery("Select DD_ID, DD_THANA_NAME, DD_DIST_ID from district_dtl where DD_ACTIVE_FLAG = 1 \n" +
                    "AND DD_DIST_ID IN (SELECT DIST_ID FROM DISTRICT WHERE DIST_DIV_ID = "+div_id+" AND DIST_ACTIVE_FLAG = 1) order by dd_id");

            while (rs.next()) {
                thanaLists.add(new ThanaList(rs.getString(1),rs.getString(2),rs.getString(3)));
            }

            rs.close();

            stmt.close();

            if (div_id == null) {
                div_id = "";
            }

            connected = true;

            connection.close();

        } catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

    public void OrderQuery () {

        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            deliveryPendingLists = new ArrayList<>();
            total_del_pen_amnt = 0.0;

            if (firstDate.isEmpty()) {
                firstDate = null;
            }
            if (lastDate.isEmpty()) {
                lastDate = null;
            }

            CallableStatement callableStatement = connection.prepareCall("begin ? := GET_SALES_ORDER_PENDING_LIST(?,?,?); end;");
            callableStatement.setString(2,firstDate);
            callableStatement.setString(3,lastDate);
            callableStatement.setString(4,null);
            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.execute();

            ResultSet resultSet1 = (ResultSet) callableStatement.getObject(1);

            while (resultSet1.next()) {

                int qty = resultSet1.getInt(21);
                int d_qty = resultSet1.getInt(22);
                String d_status = "" ;
                if (qty == d_qty) {
                    d_status = "DELIVERED";
                }
                else {
                    d_status = "PENDING";
                }

                deliveryPendingLists.add(new DeliveryPendingList(resultSet1.getString(1), resultSet1.getString(2),resultSet1.getString(3),
                        resultSet1.getString(4), resultSet1.getString(5),resultSet1.getString(6),
                        resultSet1.getString(7),resultSet1.getString(8), resultSet1.getString(9),
                        resultSet1.getString(10),resultSet1.getString(11),resultSet1.getString(12),
                        resultSet1.getString(13),resultSet1.getString(14),resultSet1.getString(15),
                        resultSet1.getString(16),resultSet1.getString(17),resultSet1.getString(18),
                        resultSet1.getString(19),resultSet1.getString(20),resultSet1.getString(21),
                        resultSet1.getString(22),resultSet1.getString(23),resultSet1.getString(24),d_status));
            }

            resultSet1.close();
            callableStatement.close();

            if (firstDate == null) {
                firstDate = "";
            }
            if (lastDate == null) {
                lastDate = "";
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