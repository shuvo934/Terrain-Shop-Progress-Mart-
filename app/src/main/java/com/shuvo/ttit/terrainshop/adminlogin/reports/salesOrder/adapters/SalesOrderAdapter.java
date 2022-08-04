package com.shuvo.ttit.terrainshop.adminlogin.reports.salesOrder.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.adminlogin.reports.salesOrder.lists.SalesOrderList;

import java.util.ArrayList;

public class SalesOrderAdapter extends RecyclerView.Adapter<SalesOrderAdapter.SalesOrderHolder> {

    private ArrayList<SalesOrderList> mCategoryItem;
    private final Context myContext;
    private final ClickedItem myClickedItem;

    public SalesOrderAdapter(ArrayList<SalesOrderList> mCategoryItem, Context myContext, ClickedItem myClickedItem) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
        this.myClickedItem = myClickedItem;
    }

    @NonNull
    @Override
    public SalesOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.sales_order_item_layout, parent, false);
        return new SalesOrderHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesOrderHolder holder, int position) {

        SalesOrderList salesOrderList = mCategoryItem.get(position);

        holder.orderNo.setText(salesOrderList.getOrder_no());
        holder.orderDate.setText(salesOrderList.getSom_date());
        holder.deliveryDate.setText(salesOrderList.getDelivery_date());

        if (salesOrderList.getDelivery_status().equals("DELIVERED")) {
            holder.deliveryStatus.setText(salesOrderList.getDelivery_status());
            holder.deliveryStatus.setTextColor(Color.parseColor("#FF018786"));
        }
        else {
            holder.deliveryStatus.setText(salesOrderList.getDelivery_status());
            holder.deliveryStatus.setTextColor(Color.parseColor("#b2bec3"));
        }

        holder.divisionName.setText(salesOrderList.getDivision_name());
        holder.locationName.setText(salesOrderList.getLocation_name());
        holder.deliveryCharge.setText(salesOrderList.getDelivery_charge());
        holder.orderAmount.setText(salesOrderList.getOrder_amnt());

        int charge = Integer.parseInt(salesOrderList.getDelivery_charge());
        int amnt = Integer.parseInt(salesOrderList.getOrder_amnt());

        int tt = charge + amnt;

        String total = String.valueOf(tt);

        holder.totalAmount.setText(total);
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public static class SalesOrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView orderNo;
        public TextView orderDate;
        public TextView deliveryDate;
        public TextView deliveryStatus;
        public TextView divisionName;
        public TextView locationName;
        public TextView deliveryCharge;
        public TextView orderAmount;
        public TextView totalAmount;

        ClickedItem mClickedItem;

        public SalesOrderHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);

            orderNo = itemView.findViewById(R.id.order_no_so);
            orderDate = itemView.findViewById(R.id.order_date_so);
            deliveryDate = itemView.findViewById(R.id.delivery_date_so);
            deliveryStatus = itemView.findViewById(R.id.delivery_status_so);
            divisionName = itemView.findViewById(R.id.division_so);
            locationName = itemView.findViewById(R.id.location_so);
            deliveryCharge = itemView.findViewById(R.id.delivery_charge_so);
            orderAmount = itemView.findViewById(R.id.order_amount_so);
            totalAmount = itemView.findViewById(R.id.total_amount_so);

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

    public void filterList(ArrayList<SalesOrderList> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }
}
