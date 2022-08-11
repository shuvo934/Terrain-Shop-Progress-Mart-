package com.shuvo.ttit.terrainshop.adminlogin.reports.deliveryregister.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.adminlogin.reports.deliveryregister.lists.DeliveryDetailsList;

import java.util.ArrayList;

public class DeliveryOrderDetailsAdapter extends RecyclerView.Adapter<DeliveryOrderDetailsAdapter.DODHolder> {

    private final ArrayList<DeliveryDetailsList> mCategoryItem;
    private final Context myContext;

    public DeliveryOrderDetailsAdapter(ArrayList<DeliveryDetailsList> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public DODHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.delivery_order_details_item_layout, parent, false);
        return new DODHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DODHolder holder, int position) {

        DeliveryDetailsList deliveryDetailsList = mCategoryItem.get(position);

        holder.itemName.setText(deliveryDetailsList.getItem_name());
        holder.partNumb.setText(deliveryDetailsList.getPart_numb());
        holder.hsCode.setText(deliveryDetailsList.getHs_code());
        holder.color.setText(deliveryDetailsList.getColor_name());
        holder.size.setText(deliveryDetailsList.getSize_name());
        holder.rate.setText(deliveryDetailsList.getSd_rate());
        holder.itemQuantity.setText(deliveryDetailsList.getSd_qty());
        holder.itemUnit.setText(deliveryDetailsList.getItem_unit());
        holder.unitPrice.setText(deliveryDetailsList.getPrice());
        holder.discountType.setText(deliveryDetailsList.getDiscount_type());
        holder.disValueUnit.setText(deliveryDetailsList.getDiscount_value());
        holder.amnt.setText(deliveryDetailsList.getTotal_amnt());
        holder.retAmnt.setText(deliveryDetailsList.getReturn_amnt());
        holder.rtnQty.setText(deliveryDetailsList.getReturn_qty());
        holder.totQty.setText(deliveryDetailsList.getSd_qty());

    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public static class DODHolder extends RecyclerView.ViewHolder {

        public TextView itemName;
        public TextView partNumb;
        public TextView hsCode;
        public TextView color;
        public TextView size;
        public TextView rate;
        public TextView itemQuantity;
        public TextView itemUnit;
        public TextView unitPrice;
        public TextView discountType;
        public TextView disValueUnit;
        public TextView amnt;
        public TextView retAmnt;
        public TextView rtnQty;
        public TextView totQty;

        public DODHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.item_name_dod);
            partNumb = itemView.findViewById(R.id.part_numb_dod);
            hsCode = itemView.findViewById(R.id.hs_code_dod);
            color = itemView.findViewById(R.id.color_name_dod);
            size = itemView.findViewById(R.id.size_name_dod);
            rate = itemView.findViewById(R.id.rate_unit_dod);
            itemQuantity = itemView.findViewById(R.id.quantity_dod);
            itemUnit = itemView.findViewById(R.id.unit_dod);
            unitPrice = itemView.findViewById(R.id.unit_price_dod);
            discountType = itemView.findViewById(R.id.discount_type_dod);
            disValueUnit = itemView.findViewById(R.id.dis_value_unit_dod);
            amnt = itemView.findViewById(R.id.invoice_amnt_dod);
            retAmnt = itemView.findViewById(R.id.ret_amnt_dod);
            rtnQty = itemView.findViewById(R.id.return_qty_dod);
            totQty = itemView.findViewById(R.id.total_qty_dod);


        }
    }
}
