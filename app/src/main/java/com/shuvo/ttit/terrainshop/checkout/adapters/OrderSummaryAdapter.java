package com.shuvo.ttit.terrainshop.checkout.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shuvo.ttit.terrainshop.R;

import com.shuvo.ttit.terrainshop.cart.lists.CartItemList;

import java.util.ArrayList;

public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.OrderSumHolder> {

    private final Context context;
    private final ArrayList<CartItemList> cartItemLists;

    public OrderSummaryAdapter(Context context, ArrayList<CartItemList> cartItemLists) {
        this.context = context;
        this.cartItemLists = cartItemLists;
    }

    @NonNull
    @Override
    public OrderSumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_summary_item_layout, parent, false);
        return new OrderSumHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderSumHolder holder, int position) {

        CartItemList cartItemList = cartItemLists.get(position);

        String nameText = cartItemList.getItem_qty() + "  x  " + cartItemList.getIem_name();
        holder.productName_QTY.setText(nameText);
        String priceText = "Price: ৳"+ cartItemList.getCardRate();
        holder.productPrice.setText(priceText);
        String totalPriceText = "৳ " + cartItemList.getItem_total_price();
        holder.itemWisePrice.setText(totalPriceText);
        String packQty = "";

        if (cartItemList.getPackage_qty().equals(" ")) {
            packQty = "1 " + cartItemList.getItem_unit();
            holder.itemUnit.setText(packQty);
        } else {
            packQty = "1 " + cartItemList.getItem_unit() + " ( " + cartItemList.getPackage_qty() + " )";
            holder.itemUnit.setText(packQty);
        }
    }

    @Override
    public int getItemCount() {
        return cartItemLists.size();
    }

    public static class OrderSumHolder extends RecyclerView.ViewHolder {

        public TextView productName_QTY;
        public TextView productPrice;
        public TextView itemWisePrice;
        public TextView itemUnit;


        public OrderSumHolder(@NonNull View itemView) {
            super(itemView);
            productName_QTY = itemView.findViewById(R.id.order_summary_item_name_with_qty);
            productPrice = itemView.findViewById(R.id.order_summary_item_price);
            itemWisePrice = itemView.findViewById(R.id.order_summary_item_total_price);
            itemUnit = itemView.findViewById(R.id.order_summary_item_unit);

        }

    }

}
