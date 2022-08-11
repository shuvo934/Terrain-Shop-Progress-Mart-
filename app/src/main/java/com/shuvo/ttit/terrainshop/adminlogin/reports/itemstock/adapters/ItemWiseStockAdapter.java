package com.shuvo.ttit.terrainshop.adminlogin.reports.itemstock.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.adminlogin.reports.itemstock.lists.ItemWiseStockList;
import com.shuvo.ttit.terrainshop.adminlogin.reports.itemstock.lists.WareHouseQtyList;

import java.util.ArrayList;

public class ItemWiseStockAdapter extends RecyclerView.Adapter<ItemWiseStockAdapter.ItemWiseHolder> {

    private ArrayList<ItemWiseStockList> mCategoryItem;
    private final ClickedItem myClickedItem;
    private final Context myContext;
    public WareHouseQtyAdapter wareHouseQtyAdapter;

    int selectedPosition = -1;

    public ItemWiseStockAdapter(ArrayList<ItemWiseStockList> mCategoryItem, ClickedItem myClickedItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myClickedItem = myClickedItem;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public ItemWiseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.item_stock_item_layout, parent, false);
        return new ItemWiseHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemWiseHolder holder, int position) {

        ItemWiseStockList categoryItem = mCategoryItem.get(position);

        if (categoryItem.getSlNo() != null) {
            holder.sl_no.setText(categoryItem.getSlNo());
        }

        holder.itemCode.setText(categoryItem.getItem_code());
        holder.itemName.setText(categoryItem.getItem_name());
        holder.unit.setText(categoryItem.getItem_unit());
        holder.vat.setText(categoryItem.getVat());
        holder.amnt.setText(categoryItem.getPurchase_amnt());
        holder.sales.setText(categoryItem.getSales_price());
        holder.hsCode.setText(categoryItem.getHs_code());
        holder.cateName.setText(categoryItem.getCate_name());
        holder.subCatName.setText(categoryItem.getSub_cate_name());

        ArrayList<WareHouseQtyList> wareHouseQtyLists = categoryItem.getItem_qty();

        holder.qty.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(myContext);
        holder.qty.setLayoutManager(layoutManager);
        wareHouseQtyAdapter = new WareHouseQtyAdapter(wareHouseQtyLists,myContext);
        holder.qty.setAdapter(wareHouseQtyAdapter);

        if(selectedPosition == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#81ecec"));
        }
        else {
            holder.itemView.setBackgroundColor(Color.parseColor("#f5f6fa"));
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition == position) {
                    selectedPosition = -1;
                } else {
                    selectedPosition=position;
                }

                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public class ItemWiseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView sl_no;
        public TextView cateName;
        public TextView subCatName;
        public TextView itemCode;
        public TextView itemName;
        public TextView unit;
        public RecyclerView qty;
        public TextView vat;
        public TextView amnt;
        public TextView sales;
        public TextView hsCode;


        ClickedItem mClickedItem;

        public ItemWiseHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            sl_no = itemView.findViewById(R.id.serial_no_item_stock);
            cateName = itemView.findViewById(R.id.category_name_item_stock);
            subCatName = itemView.findViewById(R.id.sub_category_name_item_stock);
            itemCode = itemView.findViewById(R.id.item_code_item_stock);
            itemName = itemView.findViewById(R.id.item_reference_name_item_stock);
            unit = itemView.findViewById(R.id.item_unit_item_stock);
            qty = itemView.findViewById(R.id.item_qty_item_stock);
            vat = itemView.findViewById(R.id.item_vat_amnt_item_stock);
            amnt = itemView.findViewById(R.id.item_purchase_amount_item_stock);
            sales = itemView.findViewById(R.id.item_sales_price_item_stock);
            hsCode = itemView.findViewById(R.id.item_hs_code_item_stock);



            this.mClickedItem = ci;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mClickedItem.onCategoryClicked(getAdapterPosition());
            Log.i("Category Name", mCategoryItem.get(getAdapterPosition()).getItem_name());
        }
    }

    public interface ClickedItem {
        void onCategoryClicked(int CategoryPosition);
    }

    public void filterList(ArrayList<ItemWiseStockList> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }
}
