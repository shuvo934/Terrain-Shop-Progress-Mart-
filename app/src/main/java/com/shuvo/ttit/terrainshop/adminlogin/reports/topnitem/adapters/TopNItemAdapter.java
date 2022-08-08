package com.shuvo.ttit.terrainshop.adminlogin.reports.topnitem.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.adminlogin.reports.topnitem.lists.TopNItemList;

import java.util.ArrayList;

public class TopNItemAdapter extends RecyclerView.Adapter<TopNItemAdapter.TopNItemHolder> {

    private ArrayList<TopNItemList> mCategoryItem;
    private final ClickedItem myClickedItem;
    private final Context myContext;

    int selectedPosition = -1;

    public TopNItemAdapter(ArrayList<TopNItemList> mCategoryItem, Context myContext, ClickedItem myClickedItem) {
        this.mCategoryItem = mCategoryItem;
        this.myClickedItem = myClickedItem;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public TopNItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.top_n_item_lists_view, parent, false);
        return new TopNItemHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull TopNItemHolder holder, int position) {

        TopNItemList topNItemLists = mCategoryItem.get(position);

        holder.slNo.setText(topNItemLists.getSl_no());
        holder.itemCode.setText(topNItemLists.getItem_code());
        holder.hsCode.setText(topNItemLists.getHs_code());
        holder.itemName.setText(topNItemLists.getItem_name());
        holder.partNo.setText(topNItemLists.getPart_no());
        holder.unit.setText(topNItemLists.getUnit());
        holder.cateName.setText(topNItemLists.getCat_name());
        holder.subCatName.setText(topNItemLists.getSubCat_name());
        holder.qty.setText(topNItemLists.getQuantity());
        holder.amnt.setText(topNItemLists.getAmount());

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

    public void filterList(ArrayList<TopNItemList> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }

    public class TopNItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView cateName;
        public TextView subCatName;
        public TextView itemCode;
        public TextView itemName;
        public TextView unit;
        public TextView hsCode;
        public TextView partNo;
        public TextView qty;
        public TextView amnt;
        public TextView slNo;

        ClickedItem mClickedItem;

        public TopNItemHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            slNo = itemView.findViewById(R.id.seral_no_top_n_item);
            cateName = itemView.findViewById(R.id.category_name_top_n_item);
            subCatName = itemView.findViewById(R.id.sub_category_name_top_n_item);
            itemCode = itemView.findViewById(R.id.item_code_top_n_item);
            itemName = itemView.findViewById(R.id.item_name_top_n_item);
            unit = itemView.findViewById(R.id.unit_top_n_item);
            partNo = itemView.findViewById(R.id.part_no_top_n_item);
            hsCode = itemView.findViewById(R.id.hs_code_top_n_item);
            qty = itemView.findViewById(R.id.quantity_top_n_item);
            amnt = itemView.findViewById(R.id.amount_top_n_item);

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

}
