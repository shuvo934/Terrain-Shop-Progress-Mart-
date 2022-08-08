package com.shuvo.ttit.terrainshop.adminlogin.reports.reorder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rosemaryapp.amazingspinner.AmazingSpinner;
import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.adminlogin.reports.reorder.adapters.ReorderAdapter;
import com.shuvo.ttit.terrainshop.adminlogin.reports.reorder.lists.ReorderList;
import com.shuvo.ttit.terrainshop.adminlogin.reports.topnitem.TopNItem;
import com.shuvo.ttit.terrainshop.adminlogin.reports.topnitem.adapters.TopNItemAdapter;
import com.shuvo.ttit.terrainshop.adminlogin.reports.topnitem.lists.ChoiceList;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;

import oracle.jdbc.driver.OracleTypes;

import static com.shuvo.ttit.terrainshop.connection.OracleConnection.createConnection;

public class ReOrder extends AppCompatActivity implements ReorderAdapter.ClickedItem{

    RelativeLayout reOrderBar;

    RelativeLayout fullLayout;
    CircularProgressIndicator circularProgressIndicator;

    private Boolean conn = false;
    private Boolean connected = false;
    Boolean isfiltered = false;

    Connection connection;
    private AsyncTask mTask;

    AmazingSpinner categorySpinner;
    AmazingSpinner subCatSpinner;
    TextInputLayout sublay;

    ArrayList<ChoiceList> categoryLists;
    ArrayList<ChoiceList> subCategoryLists;

    LinearLayout afterSubCatSelect;

    TextInputEditText searchItem;
    TextInputLayout searchItemLay;
    RecyclerView itemView;
    ReorderAdapter itemAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<ReorderList> reorderItemLists;
    ArrayList<ReorderList> filteredList;

    String categoryId = "";
    String subCategoryId = "";
    String catName = "";
    String subCatName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_order);

        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.back_color));

        reOrderBar = findViewById(R.id.re_order_action_bar);
        fullLayout = findViewById(R.id.re_order_design_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_re_order);
        circularProgressIndicator.setVisibility(View.GONE);

        afterSubCatSelect = findViewById(R.id.after_selecting_sub_category_re_order);
        afterSubCatSelect.setVisibility(View.GONE);

        categorySpinner = findViewById(R.id.cat_type_spinner_re_order);
        subCatSpinner = findViewById(R.id.sub_cat_type_spinner_re_order);
        sublay = findViewById(R.id.spinner_layout_sub_cat_re_order);
        sublay.setEnabled(false);

        searchItem = findViewById(R.id.search_item_name_re_order);
        searchItemLay = findViewById(R.id.search_item_name_lay_re_order);
        itemView = findViewById(R.id.item_overview_relation_re_order);

        categoryLists = new ArrayList<>();
        subCategoryLists = new ArrayList<>();
        reorderItemLists = new ArrayList<>();
        filteredList = new ArrayList<>();

        itemView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        itemView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(itemView.getContext(),DividerItemDecoration.VERTICAL);
        itemView.addItemDecoration(dividerItemDecoration);

        // Selecting Category
        categorySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                sublay.setEnabled(false);
                categoryId = "";
                catName = "";
                subCatName = "";
                subCategoryId = "";
                subCatSpinner.setText("");
                String name = parent.getItemAtPosition(position).toString();
                System.out.println(position+": "+name);
                for (int i = 0; i <categoryLists.size(); i++) {
                    if (name.equals(categoryLists.get(i).getType())) {
                        categoryId = categoryLists.get(i).getId();
                    }
                }
                System.out.println(categoryId);

                mTask = new SubWithItemCheck().execute();
                afterSubCatSelect.setVisibility(View.GONE);

            }
        });

        // Selecting Sub Category
        subCatSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                subCatName = "";
                subCategoryId = "";
                String name = parent.getItemAtPosition(position).toString();
                System.out.println(position+": "+name);
                for (int i = 0; i <subCategoryLists.size(); i++) {
                    if (name.equals(subCategoryLists.get(i).getType())) {
                        subCategoryId = subCategoryLists.get(i).getId();
                        if (subCategoryId.isEmpty()) {
                            subCatName = "";
                        }
                        else {
                            subCatName = subCategoryLists.get(i).getType();
                        }
                    }
                }

                if (name.equals("...")) {
                    subCatSpinner.setText("");
                }
                System.out.println(subCategoryId);
                mTask = new ItemCheck().execute();
            }
        });

        searchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());
            }
        });

        searchItem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        Log.i("Let see", "Come here");
                        closeKeyBoard();



                        return false; // consume.
                    }
                }
                return false;
            }
        });


        reOrderBar.setOnClickListener(new View.OnClickListener() {
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

    private void closeKeyBoard () {
        View view = this.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void filter(String text) {

        filteredList = new ArrayList<>();
        for (ReorderList item : reorderItemLists) {
            if (item.getItemName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add((item));
                isfiltered = true;
            }
        }
        itemAdapter.filterList(filteredList);
    }

    @Override
    public void onCategoryClicked(int CategoryPosition) {
        
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

                CategoryData();
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

                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < categoryLists.size(); i++) {
                    type.add(categoryLists.get(i).getType());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ReOrder.this,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                categorySpinner.setAdapter(arrayAdapter);



            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection or Slow Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(ReOrder.this)
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
    public class SubWithItemCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            circularProgressIndicator.setVisibility(View.VISIBLE);
            fullLayout.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                SubCategoryWithItemData();
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
                isfiltered = false;
                sublay.setEnabled(true);

                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < subCategoryLists.size(); i++) {
                    type.add(subCategoryLists.get(i).getType());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ReOrder.this,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                subCatSpinner.setAdapter(arrayAdapter);

                afterSubCatSelect.setVisibility(View.VISIBLE);

                itemAdapter = new ReorderAdapter(reorderItemLists, ReOrder.this,ReOrder.this);
                itemView.setAdapter(itemAdapter);
                searchItem.setText("");

                searchItem.clearFocus();



            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection or Slow Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(ReOrder.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mTask = new SubWithItemCheck().execute();
                        dialog.dismiss();
                    }
                });

            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class ItemCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            circularProgressIndicator.setVisibility(View.VISIBLE);
            fullLayout.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                ItemData();
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
                isfiltered = false;

                itemAdapter = new ReorderAdapter(reorderItemLists, ReOrder.this,ReOrder.this);
                itemView.setAdapter(itemAdapter);
                searchItem.setText("");

                searchItem.clearFocus();


            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection or Slow Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(ReOrder.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mTask = new ItemCheck().execute();
                        dialog.dismiss();
                    }
                });

            }
        }
    }

    public void CategoryData() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            Statement stmt = connection.createStatement();

            categoryLists = new ArrayList<>();

            ResultSet resultSet1 = stmt.executeQuery("SELECT IM_ID, IM_NAME FROM ITEM_MST");

            while (resultSet1.next()) {
                categoryLists.add(new ChoiceList(resultSet1.getString(1),resultSet1.getString(2)));
            }
            resultSet1.close();

            connected = true;

            connection.close();

        }
        catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void SubCategoryWithItemData() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            Statement stmt = connection.createStatement();

            subCategoryLists = new ArrayList<>();
            reorderItemLists = new ArrayList<>();

            if (categoryId.isEmpty()) {
                categoryId = null;
            }
            if (subCategoryId.isEmpty()) {
                subCategoryId = null;
            }

            subCategoryLists.add(new ChoiceList("","..."));
            ResultSet resultSet1 = stmt.executeQuery("SELECT ITEM_SUBCAT.ISC_ID, ITEM_SUBCAT.ISC_SUBCATM_ID, SUBCAT_MST.SUBCATM_NAME FROM ITEM_SUBCAT, SUBCAT_MST\n" +
                    "WHERE ITEM_SUBCAT.ISC_SUBCATM_ID = SUBCAT_MST.SUBCATM_ID\n" +
                    "AND ITEM_SUBCAT.ISC_IM_ID = "+categoryId+"\n" +
                    "order by ITEM_SUBCAT.ISC_ID");

            while (resultSet1.next()) {
                subCategoryLists.add(new ChoiceList(resultSet1.getString(1),resultSet1.getString(3)));
            }
            resultSet1.close();

            CallableStatement callableStatement = connection.prepareCall("begin ? := GET_ITEM_REORDER_LEVEL(?,?); end;");
            callableStatement.setString(2,(categoryId));
            callableStatement.setString(3,(subCategoryId));
            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.execute();

            ResultSet resultSet = (ResultSet) callableStatement.getObject(1);

            while (resultSet.next()) {
                reorderItemLists.add(new ReorderList("Re-Order",resultSet.getString(1),resultSet.getString(2),
                        resultSet.getString(3),resultSet.getString(6),resultSet.getString(7)));
            }
            resultSet.close();

            callableStatement.close();


            if (categoryId == null) {
                subCategoryLists = new ArrayList<>();
                categoryId = "";
            }
            if (subCategoryId == null) {
                subCategoryId = "";
            }

            connected = true;

            connection.close();

        }
        catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void ItemData() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            Statement stmt = connection.createStatement();

            reorderItemLists = new ArrayList<>();

            if (categoryId.isEmpty()) {
                categoryId = null;
            }
            if (subCategoryId.isEmpty()) {
                subCategoryId = null;
            }


            CallableStatement callableStatement = connection.prepareCall("begin ? := GET_ITEM_REORDER_LEVEL(?,?); end;");
            callableStatement.setString(2,(categoryId));
            callableStatement.setString(3,(subCategoryId));
            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.execute();

            ResultSet resultSet = (ResultSet) callableStatement.getObject(1);

            while (resultSet.next()) {
                reorderItemLists.add(new ReorderList("Re-Order",resultSet.getString(1),resultSet.getString(2),
                        resultSet.getString(3),resultSet.getString(6),resultSet.getString(7)));
            }
            resultSet.close();

            callableStatement.close();


            if (categoryId == null) {
                categoryId = "";
            }
            if (subCategoryId == null) {
                subCategoryId = "";
            }

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