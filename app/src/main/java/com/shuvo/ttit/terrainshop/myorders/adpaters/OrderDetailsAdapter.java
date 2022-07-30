package com.shuvo.ttit.terrainshop.myorders.adpaters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shuvo.ttit.terrainshop.R;

import com.shuvo.ttit.terrainshop.myorders.lists.OrderDetailsList;

import java.util.ArrayList;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsHolder> {

    private final Context context;
    private final ArrayList<OrderDetailsList> orderDetailsLists;

    public OrderDetailsAdapter(Context context, ArrayList<OrderDetailsList> orderDetailsLists) {
        this.context = context;
        this.orderDetailsLists = orderDetailsLists;
    }

    @NonNull
    @Override
    public OrderDetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_history_details_item_layout, parent, false);
        return new OrderDetailsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsHolder holder, int position) {

        OrderDetailsList orderDetailsList = orderDetailsLists.get(position);

//        String nameText = orderDetailsList.getSod_qty() + "  x  " + orderDetailsList.getItem_name();
        holder.productName_QTY.setText(orderDetailsList.getItem_name());

        String priceText = "৳ "+ orderDetailsList.getItem_card_rate();
        holder.productPrice.setText(priceText);

        String totalPriceText = "৳ " + orderDetailsList.getOrder_rate();
        holder.itemWisePrice.setText(totalPriceText);

        String qty = orderDetailsList.getSod_qty() + "x";

        holder.qty.setText(qty);

        String un = "1 " + orderDetailsList.getUnit();

        holder.itemUnit.setText(un);

        if (orderDetailsList.getDis_type() != null) {
            if (!orderDetailsList.getDis_type().isEmpty()) {
                holder.productRealPrice.setVisibility(View.VISIBLE);
                String realP = "৳ " + orderDetailsList.getItem_real_rate();
                holder.productRealPrice.setText(realP);
                holder.productRealPrice.setPaintFlags(holder.productRealPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            else {
                holder.productRealPrice.setVisibility(View.GONE);
            }
        }
        else {
            holder.productRealPrice.setVisibility(View.GONE);
        }

        Glide.with(context)
                .load(orderDetailsList.getBitmap())
                .fitCenter()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return orderDetailsLists.size();
    }

    public static class OrderDetailsHolder extends RecyclerView.ViewHolder {

        public TextView productName_QTY;
        public TextView productPrice;
        public TextView productRealPrice;
        public TextView itemWisePrice;
        public ImageView imageView;
        public TextView qty;
        public TextView itemUnit;


        public OrderDetailsHolder(@NonNull View itemView) {
            super(itemView);
            productName_QTY = itemView.findViewById(R.id.order_history_details_item_name_with_qty);
            productPrice = itemView.findViewById(R.id.order_history_details_item_card_price);
            productRealPrice = itemView.findViewById(R.id.order_history_details_item_real_price);
            itemWisePrice = itemView.findViewById(R.id.order_history_details_item_total_price);
            imageView = itemView.findViewById(R.id.order_details_item_image);
            qty = itemView.findViewById(R.id.order_history_details_item_qty);
            itemUnit = itemView.findViewById(R.id.order_history_details_item_unit);

        }

    }
}
