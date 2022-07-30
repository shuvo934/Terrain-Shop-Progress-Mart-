package com.shuvo.ttit.terrainshop.homepage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.homepage.lists.NavigationUserList;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    private final ArrayList<NavigationUserList> navigationUserLists;
    private final Context context;
    private final MenuClickedItem myMenuClickedItem;

    public UserAdapter(ArrayList<NavigationUserList> navigationUserLists, Context context, MenuClickedItem myMenuClickedItem) {
        this.navigationUserLists = navigationUserLists;
        this.context = context;
        this.myMenuClickedItem = myMenuClickedItem;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item_layout,parent,false);
        return new UserHolder(view, myMenuClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {

        NavigationUserList navigationUserList = navigationUserLists.get(position);

        holder.voucherName.setText(navigationUserList.getName());

    }

    @Override
    public int getItemCount() {
        return navigationUserLists.size();
    }

    public static class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView voucherName;
        public RecyclerView detailsView;
        MenuClickedItem mMenuClickedItem;

        public UserHolder(@NonNull View itemView, MenuClickedItem ci) {
            super(itemView);

            voucherName = itemView.findViewById(R.id.user_list_item);

            this.mMenuClickedItem = ci;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            mMenuClickedItem.onMenuClicked(getAdapterPosition());
        }
    }

    public interface MenuClickedItem {
        void onMenuClicked(int CategoryPosition);
    }
}
