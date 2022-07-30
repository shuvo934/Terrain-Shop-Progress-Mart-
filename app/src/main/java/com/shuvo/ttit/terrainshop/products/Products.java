package com.shuvo.ttit.terrainshop.products;

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
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.cart.OrderCart;
import com.shuvo.ttit.terrainshop.homepage.fragments.lists.NewArrivalItemList;
import com.shuvo.ttit.terrainshop.productDetails.ProductDetails;
import com.shuvo.ttit.terrainshop.products.adapters.ProductAdapter;
import com.shuvo.ttit.terrainshop.products.lists.ProductList;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static com.shuvo.ttit.terrainshop.cart.OrderCart.myCartList;
import static com.shuvo.ttit.terrainshop.connection.OracleConnection.createConnection;

public class Products extends AppCompatActivity implements ProductAdapter.ClickedItem{

    RecyclerView proView;
    ProductAdapter productAdapter;
    ArrayList<ProductList> productLists;

    CircularProgressIndicator circularProgressIndicator;

    TextView noProductMsg;

    private Boolean conn = false;
    private Boolean connected = false;

    String iem_id = "";
    String iem_name = "";

    TextView subCatName;

    ImageView backLogo;

    FrameLayout frameLayout;
    public static TextView cartBadgeProducts;
    private AsyncTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        proView = findViewById(R.id.products_list_view);
        circularProgressIndicator = findViewById(R.id.progress_indicator_products);
        circularProgressIndicator.setVisibility(View.GONE);
        noProductMsg = findViewById(R.id.no_product_msg_products);
        noProductMsg.setVisibility(View.GONE);
        subCatName = findViewById(R.id.sub_categories_name_in_products);
        backLogo = findViewById(R.id.back_logo_products);
        frameLayout = findViewById(R.id.cart_layout_in_products);
        cartBadgeProducts = findViewById(R.id.cart_badge_in_products);
        cartBadgeProducts.setVisibility(View.GONE);

        productLists = new ArrayList<>();

        Intent intent = getIntent();
        iem_id = intent.getStringExtra("IEM ID");
        System.out.println(iem_id);
        iem_name = intent.getStringExtra("IEM NAME");

        iem_name = iem_name.toUpperCase();

        subCatName.setText(iem_name);

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Products.this, OrderCart.class);
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

    public static void CartItemCheckProducts() {
        int count = myCartList.size();
        if (count == 0) {
            if (cartBadgeProducts.getVisibility() != View.GONE) {
                Log.i("assa","bujhlam2");

                cartBadgeProducts.setVisibility(View.GONE);
            }
        }
        else {
            cartBadgeProducts.setText(String.valueOf(Math.min(count, 99)));
            Log.i("assa","bujhlam3");

            if (cartBadgeProducts.getVisibility() != View.VISIBLE) {
                Log.i("assa","bujhlam4");

                cartBadgeProducts.setVisibility(View.VISIBLE);
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
        CartItemCheckProducts();
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

        Bitmap bmp = productLists.get(CategoryPosition).getImage();
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        byte[] byteArray = stream.toByteArray();

        String filename = "bitmap.png";
        try {
            //Write file
            filename = "bitmap.png";
            FileOutputStream stream = openFileOutput(filename, Context.MODE_PRIVATE);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

            //Cleanup
            stream.close();
//            bmp.recycle();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(Products.this, ProductDetails.class);
        intent.putExtra("PRODUCT NAME", productLists.get(CategoryPosition).getIem_name());
        intent.putExtra("CARD RATE PRICE", productLists.get(CategoryPosition).getCard_rate());
        intent.putExtra("PRODUCT QTY", productLists.get(CategoryPosition).getPackaging_unit_qty());
        intent.putExtra("ITEM ID", productLists.get(CategoryPosition).getIem_item_id());
        intent.putExtra("PRODUCT DETAILS", productLists.get(CategoryPosition).getItemDetails());
        intent.putExtra("STOCK MSG", productLists.get(CategoryPosition).getStock_availability());
        intent.putExtra("NEW TAG", productLists.get(CategoryPosition).getNew_tag());

        intent.putExtra("PRODUCT IMAGE", filename);
        intent.putExtra("IEM ID",productLists.get(CategoryPosition).getIem_id());
        intent.putExtra("IEM IEM ID",productLists.get(CategoryPosition).getIem_iem_id());
        intent.putExtra("ITEM UNIT", productLists.get(CategoryPosition).getItem_unit());
        intent.putExtra("REAL RATE PRICE", productLists.get(CategoryPosition).getReal_rate());
        intent.putExtra("DISCOUNT DTL", productLists.get(CategoryPosition).getDiscount_dtl());
        intent.putExtra("DISCOUNT TYPE", productLists.get(CategoryPosition).getDiscount_type());
        intent.putExtra("DISCOUNT VALUE",productLists.get(CategoryPosition).getDiscount_value());
        intent.putExtra("STOCK QTY",productLists.get(CategoryPosition).getStock_qty());
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

                if (productLists.size() != 0) {
                    noProductMsg.setVisibility(View.GONE);
                    GridLayoutManager layoutManager = new GridLayoutManager(Products.this, 2);

                    layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            if (productLists.size() % 2 != 0) {
                                if (position == productLists.size() -1) {
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

                    proView.setLayoutManager(layoutManager);

                    productAdapter = new ProductAdapter(productLists,Products.this,Products.this);
                    proView.setAdapter(productAdapter);
                }
                else {
                    noProductMsg.setVisibility(View.VISIBLE);
                }


                conn = false;
                connected = false;

            } else {

                //Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(Products.this)
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

            productLists = new ArrayList<>();
            Statement stmt = connection.createStatement();

            ResultSet resultSet = stmt.executeQuery("select IEM_ID, IEM_IEM_ID, IEM_TYPE, IEM_NAME, IEM_THUMB,IEM_ITEM_ID,\n" +
                    "                    ITEM_PACK.GET_ITEM_UNIT(IEM_ITEM_ID) as ITM_UNIT,\n" +
                    "                    case when ITEM_PACK.GET_ITEM_PACKAGING_QTY(IEM_ITEM_ID) = 0 then  \n" +
                    "                     '(' ||ITEM_PACK.GET_ITEM_PACKAGING_QTY(IEM_ITEM_ID)||' '||ITEM_PACK.GET_ITEM_PACKAGING_UNIT(IEM_ITEM_ID) ||')' else \n" +
                    "                    ITEM_PACK.GET_ITEM_PACKAGING_QTY(IEM_ITEM_ID)||' '||ITEM_PACK.GET_ITEM_PACKAGING_UNIT(IEM_ITEM_ID) end AS PACKAGING_UNIT_QTY,\n" +
                    "                    CASE WHEN ITEM_PACK.GET_ITEM_SELLING_DISCOUNT(IEM_ITEM_ID) >0 THEN   TO_CHAR(ITEM_PACK.GET_ITEM_SELLING_DISCOUNT(IEM_ITEM_ID)) else\n" +
                    "                    TO_CHAR(ITEM_PACK.GET_ITEM_SELLING_PRICE(IEM_ITEM_ID)) end AS CARD_RATE,\n" +
                    "                    CASE WHEN ITEM_PACK.GET_ITEM_SELLING_DISCOUNT(IEM_ITEM_ID) >0 THEN  \n" +
                    "                    'Price : '||TO_CHAR(ITEM_PACK.GET_ITEM_SELLING_PRICE(IEM_ITEM_ID)) ELSE '' END as CARD_OTHER_INFO, \n" +
                    "                    SUBSTR(ITEM_PACK.GET_ITEM_DISCOUNT_DTL(IEM_ITEM_ID),1,3) as DISCOUNT_DTL,\n" +
                    "                    case when (SELECT DISTINCT IEM.IEM_ITEM_ID FROM ITEM_ECOM_MST IEM WHERE IEM.IEM_ITEM_ID = ITEM_ECOM_MST.IEM_ITEM_ID AND IEM_IEM_ID = 1) \n" +
                    "                        IS NOT NULL THEN 'NEW' ELSE 'N' END AS NEW_ARRIVAL_TAG,\n" +
                    "                    decode(ITEM_PACK.GET_ECOMITEM_PRE_STOCK(IEM_ITEM_ID,NULL,NULL,SYSDATE),0,'StockOut','E') as ECOM_DIS,\n" +
                    "                    TO_CHAR(ITEM_PACK.GET_ITEM_SELLING_PRICE(IEM_ITEM_ID)) as REAL_PRICE,\n" +
                    "                    ITEM_PACK.GET_ITEM_STOCK_QTY(ITEM_ECOM_MST.IEM_ITEM_ID, ITEM_DTL.ITEM_COLOR_ID,ITEM_DTL.ITEM_SIZE_ID,SYSDATE) as STOCK\n" +
                    "                    from ITEM_ECOM_MST,ITEM_DTL\n" +
                    "                    where iem_type in (3)\n" +
                    "                    AND ITEM_ECOM_MST.IEM_ITEM_ID = ITEM_DTL.ITEM_ID\n" +
                    "                    AND IEM_IEM_ID ="+iem_id+"\n" +
                    "                    and TO_CHAR(ITEM_PACK.GET_ITEM_SELLING_PRICE(IEM_ITEM_ID))>0\n" +
                    "                    ORDER by iem_id");

            while (resultSet.next()) {
                Blob b=resultSet.getBlob(5);
                byte[] barr =b.getBytes(1,(int)b.length());
                Bitmap bitmap = BitmapFactory.decodeByteArray(barr,0,barr.length);

                String discount_type = "";
                String discount_value = "";

                String dd = resultSet.getString(11);

                if (dd != null) {
                    if (!dd.isEmpty()) {
                        if (dd.contains("%")) {
                            discount_type = "%";
                            discount_value = dd.substring(0,dd.length()-1);
                        }
                        else {
                            discount_type = "SR";
                            discount_value = dd.substring(0,dd.length()-1);
                        }
                    }
                    else {
                        discount_type = "";
                        discount_value = "";
                    }
                }
                else {
                    discount_type = null;
                    discount_value = null;
                }
                productLists.add(new ProductList(resultSet.getString(1), resultSet.getString(2),resultSet.getString(3),
                        resultSet.getString(4),bitmap,resultSet.getString(6),resultSet.getString(7),resultSet.getString(8),
                        resultSet.getString(9),resultSet.getString(10),resultSet.getString(11),resultSet.getString(12),
                        resultSet.getString(13),resultSet.getString(14),"",discount_type,discount_value,resultSet.getString(15)));
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