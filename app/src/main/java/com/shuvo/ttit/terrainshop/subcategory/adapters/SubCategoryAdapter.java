package com.shuvo.ttit.terrainshop.subcategory.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.homepage.fragments.adapters.TopHitAdapter;
import com.shuvo.ttit.terrainshop.homepage.fragments.lists.TopHitItemList;
import com.shuvo.ttit.terrainshop.subcategory.lists.SubCategoryList;

import java.util.ArrayList;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.SUBHolder> {

    private final ArrayList<SubCategoryList> subCategoryLists;
    private final Context context;
    private final ClickedItem myClickedItem;

    public SubCategoryAdapter(ArrayList<SubCategoryList> subCategoryLists, Context context, ClickedItem myClickedItem) {
        this.subCategoryLists = subCategoryLists;
        this.context = context;
        this.myClickedItem = myClickedItem;
    }

    @NonNull
    @Override
    public SUBHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        if (viewType == 2) {
            view = LayoutInflater.from(context).inflate(R.layout.sub_category_item_layout,parent,false);
        }
        else {
            view = LayoutInflater.from(context).inflate(R.layout.sub_category_item_layout,parent,false);
        }
        return new SUBHolder(view,myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull SUBHolder holder, int position) {

        SubCategoryList subCategoryList = subCategoryLists.get(position);

        Glide.with(context)
                .load(subCategoryList.getIem_image())
                .fitCenter()
                .into(holder.imageView);

        holder.itemName.setText(subCategoryList.getIem_name());

    }

    @Override
    public int getItemCount() {
        return subCategoryLists.size();
    }

    @Override
    public int getItemViewType(int position) {

//        if ((position+1) % 5 * 2 == 0) {
//            return 2;
//        }
//        else {
//            return 1;
//        }

        if (subCategoryLists.size() % 2 != 0) {
            if (position == subCategoryLists.size() -1) {
                return 1;
            }
            else {
                return 1;
            }
        } else  {
            return 1;
        }

    }

    public class SUBHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageView;
        public TextView itemName;

        ClickedItem mClickedItem;

        public SUBHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);

            imageView = itemView.findViewById(R.id.sub_category_item_image);
            itemName = itemView.findViewById(R.id.sub_category_item_name);

            this.mClickedItem = ci;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            mClickedItem.onCategoryClicked(getAdapterPosition());
        }
    }

    public interface ClickedItem {
        void onCategoryClicked(int CategoryPosition);
    }
}
