package com.shuvo.ttit.terrainshop.homepage.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.homepage.lists.CategoryList;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryVH> {

    private final Context context;
    private final ArrayList<CategoryList> categoryLists;
    private final ClickedItem myClickedItem;

    public CategoryAdapter(Context context, ArrayList<CategoryList> categoryLists, ClickedItem myClickedItem) {
        this.context = context;
        this.categoryLists = categoryLists;
        this.myClickedItem = myClickedItem;
    }

    @NonNull
    @Override
    public CategoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.categories_item_layout, parent, false);
        return new CategoryVH(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryVH holder, int position) {

        CategoryList categoryList = categoryLists.get(position);

        holder.cCard.setCardBackgroundColor(categoryList.getColor());
        holder.cName.setText(categoryList.getCategoryName());

        int color = manipulateColor(categoryList.getColor(), 0.6f);
        holder.cName.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        return categoryLists.size();
    }

    public static class CategoryVH extends RecyclerView.ViewHolder implements View.OnClickListener {


        public CardView cCard;
        public TextView cName;

        ClickedItem mClickedItem;

        public CategoryVH(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            cCard = itemView.findViewById(R.id.category_item_card);
            cName = itemView.findViewById(R.id.category_item_name);

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

    public static int manipulateColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r,255),
                Math.min(g,255),
                Math.min(b,255));
    }
}
