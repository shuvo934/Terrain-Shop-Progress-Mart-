package com.shuvo.ttit.terrainshop.adminlogin.reports.reorder.adapters;

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
import com.shuvo.ttit.terrainshop.adminlogin.reports.reorder.lists.ReorderList;

import java.util.ArrayList;

public class ReorderAdapter extends RecyclerView.Adapter<ReorderAdapter.ReorderHolder> {

    private ArrayList<ReorderList> mCategoryItem;
    private final ClickedItem myClickedItem;
    private final Context myContext;

    public ReorderAdapter(ArrayList<ReorderList> mCategoryItem, ClickedItem myClickedItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myClickedItem = myClickedItem;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public ReorderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.re_order_item_layout, parent, false);
        return new ReorderHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ReorderHolder holder, int position) {
        ReorderList categoryItem = mCategoryItem.get(position);

        holder.ftext.setText(categoryItem.getItemName());
        holder.stext.setText(categoryItem.getUnit());
        holder.ttext.setText(categoryItem.getQty());
        if (categoryItem.getItemId().equals("Re-Order")) {
            holder.fotext.setText(categoryItem.getValue());
            holder.fotext.setTextSize(14);
            holder.fotext.setTextColor(Color.RED);
        } else {
            holder.fotext.setText(categoryItem.getValue()+" BDT");
        }
        holder.fifText.setText(categoryItem.getStock_qty());
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public class ReorderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView ftext;
        public TextView stext;
        public TextView ttext;
        public TextView fotext;
        public TextView fifText;

        ClickedItem mClickedItem;

        public ReorderHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            ftext = itemView.findViewById(R.id.first_item_stock);
            stext = itemView.findViewById(R.id.second_item_stock);
            ttext = itemView.findViewById(R.id.third_item_stock);
            fotext = itemView.findViewById(R.id.fourth_item_stock);
            fifText = itemView.findViewById(R.id.fifth_item_stock);


            this.mClickedItem = ci;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mClickedItem.onCategoryClicked(getAdapterPosition());
            Log.i("Category Name", mCategoryItem.get(getAdapterPosition()).getItemName());
        }
    }

    public interface ClickedItem {
        void onCategoryClicked(int CategoryPosition);
    }

    public void filterList(ArrayList<ReorderList> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }


}
