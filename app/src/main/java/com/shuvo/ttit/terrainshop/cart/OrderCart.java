package com.shuvo.ttit.terrainshop.cart;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.cart.adapters.CartAdapter;
import com.shuvo.ttit.terrainshop.cart.lists.CartItemList;
import com.shuvo.ttit.terrainshop.checkout.CheckOut;
import com.shuvo.ttit.terrainshop.homepage.HomePage;
import com.shuvo.ttit.terrainshop.login.Login;

import static com.shuvo.ttit.terrainshop.login.Login.userInfoLists;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class OrderCart extends AppCompatActivity implements CartAdapter.ClickedItem {

    public static ArrayList<CartItemList> myCartList = new ArrayList<>();

    RecyclerView cartView;
    public static CartAdapter cartAdapter;
    RecyclerView.LayoutManager layoutManager;

    RelativeLayout cartActionBar;
    static TextView totalPriceAll;

    static int total_price_all = 0;

    static MaterialButton checkOut;

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_cart);

        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.back_color));

        context = getApplicationContext();

        cartView = findViewById(R.id.my_cart_list_view);
        cartActionBar = findViewById(R.id.my_cart_action_bar);
        totalPriceAll = findViewById(R.id.total_price_of_all_item_cart);
        checkOut = findViewById(R.id.check_out_button_cart);

        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setRemoveDuration(600);
        cartView.setItemAnimator(new SlideInLeftAnimator());

        cartView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        cartView.setLayoutManager(layoutManager);
        cartAdapter = new CartAdapter(this,myCartList,this);
        cartView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        cartActionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userInfoLists != null) {
                    if (userInfoLists.size() != 0) {
                        System.out.println(userInfoLists.get(0).getAd_name());
                        Intent intent = new Intent(OrderCart.this, CheckOut.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        System.out.println("NO USER");
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(OrderCart.this,R.style.Theme_MyApp_Dialog_Alert);
                        builder.setTitle("LOGIN ALERT!")
                                .setMessage("You need to get login in the app for proceeding further")
                                .setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(OrderCart.this, Login.class);
                                        startActivity(intent);

                                    }
                                })
                                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                }
                else {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(OrderCart.this,R.style.Theme_MyApp_Dialog_Alert);
                        builder.setTitle("LOGIN ALERT!")
                                .setMessage("You need to get login in the app for proceeding further")
                                .setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(OrderCart.this, Login.class);
                                        startActivity(intent);

                                    }
                                })
                                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                }

            }
        });


    }

    public static void TotalPriceCheck() {
        total_price_all = 0;
        for (int i = 0 ; i < myCartList.size(); i++) {
            total_price_all = total_price_all + Integer.parseInt(myCartList.get(i).getItem_total_price());
        }

        String tpa = "৳ " + String.valueOf(total_price_all);
        totalPriceAll.setText(tpa);

        if (myCartList.size() == 0) {
            checkOut.setEnabled(false);
            checkOut.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.disabled));
        } else {
            checkOut.setEnabled(true);
            checkOut.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.dark_yellow));
        }

    }

    @Override
    protected void onResume() {
        TotalPriceCheck();
        super.onResume();
    }

    @Override
    public void onCategoryClicked(int CategoryPosition) {

    }
}