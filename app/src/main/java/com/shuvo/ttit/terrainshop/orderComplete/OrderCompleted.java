package com.shuvo.ttit.terrainshop.orderComplete;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.shuvo.ttit.terrainshop.R;

public class OrderCompleted extends AppCompatActivity {

    RelativeLayout orderCompleteBar;

    TextView orderNo;
    TextView deliveryAddress;
    TextView deliveryDate;

    MaterialButton orderMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_completed);

        Window window = getWindow();
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.back_color));

        orderCompleteBar = findViewById(R.id.order_completed_action_bar);
        orderNo = findViewById(R.id.order_no_text_oc);
        deliveryAddress = findViewById(R.id.delivery_address_oc);
        deliveryDate = findViewById(R.id.delivery_date_oc);

        orderMore = findViewById(R.id.order_more_button_o_c);

        Intent intent = getIntent();
        String order = intent.getStringExtra("ORDER NO");
        String adds = intent.getStringExtra("DELIVERY ADDRESS");
        String date = intent.getStringExtra("DELIVERY DATE");

        orderCompleteBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        orderNo.setText(order);
        deliveryAddress.setText(adds);
        deliveryDate.setText(date);

        orderMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}