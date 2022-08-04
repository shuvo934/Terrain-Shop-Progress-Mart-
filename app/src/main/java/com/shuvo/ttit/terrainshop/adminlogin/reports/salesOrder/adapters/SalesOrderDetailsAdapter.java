package com.shuvo.ttit.terrainshop.adminlogin.reports.salesOrder.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.adminlogin.reports.salesOrder.lists.SalesOrderDetailList;

import java.util.ArrayList;

public class SalesOrderDetailsAdapter extends RecyclerView.Adapter<SalesOrderDetailsAdapter.SODHolder> {

    private final ArrayList<SalesOrderDetailList> mCategoryItem;
    private final Context myContext;

    public SalesOrderDetailsAdapter(ArrayList<SalesOrderDetailList> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public SODHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.sales_order_details_item_layout, parent, false);
        return new SODHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SODHolder holder, int position) {
        SalesOrderDetailList salesOrderDetailList = mCategoryItem.get(position);

        holder.itemName.setText(salesOrderDetailList.getItem_name());
        holder.hsCode.setText(salesOrderDetailList.getHs_code());
        holder.rate.setText(salesOrderDetailList.getItem_actual_rate());
        holder.itemQuantity.setText(salesOrderDetailList.getQty());
        holder.itemUnit.setText(salesOrderDetailList.getUnit());
        holder.unitPrice.setText(salesOrderDetailList.getItem_price());
        holder.discountType.setText(salesOrderDetailList.getDis_type());
        holder.disValueUnit.setText(salesOrderDetailList.getDis_val());
        holder.amnt.setText(salesOrderDetailList.getTotal_amnt());
        holder.delQty.setText(salesOrderDetailList.getDelivered_qty());
        holder.rtnQty.setText(salesOrderDetailList.getReturn_qty());
        holder.balQty.setText(salesOrderDetailList.getBalance_qty());
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public static class SODHolder extends RecyclerView.ViewHolder {

        public TextView itemName;
        public TextView hsCode;
        public TextView rate;
        public TextView itemQuantity;
        public TextView itemUnit;
        public TextView unitPrice;
        public TextView discountType;
        public TextView disValueUnit;
        public TextView amnt;
        public TextView delQty;
        public TextView rtnQty;
        public TextView balQty;

        public SODHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.item_name_sod);
            hsCode = itemView.findViewById(R.id.hs_code_sod);
            rate = itemView.findViewById(R.id.rate_unit_sod);
            itemQuantity = itemView.findViewById(R.id.quantity_sod);
            itemUnit = itemView.findViewById(R.id.unit_sod);
            unitPrice = itemView.findViewById(R.id.unit_price_sod);
            discountType = itemView.findViewById(R.id.discount_type_sod);
            disValueUnit = itemView.findViewById(R.id.dis_value_unit_sod);
            amnt = itemView.findViewById(R.id.order_amount_sod);
            delQty = itemView.findViewById(R.id.deliverd_qty_sod);
            rtnQty = itemView.findViewById(R.id.return_qty_sod);
            balQty = itemView.findViewById(R.id.balance_qty_sod);

        }
    }
}
