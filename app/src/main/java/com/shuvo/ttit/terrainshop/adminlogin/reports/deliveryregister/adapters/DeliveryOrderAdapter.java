package com.shuvo.ttit.terrainshop.adminlogin.reports.deliveryregister.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.adminlogin.reports.deliveryregister.lists.DeliveryOrderList;

import java.util.ArrayList;

public class DeliveryOrderAdapter extends RecyclerView.Adapter<DeliveryOrderAdapter.DeliveryOrderHolder> {

    private ArrayList<DeliveryOrderList> mCategoryItem;
    private final ClickedItem myClickedItem;
    private final Context myContext;

    public DeliveryOrderAdapter(ArrayList<DeliveryOrderList> mCategoryItem, ClickedItem myClickedItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myClickedItem = myClickedItem;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public DeliveryOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.delivery_order_item_layout, parent, false);
        return new DeliveryOrderHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryOrderHolder holder, int position) {
        DeliveryOrderList deliveryOrderList = mCategoryItem.get(position);

        holder.deliveryNo.setText(deliveryOrderList.getDelivery_no());
        holder.deliveryDate.setText(deliveryOrderList.getDelivery_date());
        String loc = deliveryOrderList.getLocation_name() + ", " + deliveryOrderList.getDivision_name();
        holder.deliveryLocation.setText(loc);
        holder.orderNo.setText(deliveryOrderList.getOrder_no());
        holder.orderDate.setText(deliveryOrderList.getOrder_date());
        holder.invAmnt.setText(deliveryOrderList.getInvoice_amnt());
        holder.deliveryCharge.setText(deliveryOrderList.getDelivery_charge());
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public class DeliveryOrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView deliveryNo;
        public TextView deliveryDate;
        public TextView deliveryLocation;
        public TextView orderNo;
        public TextView orderDate;
        public TextView invAmnt;
        public TextView deliveryCharge;

        ClickedItem mClickedItem;

        public DeliveryOrderHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            deliveryNo = itemView.findViewById(R.id.delivery_no_dr);
            deliveryDate = itemView.findViewById(R.id.delivery_date_dr);
            deliveryLocation = itemView.findViewById(R.id.delivery_location_dr);
            orderNo = itemView.findViewById(R.id.order_no_dr);
            orderDate = itemView.findViewById(R.id.order_date_dr);
            invAmnt = itemView.findViewById(R.id.invoice_amount_dr);
            deliveryCharge = itemView.findViewById(R.id.delivery_charge_dr);

            this.mClickedItem = ci;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mClickedItem.onCategoryClicked(getAdapterPosition());
            Log.i("Client Name", mCategoryItem.get(getAdapterPosition()).getClient_name());
        }
    }

    public interface ClickedItem {
        void onCategoryClicked(int CategoryPosition);
    }

    public void filterList(ArrayList<DeliveryOrderList> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }
}
