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
import com.shuvo.ttit.terrainshop.adminlogin.reports.salesOrder.OrderCollection;
import com.shuvo.ttit.terrainshop.homepage.HomePage;

public class ReportPage extends AppCompatActivity {

    MaterialCardView orderCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_page);

        orderCollection = findViewById(R.id.order_collection);

        orderCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportPage.this, OrderCollection.class);
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