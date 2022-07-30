package com.shuvo.ttit.terrainshop.productDetails;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.cart.lists.CartItemList;
import com.shuvo.ttit.terrainshop.productDetails.adapters.ImageAdapter;
import com.shuvo.ttit.terrainshop.productDetails.lists.ImageList;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static com.shuvo.ttit.terrainshop.cart.OrderCart.myCartList;
import static com.shuvo.ttit.terrainshop.connection.OracleConnection.createConnection;

public class ProductDetails extends AppCompatActivity {

    SliderView productImage;
    TextView productName;
    TextView productQty;
    TextView cardPrice;
    TextView realPrice;
    TextView about;
    Button addToCart;
    RelativeLayout actionBar;
    TextView out;
    TextView itemCount;
    MaterialButton addButton;
    MaterialButton removeButton;
    TextView totalPrice;

    String pName = "";
    String card_Price = "";
    String pQty = "";
    String pAbout = "";
    String item_id = "";
    String stock = "";
    String newTag = "";
    Bitmap frontImage;
    String iem_id = "";
    String iem_iem_id = "";
    String itemUnit = "";
    String pNameForCart = "";
    String real_Price = "";
    String disDtl = "";
    String disType = "";
    String disValue = "";
    String stockQty = "";

    ArrayList<ImageList> sliderItems;

    ScrollView productDetailsLay;
    CircularProgressIndicator circularProgressIndicator;

    private Boolean conn = false;
    private Boolean connected = false;
    RelativeLayout addToCartLay;
    CardView addRemoveCard;

    int total_price = 0;
    private AsyncTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.back_color));

        productImage = findViewById(R.id.product_image_in_product_details);
        productName = findViewById(R.id.product_name_in_product_details);
        productQty = findViewById(R.id.product_qty_in_product_details);
        cardPrice = findViewById(R.id.product_card_price_in_product_details);
        realPrice = findViewById(R.id.product_real_price_in_product_details);
        about = findViewById(R.id.about_product_details);
        addToCart = findViewById(R.id.add_to_cart_product_details);
        actionBar = findViewById(R.id.product_details_action_bar);
        productDetailsLay = findViewById(R.id.product_details_layout);
        productDetailsLay.setVisibility(View.GONE);
        circularProgressIndicator = findViewById(R.id.progress_indicator_product_details);
        circularProgressIndicator.setVisibility(View.GONE);
        out = findViewById(R.id.out_of_stock_msg_product_details);
        out.setVisibility(View.GONE);
        addToCartLay = findViewById(R.id.product_details_add_to_cart_layout);
        addToCartLay.setVisibility(View.GONE);
        addRemoveCard = findViewById(R.id.add_remove_item_card_view_pd);
        addRemoveCard.setVisibility(View.GONE);
        itemCount = findViewById(R.id.count_of_total_item_product_details);
        addButton = findViewById(R.id.add_item_button_product_details);
        removeButton = findViewById(R.id.remove_item_button_product_details);
        totalPrice = findViewById(R.id.total_price_to_qty_product_details);

        sliderItems = new ArrayList<>();


//        productImage.startAutoCycle();

        actionBar.setOnClickListener(new View.OnClickListener() {
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

        Intent intent = getIntent();

        pName = intent.getStringExtra("PRODUCT NAME");
        card_Price = intent.getStringExtra("CARD RATE PRICE");
        pQty = intent.getStringExtra("PRODUCT QTY");
        pAbout = intent.getStringExtra("PRODUCT DETAILS");
        item_id = intent.getStringExtra("ITEM ID");
        stock = intent.getStringExtra("STOCK MSG");
        newTag = intent.getStringExtra("NEW TAG");

        String filename = intent.getStringExtra("PRODUCT IMAGE");

//        byte[] byteArray = getIntent().getByteArrayExtra("PRODUCT IMAGE");
//        frontImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        try {
            FileInputStream is = this.openFileInput(filename);
            frontImage = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //frontImage = (Bitmap) intent.getParcelableExtra("");
        iem_id = intent.getStringExtra("IEM ID");
        iem_iem_id = intent.getStringExtra("IEM IEM ID");
        itemUnit = intent.getStringExtra("ITEM UNIT");
        real_Price = intent.getStringExtra("REAL RATE PRICE");
        disDtl = intent.getStringExtra("DISCOUNT DTL");
        disType = intent.getStringExtra("DISCOUNT TYPE");
        disValue = intent.getStringExtra("DISCOUNT VALUE");
        stockQty = intent.getStringExtra("STOCK QTY");

        pNameForCart = pName;

        if (newTag.equals("NEW")) {
            pName = pName + " (NEW)";
        }
        if (disDtl != null) {
            if (!disDtl.isEmpty()) {
                if (disDtl.contains("%")) {
                    pName = pName + "\n("+disDtl+" off)";
                }
                else {
                    pName = pName + "\n(৳ "+disDtl+" off)";
                }
            }
        }

        SpannableStringBuilder ss1 = new SpannableStringBuilder(pName);
        if (pName.contains("(NEW)") ) {

            for (int i = -1; (i = pName.indexOf("(NEW)", i + 1)) != -1; i++) {
                //System.out.println(i);
                ss1.setSpan(new ForegroundColorSpan(Color.rgb(192, 57, 43)), i, i + 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss1.setSpan(new RelativeSizeSpan(0.7f),i,i+5,0);
            }
        }
        if (pName.contains(" off)") ) {
            int firstIndex = pName.lastIndexOf("(");
            int lastIndex = pName.lastIndexOf(")");

            ss1.setSpan(new ForegroundColorSpan(Color.rgb(192, 57, 43)), firstIndex, lastIndex+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss1.setSpan(new RelativeSizeSpan(0.7f),firstIndex,lastIndex+1,0);

        }

        productName.setText(ss1);
        if (disDtl != null) {
            if (!disDtl.isEmpty()) {
                realPrice.setVisibility(View.VISIBLE);
                String realP = "৳ " + real_Price;
                realPrice.setText(realP);
                realPrice.setPaintFlags(realPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            else {
                realPrice.setVisibility(View.GONE);
            }
        }
        else {
            realPrice.setVisibility(View.GONE);
        }

        String cardP = "৳ " + card_Price;

        cardPrice.setText(cardP);

        String packQty = "";
        if (pQty.equals(" ")) {
            productQty.setVisibility(View.VISIBLE);
            packQty = "1 " + itemUnit;
            productQty.setText(packQty);
        }
        else {
            productQty.setVisibility(View.VISIBLE);
            packQty = "1 " + itemUnit + " ( " + pQty + " )";
            productQty.setText(packQty);
        }

        if (pAbout.isEmpty()) {
            about.setText("No Information Found");
        }
        else {
            about.setText(pAbout);
        }

        itemCount.setText("1");
        if (itemCount.getText().equals("1")) {
            removeButton.setBackgroundColor(Color.parseColor("#b2bec3"));
        }
        else {
            removeButton.setBackgroundColor(Color.parseColor("#e1b12c"));
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = itemCount.getText().toString();
                int num = Integer.parseInt(text);
                int add = num + 1;
                itemCount.setText(String.valueOf(add));
                if (add > 1) {
                    removeButton.setBackgroundColor(Color.parseColor("#e1b12c"));
                }
                PriceCheck();
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = itemCount.getText().toString();
                int num = Integer.parseInt(text);

                if (num > 1) {
                    int minus = num -1;
                    itemCount.setText(String.valueOf(minus));
                    if (minus == 1) {
                        removeButton.setBackgroundColor(Color.parseColor("#b2bec3"));
                    }
                    else {
                        removeButton.setBackgroundColor(Color.parseColor("#e1b12c"));
                    }
                    PriceCheck();
                }
            }
        });

        total_price = Integer.parseInt(itemCount.getText().toString()) * Integer.parseInt(card_Price);
        totalPrice.setText("৳ "+ total_price);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int a = 0;
                int b = 0;
                for (int i = 0 ; i < myCartList.size(); i++) {
                    if (item_id.equals(myCartList.get(i).getIem_item_id())) {
                        String qty = myCartList.get(i).getItem_qty();
                        int qq = Integer.parseInt(qty);
                        if (stockQty == null) {
                            stockQty = "0";
                            if (stockQty.isEmpty()) {
                                stockQty = "0";
                            }
                        }
                        int stq = Integer.parseInt(stockQty);
                        if (stq > qq) {
                            int newQQ = Integer.parseInt(itemCount.getText().toString());
                            qq = qq + newQQ;
                            if (stq >= qq) {
                                int total = qq * Integer.parseInt(myCartList.get(i).getCardRate());
                                myCartList.get(i).setItem_total_price(String.valueOf(total));
                                myCartList.get(i).setItem_qty(String.valueOf(qq));
                                myCartList.get(i).setStock_qty(stockQty);
                                b = 1;
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Sorry, stock limit reached",Toast.LENGTH_SHORT).show();
                                myCartList.get(i).setStock_qty(stockQty);
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Sorry, stock limit reached",Toast.LENGTH_SHORT).show();
                            myCartList.get(i).setStock_qty(stockQty);
                        }
                        a = 1;
                    }
                }

                if (a == 0) {
                    if (stockQty == null) {
                        stockQty = "0";
                        if (stockQty.isEmpty()) {
                            stockQty = "0";
                        }
                    }
                    int stq = Integer.parseInt(stockQty);
                    String i_qty = itemCount.getText().toString();
                    int qq = Integer.parseInt(i_qty);
                    if (stq >= qq) {
                        myCartList.add(new CartItemList(iem_id,iem_iem_id,item_id,pNameForCart, card_Price,pQty,itemUnit,frontImage,i_qty,String.valueOf(total_price),real_Price,disType,disValue,stockQty));
                        Toast.makeText(getApplicationContext(),"Item added to Cart",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Sorry, stock limit reached",Toast.LENGTH_SHORT).show();
                    }
                }
                else if (a == 1 && b == 1) {
                    Toast.makeText(getApplicationContext(),"Item added to Cart",Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });

        mTask = new Check().execute();

    }

    private void PriceCheck() {

        total_price = Integer.parseInt(itemCount.getText().toString()) * Integer.parseInt(card_Price);
        totalPrice.setText("৳ "+ total_price);


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
            productDetailsLay.setVisibility(View.GONE);
            addToCartLay.setVisibility(View.GONE);
            addRemoveCard.setVisibility(View.GONE);
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

                productDetailsLay.setVisibility(View.VISIBLE);
                addToCartLay.setVisibility(View.VISIBLE);
                addRemoveCard.setVisibility(View.VISIBLE);

                productImage.setSliderAdapter(new ImageAdapter(ProductDetails.this,sliderItems));
                productImage.setIndicatorAnimation(IndicatorAnimationType.WORM);
                productImage.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);

                if (stock.equals("StockOut")) {
                    out.setVisibility(View.VISIBLE);
                    addToCartLay.setVisibility(View.GONE);
                    addRemoveCard.setVisibility(View.GONE);

                }
                else {
                    out.setVisibility(View.GONE);
                    addToCartLay.setVisibility(View.VISIBLE);
                    addRemoveCard.setVisibility(View.VISIBLE);
                }

                conn = false;
                connected = false;

            } else {
                productDetailsLay.setVisibility(View.GONE);
                addToCartLay.setVisibility(View.GONE);
                addRemoveCard.setVisibility(View.GONE);

                //Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(ProductDetails.this)
                        .setMessage("Slow Internet or Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
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

    public void CheckAllData () {

        try {
            Connection connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            sliderItems = new ArrayList<>();
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("select loadBlobFromFile(substr(idi_file_full_path, (instr(idi_file_full_path,'\\',-1))+1)) AS image_name from item_dtl_image WHERE IDI_ITEM_ID = "+item_id+"");

            while (rs.next()) {
//                String path = rs.getString(1);
//                path = path.substring(path.lastIndexOf("\\")+1);
//                System.out.println(path);
                Blob b=rs.getBlob(1);
                byte[] barr =b.getBytes(1,(int)b.length());
                Bitmap bitmap = BitmapFactory.decodeByteArray(barr,0,barr.length);
                sliderItems.add(new ImageList(bitmap,""));
            }
            rs.close();

            connected = true;

            connection.close();

        } catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }

    }
}