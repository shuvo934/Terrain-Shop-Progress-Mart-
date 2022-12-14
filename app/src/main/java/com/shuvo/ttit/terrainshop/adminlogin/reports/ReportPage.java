package com.shuvo.ttit.terrainshop.adminlogin.reports;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.adminlogin.reports.deliverypending.DeliveryPending;
import com.shuvo.ttit.terrainshop.adminlogin.reports.deliveryregister.DeliveryRegister;
import com.shuvo.ttit.terrainshop.adminlogin.reports.itemstock.ItemWiseStock;
import com.shuvo.ttit.terrainshop.adminlogin.reports.reorder.ReOrder;
import com.shuvo.ttit.terrainshop.adminlogin.reports.salesOrder.OrderCollection;
import com.shuvo.ttit.terrainshop.adminlogin.reports.topnitem.TopNItem;
import com.shuvo.ttit.terrainshop.homepage.HomePage;

public class ReportPage extends AppCompatActivity {

    MaterialCardView orderCollection;
    MaterialCardView topNItem;
    MaterialCardView reOrder;
    MaterialCardView deliveryRegister;
    MaterialCardView itemWiseStock;
    MaterialCardView deliveryPending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_page);

        orderCollection = findViewById(R.id.order_collection);
        topNItem = findViewById(R.id.top_n_item);
        reOrder = findViewById(R.id.re_order);
        deliveryRegister = findViewById(R.id.delivery_register);
        itemWiseStock = findViewById(R.id.item_wise_stock);
        deliveryPending = findViewById(R.id.delivery_pending);

        orderCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportPage.this, OrderCollection.class);
                startActivity(intent);
            }
        });

        topNItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportPage.this, TopNItem.class);
                startActivity(intent);
            }
        });

        reOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportPage.this, ReOrder.class);
                startActivity(intent);
            }
        });

        deliveryRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportPage.this, DeliveryRegister.class);
                startActivity(intent);
            }
        });

        itemWiseStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportPage.this, ItemWiseStock.class);
                startActivity(intent);
            }
        });

        deliveryPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportPage.this, DeliveryPending.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(ReportPage.this)
                .setTitle("EXIT!")
                .setMessage("Do you want to exit from this Report Panel?")
                .setIcon(R.drawable.terrain_shop_icon)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}