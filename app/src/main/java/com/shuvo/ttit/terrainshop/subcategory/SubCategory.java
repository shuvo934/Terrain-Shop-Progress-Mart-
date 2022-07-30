package com.shuvo.ttit.terrainshop.subcategory;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.view.Menu;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.cart.OrderCart;
import com.shuvo.ttit.terrainshop.homepage.HomePage;
import com.shuvo.ttit.terrainshop.homepage.adapters.CategoryAdapter;
import com.shuvo.ttit.terrainshop.homepage.fragments.NewArrivalItem;
import com.shuvo.ttit.terrainshop.homepage.lists.CategoryList;
import com.shuvo.ttit.terrainshop.products.Products;
import com.shuvo.ttit.terrainshop.subcategory.adapters.SubCategoryAdapter;
import com.shuvo.ttit.terrainshop.subcategory.lists.SubCategoryList;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static com.shuvo.ttit.terrainshop.cart.OrderCart.myCartList;
import static com.shuvo.ttit.terrainshop.connection.OracleConnection.createConnection;

public class SubCategory extends AppCompatActivity implements SubCategoryAdapter.ClickedItem{


    RecyclerView subView;
    SubCategoryAdapter subCategoryAdapter;
    ArrayList<SubCategoryList> subCategoryLists;

    CircularProgressIndicator circularProgressIndicator;

    private Boolean conn = false;
    private Boolean connected = false;

    String iem_id = "";
    String iem_name = "";

    TextView categoryName;

    ImageView backLogo;

    FrameLayout frameLayout;
    public static TextView cartBadgeSubCategory;
    private AsyncTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        subView = findViewById(R.id.sub_category_list_view);
        circularProgressIndicator = findViewById(R.id.progress_indicator_sub_category);
        circularProgressIndicator.setVisibility(View.GONE);
        categoryName = findViewById(R.id.categories_name_in_sub_category);
        backLogo = findViewById(R.id.back_logo_sub_category);
        frameLayout = findViewById(R.id.cart_layout_in_subcategory);
        cartBadgeSubCategory = findViewById(R.id.cart_badge_in_sub_category);
        cartBadgeSubCategory.setVisibility(View.GONE);

        subCategoryLists = new ArrayList<>();

        Intent intent = getIntent();
        iem_id = intent.getStringExtra("IEM ID");
        iem_name = intent.getStringExtra("IEM NAME");

        iem_name = iem_name.toUpperCase();

        categoryName.setText(iem_name);

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SubCategory.this, OrderCart.class);
                startActivity(intent);
            }
        });

        backLogo.setOnClickListener(new View.OnClickListener() {
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

    public static void CartItemCheckSub() {
        int count = myCartList.size();
        if (count == 0) {
            if (cartBadgeSubCategory.getVisibility() != View.GONE) {
                Log.i("assa","bujhlam2");

                cartBadgeSubCategory.setVisibility(View.GONE);
            }
        }
        else {
            cartBadgeSubCategory.setText(String.valueOf(Math.min(count, 99)));
            Log.i("assa","bujhlam3");

            if (cartBadgeSubCategory.getVisibility() != View.VISIBLE) {
                Log.i("assa","bujhlam4");

                cartBadgeSubCategory.setVisibility(View.VISIBLE);
            }
        }
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
    protected void onResume() {
        CartItemCheckSub();
        super.onResume();
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
        if (subCategoryLists.get(CategoryPosition).getSubBelow() != null) {
            if (subCategoryLists.get(CategoryPosition).getSubBelow().equals("3")) {
                Intent intent = new Intent(SubCategory.this, Products.class);
                intent.putExtra("IEM ID", subCategoryLists.get(CategoryPosition).getIem_id());
                intent.putExtra("IEM NAME", subCategoryLists.get(CategoryPosition).getIem_name());
                startActivity(intent);
            }
        }
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

                CheckAllData();
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


                GridLayoutManager layoutManager = new GridLayoutManager(SubCategory.this, 2);

                layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if (subCategoryLists.size() % 2 != 0) {
                            if (position == subCategoryLists.size() -1) {
                                return 1;
                            }
                            else {
                                return 1;
                            }
                        } else  {
                            return 1;
                        }
                    }
                });

                subView.setLayoutManager(layoutManager);

                subCategoryAdapter = new SubCategoryAdapter(subCategoryLists,SubCategory.this,SubCategory.this);
                subView.setAdapter(subCategoryAdapter);

                conn = false;
                connected = false;

            } else {

                //Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(SubCategory.this)
                        .setMessage("Slow Internet or Please Check Your Internet Connection")
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

    public void CheckAllData () {

        try {
            Connection connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            subCategoryLists = new ArrayList<>();
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("select IEM_ID, IEM_IEM_ID, IEM_TYPE, IEM_NAME, IEM_THUMB,\n" +
                    "(Select AVG(iem_type) from ITEM_ECOM_MST A where A.iem_iem_id = B.IEM_ID) as below\n" +
                    "from ITEM_ECOM_MST B where iem_iem_id = "+iem_id+"\n" +
                    "                    order by IEM_ID");

            while (rs.next()) {
                Blob b=rs.getBlob(5);
                byte[] barr =b.getBytes(1,(int)b.length());
                Bitmap bitmap = BitmapFactory.decodeByteArray(barr,0,barr.length);
                subCategoryLists.add(new SubCategoryList(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),bitmap,rs.getString(6)));
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