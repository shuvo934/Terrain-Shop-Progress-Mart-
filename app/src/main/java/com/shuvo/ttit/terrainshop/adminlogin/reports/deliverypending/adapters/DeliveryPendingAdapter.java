package com.shuvo.ttit.terrainshop.adminlogin.reports.deliverypending.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.adminlogin.reports.deliverypending.lists.DeliveryPendingList;

import java.util.ArrayList;

public class DeliveryPendingAdapter extends RecyclerView.Adapter<DeliveryPendingAdapter.DPHolder> {

    private ArrayList<DeliveryPendingList> mCategoryItem;
    private final Context myContext;
    private final ClickedItem myClickedItem;

    public DeliveryPendingAdapter(ArrayList<DeliveryPendingList> mCategoryItem, Context myContext, ClickedItem myClickedItem) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
        this.myClickedItem = myClickedItem;
    }

    @NonNull
    @Override
    public DPHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.delivery_pending_item_layout, parent, false);
        return new DPHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull DPHolder holder, int position) {
        DeliveryPendingList deliveryPendingList = mCategoryItem.get(position);

        holder.orderNo.setText(deliveryPendingList.getOrder_no());
        holder.orderDate.setText(deliveryPendingList.getSom_date());
        holder.deliveryDate.setText(deliveryPendingList.getDelivery_date());

        if (deliveryPendingList.getDelivery_status().equals("DELIVERED")) {
            holder.deliveryStatus.setText(deliveryPendingList.getDelivery_status());
            holder.deliveryStatus.setTextColor(Color.parseColor("#FF018786"));
        }
        else {
            holder.deliveryStatus.setText(deliveryPendingList.getDelivery_status());
            holder.deliveryStatus.setTextColor(Color.parseColor("#b2bec3"));
        }

        holder.divisionName.setText(deliveryPendingList.getDivision_name());
        holder.locationName.setText(deliveryPendingList.getLocation_name());
        holder.deliveryCharge.setText(deliveryPendingList.getDelivery_charge());
        holder.orderAmount.setText(deliveryPendingList.getOrder_amnt());

        holder.penidngAmount.setText(deliveryPendingList.getPending_amnt());
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public static class DPHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView orderNo;
        public TextView orderDate;
        public TextView deliveryDate;
        public TextView deliveryStatus;
        public TextView divisionName;
        public TextView locationName;
        public TextView deliveryCharge;
        public TextView orderAmount;
        public TextView penidngAmount;

        ClickedItem mClickedItem;

        public DPHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);

            orderNo = itemView.findViewById(R.id.order_no_dp);
            orderDate = itemView.findViewById(R.id.order_date_dp);
            deliveryDate = itemView.findViewById(R.id.delivery_date_dp);
            deliveryStatus = itemView.findViewById(R.id.delivery_status_dp);
            divisionName = itemView.findViewById(R.id.division_dp);
            locationName = itemView.findViewById(R.id.location_dp);
            deliveryCharge = itemView.findViewById(R.id.delivery_charge_dp);
            orderAmount = itemView.findViewById(R.id.order_amount_dp);
            penidngAmount = itemView.findViewById(R.id.pending_amount_dp);

            this.mClickedItem = ci;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mClickedItem.onCategoryClicked(getAdapterPosition());
            //Log.i("Client Name", mCategoryItem.get(getAdapterPosition()).getvDate());
        }

    }
    public interface ClickedItem {
        void onCategoryClicked(int CategoryPosition);
    }

    public void filterList(ArrayList<DeliveryPendingList> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }
}
