package com.shuvo.ttit.terrainshop.checkout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rosemaryapp.amazingspinner.AmazingSpinner;
import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.checkout.adapters.OrderSummaryAdapter;
import com.shuvo.ttit.terrainshop.checkout.lists.DistrictList;
import com.shuvo.ttit.terrainshop.checkout.lists.TimeSlotList;
import com.shuvo.ttit.terrainshop.checkout.lists.UpazilaList;
import com.shuvo.ttit.terrainshop.checkout.vouchers.Voucher;
import com.shuvo.ttit.terrainshop.orderComplete.OrderCompleted;
import com.shuvo.ttit.terrainshop.signup.lists.DivisionList;
import com.shuvo.ttit.terrainshop.signup.lists.ThanaList;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.shuvo.ttit.terrainshop.cart.OrderCart.myCartList;
import static com.shuvo.ttit.terrainshop.connection.OracleConnection.createConnection;
import static com.shuvo.ttit.terrainshop.login.Login.userInfoLists;

public class CheckOut extends AppCompatActivity {

    MaterialCheckBox checkBoxDelivery;
    TextInputEditText deliveryAddress;
    TextInputLayout deliverAddLay;

    MaterialCheckBox checkBoxBilling;
    TextInputEditText billingAddress;
    TextInputLayout billingAddLay;

    AmazingSpinner division;
    TextInputLayout divisionLay;
    TextView noDistrict;

    AmazingSpinner upazila;
    TextInputLayout upazilaLay;
    TextView noUpazila;

    TextInputEditText date;
    TextInputLayout dateLay;
    private int mYear, mMonth, mDay;

    AmazingSpinner timeSlot;
    TextInputLayout timeSlotLay;
    TextView noTimeSlot;

    RecyclerView orderSummaryView;
    OrderSummaryAdapter orderSummaryAdapter;
    RecyclerView.LayoutManager layoutManager;

    TextView subTotal;
    public static int sub_total = 0;

    RelativeLayout checkOutBar;

    RelativeLayout checkOutLayout;
    CircularProgressIndicator circularProgressIndicator;

    static CardView voucherCard;
    public static int voucherAmount = 0;
    public static String voucherCode = "";

    public static LinearLayout afterVoucher;
    public static TextView voucherText;
    TextView voucherRemove;
    public static TextView voucherDiscount;

    static TextView totalPrice;
    static int total_price = 0;
    public static int delivery_fee = 0;
    int deliveryReal_fee = 0;
    static TextView deliveryFeeText;
    TextView deliveryFeeRealText;
    String deliveryDisType = "";
    String deliveryDisValue = "";

    MaterialButton placeOrder;

    String delivery_address = "";
    String billing_address = "";
    String expected_d_date = "";
    String order_no = "";


    String dist_id = "";

    ArrayList<ThanaList> upazilaLists;
    String dd_id = "";

    ArrayList<DivisionList> divisionLists;
    String div_id = "";

    ArrayList<TimeSlotList> timeSlotLists;
    String tsm_id = "";

    Connection connection;
    private Boolean conn = false;
    private Boolean connected = false;
    boolean stockReached = false;
    String stockReachedItemName = "";
    String stockLimit = "";

    boolean deliveryDiscount = false;

    RadioButton cashDelivery;

    private AsyncTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.back_color));

        checkOutBar = findViewById(R.id.check_out_action_bar);
        checkOutLayout = findViewById(R.id.check_out_design_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_checkout);
        circularProgressIndicator.setVisibility(View.GONE);

        checkBoxDelivery = findViewById(R.id.radio_button_for_delivery_address_checkout);
        deliveryAddress = findViewById(R.id.delivery_address_given_checkout);
        deliverAddLay = findViewById(R.id.delivery_address_checkout);

        checkBoxBilling = findViewById(R.id.radio_button_for_billing_address_checkout);
        billingAddress = findViewById(R.id.billing_address_given_checkout);
        billingAddLay = findViewById(R.id.billing_address_checkout);

        date = findViewById(R.id.expected_delivery_date_check_out);
        dateLay = findViewById(R.id.expected_delivery_date_layout_check_out);

        orderSummaryView = findViewById(R.id.order_summary_list_view);

        subTotal = findViewById(R.id.subtotal_of_order_summary);

        voucherCard = findViewById(R.id.voucher_generate_card);
        voucherCard.setEnabled(false);
        voucherCard.setCardBackgroundColor(Color.LTGRAY);

        afterVoucher = findViewById(R.id.after_voucher_generation);
        voucherText = findViewById(R.id.voucher_code_after_checkout);
        voucherRemove = findViewById(R.id.remove_voucher_code_checkout);
        voucherDiscount = findViewById(R.id.after_voucher_generation_discount_checkout);

        totalPrice = findViewById(R.id.total_price_of_all_item_checkout);

        placeOrder = findViewById(R.id.place_order_button_checkout);

        division = findViewById(R.id.division_spinner_checkout);
        divisionLay = findViewById(R.id.spinner_layout_division_checkout);

        noDistrict = findViewById(R.id.no_district_msg_checkout);
        noDistrict.setVisibility(View.GONE);

        upazila = findViewById(R.id.thana_spinner_checkout);
        upazilaLay = findViewById(R.id.spinner_layout_thana_checkout);
        upazilaLay.setEnabled(false);
        noUpazila = findViewById(R.id.no_upazila_msg_checkout);

        timeSlot = findViewById(R.id.time_slot_spinner_checkout);
        timeSlotLay = findViewById(R.id.spinner_layout_time_slot_checkout);
        noTimeSlot = findViewById(R.id.no_time_slot_msg_checkout);

        deliveryFeeText = findViewById(R.id.delivery_fee_total_checkout);
        deliveryFeeRealText = findViewById(R.id.delivery_fee_real_total_checkout);
        deliveryFeeRealText.setVisibility(View.GONE);
        cashDelivery = findViewById(R.id.radioCashDelivery);

        divisionLists = new ArrayList<>();
        upazilaLists = new ArrayList<>();
        timeSlotLists = new ArrayList<>();

        div_id = userInfoLists.get(0).getAd_div_id();
        dist_id = userInfoLists.get(0).getAd_dist_id();
        dd_id = userInfoLists.get(0).getAd_dd_id();

        orderSummaryView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        orderSummaryView.setLayoutManager(layoutManager);
        orderSummaryAdapter = new OrderSummaryAdapter(this,myCartList);
        orderSummaryView.setAdapter(orderSummaryAdapter);

        deliveryAddress.setText(userInfoLists.get(0).getAd_address());
        checkBoxDelivery.setChecked(true);

        checkBoxDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("WHY ?? "+ checkBoxDelivery.isChecked());

                if (checkBoxDelivery.isChecked()) {
                    deliveryAddress.setText(userInfoLists.get(0).getAd_address());
                    //deliveryAddress.setEnabled(false);
                }
                else {
                    //deliveryAddress.setText("");
                    deliveryAddress.setEnabled(true);
                }

            }
        });

        checkBoxBilling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("WHY ?? "+ checkBoxBilling.isChecked());

                if (checkBoxBilling.isChecked()) {
                    billingAddress.setText(userInfoLists.get(0).getAd_address());
                    //deliveryAddress.setEnabled(false);
                }
                else {
                    //deliveryAddress.setText("");
                    billingAddress.setEnabled(true);
                }

            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, +10);
                Date result = cal.getTime();

                Calendar ccc = Calendar.getInstance();
                ccc.add(Calendar.DAY_OF_MONTH,+1);
                Date ff = ccc.getTime();

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(CheckOut.this, new DatePickerDialog.OnDateSetListener() {
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
                            date.setText(dayOfMonthName + "-" + monthName + "-" + yearName);
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.getDatePicker().setMaxDate(result.getTime());
                    datePickerDialog.getDatePicker().setMinDate(ff.getTime());
                    datePickerDialog.show();
                }
            }
        });

        sub_total = 0;
        for (int i = 0; i < myCartList.size(); i++) {
            sub_total = sub_total + Integer.parseInt(myCartList.get(i).getItem_total_price());
        }

        subTotal.setText("৳ " + String.valueOf(sub_total));

        checkOutBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTask != null) {
                    if (mTask.getStatus().toString().equals("RUNNING")) {
                        Toast.makeText(getApplicationContext(),"Please wait while loading",Toast.LENGTH_SHORT).show();
                    } else {
                        voucherAmount = 0;
                        voucherCode = "";
                        total_price = 0;
                        sub_total = 0;
                        delivery_fee = 0;
                        finish();
                    }
                }
                else {
                    voucherAmount = 0;
                    voucherCode = "";
                    total_price = 0;
                    sub_total = 0;
                    delivery_fee = 0;
                    finish();
                }

            }
        });

        voucherCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                voucherAmount = 0;
//                voucherCode = "";
//                Intent intent = new Intent(CheckOut.this, Voucher.class);
//                intent.putExtra("DIVISION ID", div_id);
//                startActivity(intent);
            }
        });

        voucherRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voucherAmount = 0;
                voucherCode = "";
                VoucherCheck();
            }
        });

        total_price = sub_total + delivery_fee - voucherAmount;
        String tt = "৳ " + String.valueOf(total_price);
        totalPrice.setText(tt);

        deliveryAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                deliverAddLay.setHelperText("");
            }
        });

        billingAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                billingAddLay.setHelperText("");
            }
        });

        date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                dateLay.setHelperText("");
            }
        });

        division.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                upazilaLay.setEnabled(false);
                upazila.setText("");
                div_id = "";
                dist_id = "";
                dd_id = "";
                delivery_fee = 0;
                voucherAmount = 0;
                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < divisionLists.size(); j++) {
                    if (name.equals(divisionLists.get(j).getDivName())) {
                        div_id = (divisionLists.get(j).getDivId());
                    }
                }
                if (name.equals("...")) {
                    division.setText("");
                }
                System.out.println(name);
                System.out.println(div_id);
                noDistrict.setVisibility(View.GONE);
                voucherCard.setEnabled(false);
                voucherCard.setCardBackgroundColor(Color.LTGRAY);
                deliveryFeeRealText.setVisibility(View.GONE);

                if (!div_id.isEmpty()) {
                    mTask = new UpazilaCheck().execute();
                }

            }
        });

        upazila.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                dist_id = "";
                dd_id = "";
                delivery_fee = 0;
                voucherAmount = 0;
                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < upazilaLists.size(); j++) {
                    if (name.equals(upazilaLists.get(j).getThanaName())) {
                        dd_id = (upazilaLists.get(j).getDdId());
                        dist_id = upazilaLists.get(i).getDist_id();
                    }
                }
                System.out.println(dd_id);
                noUpazila.setVisibility(View.GONE);
                deliveryFeeRealText.setVisibility(View.GONE);

                mTask = new DeliveryFeeCheck().execute();

            }
        });

        timeSlot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                tsm_id = "";
                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < timeSlotLists.size(); j++) {
                    if (name.equals(timeSlotLists.get(j).getTsm_name())) {
                        tsm_id = (timeSlotLists.get(j).getTsm_id());
                    }
                }

                noTimeSlot.setVisibility(View.GONE);

            }
        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                delivery_address = deliveryAddress.getText().toString();
                billing_address = billingAddress.getText().toString();
                expected_d_date = date.getText().toString();

                if (div_id != null) {
                    if (!div_id.isEmpty()) {

                        if (dd_id != null) {
                            if (!dd_id.isEmpty()) {
                                noUpazila.setVisibility(View.GONE);
                                if (!delivery_address.isEmpty()) {
                                    deliverAddLay.setHelperText("");
//                            if (!billing_address.isEmpty()) {
//                                billingAddLay.setHelperText("");
                                    if (!expected_d_date.isEmpty()) {
                                        dateLay.setHelperText("");
                                        if (!tsm_id.isEmpty()) {
                                            noTimeSlot.setVisibility(View.GONE);
                                            if (cashDelivery.isChecked()) {
                                                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(CheckOut.this,R.style.Theme_MyApp_Dialog_Alert);
                                                builder.setTitle("ALERT!")
                                                        .setMessage("Do you want to place your order now?")
                                                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                mTask = new OrderCheck().execute();
                                                            }
                                                        })
                                                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {

                                                            }
                                                        });
                                                AlertDialog alert = builder.create();
                                                alert.show();
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(),"Please select Payment Method",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else {
                                            noTimeSlot.setVisibility(View.VISIBLE);
                                            Toast.makeText(getApplicationContext(),"Please select delivery time",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else {
                                        dateLay.setHelperText("Please give expected delivery date");
                                        Toast.makeText(getApplicationContext(),"Please give expected delivery date",Toast.LENGTH_SHORT).show();
                                    }
//                            }
//                            else {
//                                billingAddLay.setHelperText("Please give billing address");
//                                Toast.makeText(getApplicationContext(),"Please give billing address",Toast.LENGTH_SHORT).show();
//                            }
                                }
                                else {
                                    deliverAddLay.setHelperText("Please give delivery address");
                                    Toast.makeText(getApplicationContext(),"Please give delivery address",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                noUpazila.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(),"Please Select Location",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            noUpazila.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(),"Please Select Location",Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {
                        noDistrict.setVisibility(View.VISIBLE);
                        noDistrict.setText("Please select Division");
                        Toast.makeText(getApplicationContext(),"Please Select Division",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    noDistrict.setVisibility(View.VISIBLE);
                    noDistrict.setText("Please select Division");
                    Toast.makeText(getApplicationContext(),"Please Select Division",Toast.LENGTH_SHORT).show();
                }

            }
        });

        closeKeyBoard();
        mTask = new Check().execute();


    }

    @Override
    protected void onResume() {
        super.onResume();
        VoucherCheck();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        closeKeyBoard();
        return super.onTouchEvent(event);
    }

    public static void VoucherCheck() {

        if (voucherAmount == 0) {
            voucherCard.setVisibility(View.VISIBLE);
            afterVoucher.setVisibility(View.GONE);
            voucherText.setText(voucherCode);
            voucherDiscount.setText("৳ 0");
        }
        else {
            voucherCard.setVisibility(View.GONE);
            afterVoucher.setVisibility(View.VISIBLE);
            voucherText.setText(voucherCode);
            voucherDiscount.setText("- ৳ "+ String.valueOf(voucherAmount));
        }

        String df = "৳ " + String.valueOf(delivery_fee);
        deliveryFeeText.setText(df);

        total_price = sub_total + delivery_fee - voucherAmount;
        String tt = "৳ " + String.valueOf(total_price);
        totalPrice.setText(tt);
    }

    @Override
    public void onBackPressed() {

        if (mTask != null) {
            if (mTask.getStatus().toString().equals("RUNNING")) {
                Toast.makeText(getApplicationContext(),"Please wait while loading",Toast.LENGTH_SHORT).show();
            } else {
                voucherAmount = 0;
                voucherCode = "";
                total_price = 0;
                sub_total = 0;
                delivery_fee = 0;
                finish();
            }
        }
        else {
            voucherAmount = 0;
            voucherCode = "";
            total_price = 0;
            sub_total = 0;
            delivery_fee = 0;
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

    public class Check extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            circularProgressIndicator.setVisibility(View.VISIBLE);
            checkOutLayout.setVisibility(View.GONE);
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
            checkOutLayout.setVisibility(View.VISIBLE);

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

                        upazilaLay.setEnabled(true);

                        if (dd_id != null) {
                            if (!dd_id.isEmpty()) {
                                String upazilaName = "";
                                for (int i = 0; i < upazilaLists.size(); i++) {
                                    if (dd_id.equals(upazilaLists.get(i).getDdId())) {
                                        upazilaName = upazilaLists.get(i).getThanaName();
                                        dist_id = upazilaLists.get(i).getDist_id();
                                    }
                                }
                                upazila.setText(upazilaName);

                                if (deliveryDiscount) {
                                    deliveryDiscount = false;
                                    if (deliveryDisType != null && deliveryDisValue != null) {
                                        if (!deliveryDisType.isEmpty() && !deliveryDisValue.isEmpty()) {

                                            if (deliveryDisType.contains("%")) {
                                                double discount = (double) (deliveryReal_fee * Double.parseDouble(deliveryDisValue)/100);
                                                delivery_fee = (int) (deliveryReal_fee - discount);
                                            }
                                            else if (deliveryDisType.contains("SR")) {
                                                delivery_fee = (int) (deliveryReal_fee - Double.parseDouble(deliveryDisValue));
                                            }

                                            deliveryFeeRealText.setText("৳ "+ String.valueOf(deliveryReal_fee));
                                            deliveryFeeRealText.setVisibility(View.VISIBLE);
                                            deliveryFeeRealText.setPaintFlags(deliveryFeeRealText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                                        }
                                        else {
                                            deliveryFeeRealText.setVisibility(View.GONE);
                                            delivery_fee = deliveryReal_fee;
                                        }
                                    }
                                    else {
                                        deliveryFeeRealText.setVisibility(View.GONE);
                                        delivery_fee = deliveryReal_fee;
                                    }
                                }
                                else {
                                    deliveryFeeRealText.setVisibility(View.GONE);
                                    delivery_fee = deliveryReal_fee;
                                }

                                voucherCard.setEnabled(true);
                                voucherCard.setCardBackgroundColor(Color.parseColor("#e1b12c"));
                            }
                        }

                        ArrayList<String> type = new ArrayList<>();
                        for(int i = 0; i < upazilaLists.size(); i++) {
                            type.add(upazilaLists.get(i).getThanaName());
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                        upazila.setAdapter(arrayAdapter);

                    }
                }


                ArrayList<String> type2 = new ArrayList<>();
                for(int i = 0; i < divisionLists.size(); i++) {
                    type2.add(divisionLists.get(i).getDivName());
                }
                ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type2);

                division.setAdapter(arrayAdapter2);

                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < timeSlotLists.size(); i++) {
                    type.add(timeSlotLists.get(i).getTsm_name());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                timeSlot.setAdapter(arrayAdapter);

                VoucherCheck();

                closeKeyBoard();

            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection or Slow Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(CheckOut.this)
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
                        voucherAmount = 0;
                        voucherCode = "";
                        total_price = 0;
                        sub_total = 0;
                        delivery_fee = 0;
                        finish();
                    }
                });
            }
        }
    }

    public class UpazilaCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            circularProgressIndicator.setVisibility(View.VISIBLE);
            checkOutLayout.setVisibility(View.GONE);
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
            checkOutLayout.setVisibility(View.VISIBLE);

            if (conn) {

                upazilaLay.setEnabled(true);

                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < upazilaLists.size(); i++) {
                    type.add(upazilaLists.get(i).getThanaName());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                upazila.setAdapter(arrayAdapter);

                VoucherCheck();

                conn = false;
                connected = false;

            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection or Slow Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(CheckOut.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mTask = new UpazilaCheck().execute();
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public class DeliveryFeeCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            circularProgressIndicator.setVisibility(View.VISIBLE);
            checkOutLayout.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                DeliveryFeeQuery();
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
            checkOutLayout.setVisibility(View.VISIBLE);

            if (conn) {

                conn = false;
                connected = false;

                if (deliveryDiscount) {
                    deliveryDiscount = false;
                    if (deliveryDisType != null && deliveryDisValue != null) {
                        if (!deliveryDisType.isEmpty() && !deliveryDisValue.isEmpty()) {

                            if (deliveryDisType.contains("%")) {
                                double discount = (double) (deliveryReal_fee * Double.parseDouble(deliveryDisValue)/100);
                                delivery_fee = (int) (deliveryReal_fee - discount);
                            }
                            else if (deliveryDisType.contains("SR")) {
                                delivery_fee = (int) (deliveryReal_fee - Double.parseDouble(deliveryDisValue));
                            }

                            deliveryFeeRealText.setText("৳ "+ String.valueOf(deliveryReal_fee));
                            deliveryFeeRealText.setVisibility(View.VISIBLE);
                            deliveryFeeRealText.setPaintFlags(deliveryFeeRealText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                        }
                        else {
                            deliveryFeeRealText.setVisibility(View.GONE);
                            delivery_fee = deliveryReal_fee;
                        }
                    }
                    else {
                        deliveryFeeRealText.setVisibility(View.GONE);
                        delivery_fee = deliveryReal_fee;
                    }
                }
                else {
                    deliveryFeeRealText.setVisibility(View.GONE);
                    delivery_fee = deliveryReal_fee;
                }

                VoucherCheck();
                voucherCard.setEnabled(true);
                voucherCard.setCardBackgroundColor(Color.parseColor("#e1b12c"));




            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection or Slow Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(CheckOut.this)
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

                        mTask = new DeliveryFeeCheck().execute();
                        dialog.dismiss();
                    }
                });
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        voucherAmount = 0;
                        voucherCode = "";
                        total_price = 0;
                        sub_total = 0;
                        delivery_fee = 0;
                        finish();
                    }
                });
            }
        }
    }

    public class OrderCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            circularProgressIndicator.setVisibility(View.VISIBLE);
            checkOutLayout.setVisibility(View.GONE);
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
            checkOutLayout.setVisibility(View.VISIBLE);

            if (conn) {


                conn = false;
                connected = false;

                if (stockReached) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(CheckOut.this,R.style.Theme_MyApp_Dialog_Alert);
                    builder.setTitle("STOCK OUT!")
                            .setMessage("Your " +stockReachedItemName +"'s quantity is above our stock quantity. This product's stock quantity remains at "+ stockLimit+ ". Sorry for inconvenience.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else {
                    voucherAmount = 0;
                    voucherCode = "";
                    total_price = 0;
                    sub_total = 0;
                    delivery_fee = 0;
                    myCartList = new ArrayList<>();
                    Intent intent = new Intent(CheckOut.this, OrderCompleted.class);
                    intent.putExtra("ORDER NO", order_no);
                    intent.putExtra("DELIVERY ADDRESS", delivery_address);
                    intent.putExtra("DELIVERY DATE", expected_d_date);
                    startActivity(intent);
                    finish();
                }


            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(CheckOut.this)
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


            divisionLists = new ArrayList<>();
            timeSlotLists = new ArrayList<>();
            upazilaLists = new ArrayList<>();
            delivery_fee = 0;
            deliveryReal_fee = 0;
            deliveryDiscount = false;

            Statement stmt = connection.createStatement();

            ResultSet rs1 = stmt.executeQuery("SELECT DIVISION.DIV_ID P_DIV_ID, DIVISION.DIV_NAME FROM DIVISION WHERE DIV_ACTIVE_FLAG=1 ORDER BY DIVISION.DIV_ID");

            while (rs1.next()) {
                divisionLists.add(new DivisionList(rs1.getString(1),rs1.getString(2)));
            }

            rs1.close();

            ResultSet resultSet = stmt.executeQuery("Select TSM_ID ,\n" +
                    "TSM_NAME ,\n" +
                    "TSM_ACTIVE_STATUS ,\n" +
                    "TSM_DETAILS  from time_slot_manager\n" +
                    "where TSM_ACTIVE_STATUS = 1\n" +
                    "order by tsm_id");

            while (resultSet.next()) {
                timeSlotLists.add(new TimeSlotList(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4)));
            }
            resultSet.close();


            if (div_id != null) {
                if (!div_id.isEmpty()) {
                    ResultSet rs = stmt.executeQuery("Select DD_ID, DD_THANA_NAME, DD_DIST_ID from district_dtl where DD_ACTIVE_FLAG = 1 \n" +
                            "AND DD_DIST_ID IN (SELECT DIST_ID FROM DISTRICT WHERE DIST_DIV_ID = "+div_id+" AND DIST_ACTIVE_FLAG = 1) order by dd_id");

                    while (rs.next()) {
                        upazilaLists.add(new ThanaList(rs.getString(1),rs.getString(2),rs.getString(3)));
                    }
                    rs.close();
                }
            }

            if (dd_id != null) {
                if (!dd_id.isEmpty()) {
                    String ddff = "";
                    String dpcm_id = "";

                    ResultSet rs = stmt.executeQuery("SELECT ITEM_PACK.ITEM_ECOM_DELIVERY_CHARGE(null, SYSDATE ,null ,"+dd_id+") FROM DUAL");
                    while (rs.next()) {

                        ddff = rs.getString(1);
                    }

                    if (ddff != null) {
                        if (!ddff.isEmpty()) {
                            deliveryReal_fee = Integer.parseInt(ddff);
                        }
                        else {
                            deliveryReal_fee = 0;
                        }
                    }
                    else {
                        deliveryReal_fee = 0;
                    }

                    rs.close();

                    ResultSet resultSet2 = stmt.executeQuery("Select DPCM_ID FROM DELIVERY_PROMO_CHARGE_MST WHERE DPCM_DIV_ID = "+div_id+" AND TRUNC(SYSDATE) BETWEEN TRUNC(DPCM_PROMO_EFFECTIVE_DATE) AND TRUNC(DPCM_PROMO_EFECTIVE_TO_DATE)");

                    while (resultSet2.next()) {

                        dpcm_id = resultSet2.getString(1);
                    }
                    resultSet2.close();

                    if (dpcm_id != null) {
                        if (!dpcm_id.isEmpty()) {
                            deliveryDiscount = true;
                            ResultSet resultSet1 = stmt.executeQuery("SELECT DPCD_ID, DPCD_DPCM_ID, DPCD_INVOICE_COMP_FROM_AMT, DPCD_DISCOUNT_TYPE, DPCD_DISCOUNT_VALUE, DPCD_INVOICE_COMP_TO_AMT\n" +
                                    "FROM DELIVERY_PROMO_CHARGE_DTL\n" +
                                    "WHERE DPCD_DPCM_ID = "+dpcm_id+"\n" +
                                    "AND "+sub_total+" BETWEEN DPCD_INVOICE_COMP_FROM_AMT AND DPCD_INVOICE_COMP_TO_AMT");

                            while (resultSet1.next()) {

                                deliveryDisType = resultSet1.getString(4);
                                deliveryDisValue = resultSet1.getString(5);
                            }
                            resultSet1.close();

                        }
                        else {
                            deliveryDiscount = false;
                        }
                    }
                    else {
                        deliveryDiscount = false;
                    }
                }
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

            upazilaLists = new ArrayList<>();

            Statement stmt = connection.createStatement();

//            if (dist_id != null) {
//                if (dist_id.isEmpty()) {
//                    dist_id = null;
//                }
//            }

//            ResultSet rs = stmt.executeQuery("SELECT DISTRICT_DTL.DD_ID PCU_DD_ID, DISTRICT_DTL.DD_THANA_NAME THANA_UPOZILLA \n" +
//                    "FROM DISTRICT_DTL \n" +
//                    "WHERE DD_DIST_ID = "+dist_id+" \n" +
//                    "AND NVL(DD_ACTIVE_FLAG,0)=1 \n" +
//                    "ORDER BY DISTRICT_DTL.DD_THANA_NAME ASC");

            ResultSet rs = stmt.executeQuery("Select DD_ID, DD_THANA_NAME, DD_DIST_ID from district_dtl where DD_ACTIVE_FLAG = 1 \n" +
                    "AND DD_DIST_ID IN (SELECT DIST_ID FROM DISTRICT WHERE DIST_DIV_ID = "+div_id+" AND DIST_ACTIVE_FLAG = 1) order by dd_id");

            while (rs.next()) {
                upazilaLists.add(new ThanaList(rs.getString(1),rs.getString(2),rs.getString(3)));
            }

//            if (dist_id == null) {
//                dist_id = "";
//            }

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

    public void DeliveryFeeQuery () {

        try {
            this.connection = createConnection();

            delivery_fee = 0;
            deliveryReal_fee = 0;
            deliveryDiscount = false;

            String ddff = "";
            String dpcm_id = "";

            Statement stmt = connection.createStatement();

//            ResultSet rs = stmt.executeQuery("SELECT DDDC_DELIVERY_CHARGE FROM DISTRICT_DTL_DELIVERY_CHARGE\n" +
//                    "WHERE DDDC_DD_ID = "+dd_id+"\n" +
//                    "AND DDDC_EFFECTIVE_DATE = (SELECT MAX(DDDC_EFFECTIVE_DATE) FROM  DISTRICT_DTL_DELIVERY_CHARGE WHERE DDDC_DD_ID = "+dd_id+" and DDDC_EFFECTIVE_DATE < SYSDATE)");

            ResultSet rs = stmt.executeQuery("SELECT ITEM_PACK.ITEM_ECOM_DELIVERY_CHARGE(null, SYSDATE ,null ,"+dd_id+") FROM DUAL");
            while (rs.next()) {

                ddff = rs.getString(1);
            }

            if (ddff != null) {
                if (!ddff.isEmpty()) {
                    deliveryReal_fee = Integer.parseInt(ddff);
                }
                else {
                    deliveryReal_fee = 0;
                }
            }
            else {
                deliveryReal_fee = 0;
            }

            rs.close();

            ResultSet resultSet = stmt.executeQuery("Select DPCM_ID FROM DELIVERY_PROMO_CHARGE_MST WHERE DPCM_DIV_ID = "+div_id+" AND TRUNC(SYSDATE) BETWEEN TRUNC(DPCM_PROMO_EFFECTIVE_DATE) AND TRUNC(DPCM_PROMO_EFECTIVE_TO_DATE)");

            while (resultSet.next()) {

                dpcm_id = resultSet.getString(1);
            }
            resultSet.close();

            if (dpcm_id != null) {
                if (!dpcm_id.isEmpty()) {
                    deliveryDiscount = true;
                    ResultSet resultSet1 = stmt.executeQuery("SELECT DPCD_ID, DPCD_DPCM_ID, DPCD_INVOICE_COMP_FROM_AMT, DPCD_DISCOUNT_TYPE, DPCD_DISCOUNT_VALUE, DPCD_INVOICE_COMP_TO_AMT\n" +
                            "FROM DELIVERY_PROMO_CHARGE_DTL\n" +
                            "WHERE DPCD_DPCM_ID = "+dpcm_id+"\n" +
                            "AND "+sub_total+" BETWEEN DPCD_INVOICE_COMP_FROM_AMT AND DPCD_INVOICE_COMP_TO_AMT");

                    while (resultSet1.next()) {

                        deliveryDisType = resultSet1.getString(4);
                        deliveryDisValue = resultSet1.getString(5);
                    }
                    resultSet1.close();

                }
                else {
                    deliveryDiscount = false;
                }
            }
            else {
                deliveryDiscount = false;
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

    public void OrderQuery () {

        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            Statement stmt = connection.createStatement();

            String som_id = "";
            String som_order_no_id = "";
            String V_A = "";
            String som_order_no = "";
            String ad_id = userInfoLists.get(0).getAd_id();
            String phone = userInfoLists.get(0).getAd_phone();
            String client_aad_id = "";
            stockReached = false;
            stockReachedItemName = "";
            stockLimit = "";

            for (int x = 0; x < myCartList.size(); x++) {
                String item_id = myCartList.get(x).getIem_item_id();
                String stock = "";
                String qty = myCartList.get(x).getItem_qty();
                ResultSet checkingStock = stmt.executeQuery("SELECT (ITEM_PACK.GET_ITEM_STOCK_QTY(SOD_ITEM_ID,SOD_COLOR_ID,SOD_SIZE_ID,SYSDATE) - SUM(NVL(SOD_QTY,0)) + SUM(NVL(SOD_SALES_QTY_MARKS,0))) AS STT \n" +
                        "FROM SALES_ORDER_DTL WHERE SOD_ITEM_ID = "+item_id+"\n" +
                        "GROUP BY SOD_ITEM_ID,SOD_COLOR_ID,SOD_SIZE_ID");

                while (checkingStock.next()) {
                    stock = checkingStock.getString(1);
                }
                checkingStock.close();
                int cartQty = Integer.parseInt(qty);
                int stockQty = Integer.parseInt(stock);
                if (cartQty > stockQty) {
                    stockReached = true;
                    stockReachedItemName = myCartList.get(x).getIem_name();
                    stockLimit = String.valueOf(stockQty);
                    break;
                }
            }

            if (!stockReached) {
                ResultSet rs = stmt.executeQuery("SELECT NVL(MAX(SOM_ID),0)+1\n" +
                        "FROM SALES_ORDER_MST");

                while (rs.next()) {
                    som_id = rs.getString(1);
                }
                rs.close();

                ResultSet rs1 = stmt.executeQuery("SELECT NVL(MAX(SOM_ORDER_NO_ID),0)+1\n" +
                        "FROM SALES_ORDER_MST\n" +
                        "WHERE TO_CHAR(SALES_ORDER_MST.SOM_TIME,'RR')=TO_CHAR(SYSDATE,'RR')");

                while (rs1.next()) {
                    som_order_no_id = rs1.getString(1);
                }
                rs1.close();

                ResultSet rs2 = stmt.executeQuery("SELECT COM_PACK.GET_SMDL_SHORT_CODE_CID('SALES_ORDER',1,'/') FROM DUAL");

                while (rs2.next()) {
                    V_A = rs2.getString(1);
                }
                rs2.close();

                ResultSet rs3 = stmt.executeQuery("SELECT '"+V_A+"'||LTRIM(TO_CHAR(SYSDATE,'RR'))||'/'||LTRIM(TO_CHAR("+som_order_no_id+",'000000')) FROM DUAL");

                while (rs3.next()) {
                    som_order_no = rs3.getString(1);
                    order_no = som_order_no;
                }
                rs3.close();

                ResultSet rs4 = stmt.executeQuery("select ACC_ATTENTION_DTL.AAD_ID\n" +
                        "from ACC_ATTENTION_DTL , ACC_DTL\n" +
                        "where  ACC_ATTENTION_DTL.AAD_AD_ID = ACC_DTL.AD_ID\n" +
                        "AND AAD_ECOMMERCE_FLAG = 1\n" +
                        "and AAD_ACTIVE_CON_FLAG = 1\n" +
                        "AND upper(NVL(ACC_DTL.AD_PHONE,ACC_DTL.AD_EMAIL)) = upper('"+phone+"')");

                while (rs4.next()) {
                    client_aad_id = rs4.getString(1);
                }
                rs4.close();

                stmt.executeUpdate("INSERT INTO SALES_ORDER_MST(SOM_ID, SOM_DATE, SOM_ORDER_NO, SOM_ORDER_NO_ID, SOM_USER, SOM_POST_FLAG,\n" +
                        "SOM_DELIVERY_ADDRESS, SOM_ORDER_TYPE,SOM_TIME,SOM_AD_ID,SOM_DELIVERY_TARGET,SOM_REMARKS,SOM_ADTD_ID,\n" +
                        "SOM_DIV_ID, SOM_DIST_ID, SOM_DD_ID,SOM_CID_ID,SOM_ORDER_FLAG, SOM_SESSION_ID,\n" +
                        "SOM_ORDER_TRACKING,SOM_EDD, SOM_CLIENT_AAD_ID,SOM_DELIVERY_CHARGE,SOM_TSM_ID)\n" +
                        "VALUES ( "+som_id+", SYSDATE,'"+som_order_no+"', "+som_order_no_id+", 'APPS',NULL,'"+delivery_address+"', 1,sysdate, "+ad_id+",\n" +
                        "3,null,null,\n" +
                        ""+div_id+","+dist_id+","+dd_id+",1,2,null,1,TO_DATE('"+expected_d_date+"'),"+client_aad_id+","+delivery_fee+","+tsm_id+")");

                boolean dtls_entry = false;
                for (int i =0 ; i < myCartList.size(); i++) {

                    dtls_entry = false;

                    String sod_id = "";
                    ResultSet rs5 = stmt.executeQuery("SELECT NVL(MAX(SOD_ID),0)+1 FROM SALES_ORDER_DTL");
                    while (rs5.next()) {
                        sod_id = rs5.getString(1);
                    }
                    rs5.close();

                    int value = 0;
                    String disV = myCartList.get(i).getDiscount_value();
                    if (disV != null) {
                        if (!disV.isEmpty()) {
                            value = Integer.parseInt(disV);
                        }
                    }

                    String disT = myCartList.get(i).getDiscount_type();
                    if (disT != null) {
                        if (!disT.isEmpty()) {
                            disT = disT;
                        }
                        else {
                            disT = "";
                        }
                    }
                    else {
                        disT = "";
                    }
                    stmt.executeUpdate("INSERT INTO  SALES_ORDER_DTL(SOD_ID, SOD_SOM_ID, SOD_ITEM_ID,SOD_QTY,SOD_COLOR_ID,SOD_SIZE_ID,\n" +
                            "SOD_DEFINE_RATE, SOD_ORDER_RATE, SOD_DISCOUNT_VALUE, SOD_DISCOUNT_TYPE )\n" +
                            "VALUES ("+sod_id+", "+som_id+", "+myCartList.get(i).getIem_item_id()+", "+myCartList.get(i).getItem_qty()+",1,1,"+myCartList.get(i).getReal_price()+",\n" +
                            ""+myCartList.get(i).getReal_price()+","+value+", '"+disT+"')");

                    dtls_entry = true;

                }

                if (dtls_entry) {
                    stmt.executeUpdate("UPDATE SALES_ORDER_MST SET SOM_ORDER_TRACKING = 2 WHERE SOM_ID = "+som_id+"");
                }
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