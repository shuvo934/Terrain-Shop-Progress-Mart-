package com.shuvo.ttit.terrainshop.adminlogin.reports.itemstock.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.adminlogin.reports.itemstock.lists.WareHouseQtyList;

import java.util.ArrayList;

public class WareHouseQtyAdapter extends RecyclerView.Adapter<WareHouseQtyAdapter.WareHouseHolder> {

    private ArrayList<WareHouseQtyList> mCategoryItem;
    private final Context myContext;

    public WareHouseQtyAdapter(ArrayList<WareHouseQtyList> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public WareHouseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.warehouse_item_qty_layout, parent, false);
        return new WareHouseHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WareHouseHolder holder, int position) {
        WareHouseQtyList categoryItem = mCategoryItem.get(position);

        holder.warehouse.setText(categoryItem.getWarehouse());
        holder.qty.setText(categoryItem.getQty());
        int serial = Integer.parseInt(categoryItem.getSerial_no());
        if (serial == mCategoryItem.size()) {
            holder.bottomLay.setVisibility(View.INVISIBLE);
        } else {
            holder.bottomLay.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public static class WareHouseHolder extends RecyclerView.ViewHolder  {
        public TextView warehouse;
        public TextView qty;
        public LinearLayout bottomLay;


        public WareHouseHolder(@NonNull View itemView) {
            super(itemView);
            warehouse = itemView.findViewById(R.id.warehouse_name_item_stock);
            qty = itemView.findViewById(R.id.warehouse_item_qty);
            bottomLay = itemView.findViewById(R.id.bottom_line_lay);

        }

    }

    public void filterList(ArrayList<WareHouseQtyList> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }
}
