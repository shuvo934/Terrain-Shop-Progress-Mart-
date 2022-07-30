package com.shuvo.ttit.terrainshop.myorders.adpaters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.myorders.lists.OrderList;
import com.shuvo.ttit.terrainshop.products.adapters.ProductAdapter;
import com.shuvo.ttit.terrainshop.products.lists.ProductList;

import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ORDERHOLDER> {

    private final ArrayList<OrderList> orderLists;
    private final Context context;
    private final ClickedItem myClickedItem;

    public OrderHistoryAdapter(ArrayList<OrderList> orderLists, Context context, ClickedItem myClickedItem) {
        this.orderLists = orderLists;
        this.context = context;
        this.myClickedItem = myClickedItem;
    }

    @NonNull
    @Override
    public ORDERHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_history_item_layout,parent,false);
        return new ORDERHOLDER(view,myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ORDERHOLDER holder, int position) {

        OrderList orderList = orderLists.get(position);

        holder.orderNo.setText(orderList.getSom_order_no());
        holder.address.setText(orderList.getDelivery_address());
        String oDate = "Order Date: "+ orderList.getSom_date();
        holder.orderDate.setText(oDate);
        String dDate = "Delivery Date: "+ orderList.getDelivery_date();
        holder.deliveryDate.setText(dDate);
        int total = Integer.parseInt(orderList.getTotal_order_price()) + Integer.parseInt(orderList.getDelivery_charge());
        String tp = "৳ " + String.valueOf(total);
        holder.total.setText(tp);

        if (orderList.getOrder_tracking().equals("PLACED")) {
            holder.orderTracking.setText(orderList.getOrder_tracking());
            holder.orderTracking.setTextColor(Color.parseColor("#e1b12c"));
        }
        else if (orderList.getOrder_tracking().equals("CONFIRMED")) {
            holder.orderTracking.setText(orderList.getOrder_tracking());
            holder.orderTracking.setTextColor(Color.parseColor("#40739e"));
        }
    }

    @Override
    public int getItemCount() {
        return orderLists.size();
    }

    public static class ORDERHOLDER extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView orderNo;
        public TextView address;
        public TextView orderDate;
        public TextView deliveryDate;
        public TextView total;
        public TextView orderTracking;
        ClickedItem mClickedItem;
        public ORDERHOLDER(@NonNull View itemView, ClickedItem ci) {
            super(itemView);

            orderNo = itemView.findViewById(R.id.order_no_my_order);
            orderDate = itemView.findViewById(R.id.order_date_my_order);
            address = itemView.findViewById(R.id.delivery_address_my_order);
            deliveryDate = itemView.findViewById(R.id.delivery_date_my_order);
            total = itemView.findViewById(R.id.total_price_of_order);
            orderTracking = itemView.findViewById(R.id.order_tracking_text);

            this.mClickedItem = ci;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            mClickedItem.onCategoryClicked(getAdapterPosition());
        }
    }
    public interface ClickedItem {
        void onCategoryClicked(int CategoryPosition);
    }
}
