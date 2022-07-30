package com.shuvo.ttit.terrainshop.checkout.vouchers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.checkout.vouchers.lists.VoucherDetailsList;
import com.shuvo.ttit.terrainshop.checkout.vouchers.lists.VoucherList;

import java.util.ArrayList;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherHolder> {

    private final ArrayList<VoucherList> voucherLists;
    private final Context context;
    private final ClickedItem myClickedItem;
    public VoucherDetailsAdapter voucherDetailsAdapter;

    public VoucherAdapter(ArrayList<VoucherList> voucherLists, Context context, ClickedItem myClickedItem) {
        this.voucherLists = voucherLists;
        this.context = context;
        this.myClickedItem = myClickedItem;
    }

    @NonNull
    @Override
    public VoucherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.voucher_item_layout,parent,false);
        return new VoucherHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherHolder holder, int position) {

        VoucherList voucherList = voucherLists.get(position);

        holder.voucherName.setText(voucherList.getVoucher_code());
        ArrayList<VoucherDetailsList> voucherDetailsLists = voucherList.getVoucherDetailsLists();

        holder.detailsView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        holder.detailsView.setLayoutManager(layoutManager);
        voucherDetailsAdapter = new VoucherDetailsAdapter(voucherDetailsLists,context);
        holder.detailsView.setAdapter(voucherDetailsAdapter);

    }

    @Override
    public int getItemCount() {
        return voucherLists.size();
    }

    public static class VoucherHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView voucherName;
        public RecyclerView detailsView;
        ClickedItem mClickedItem;

        public VoucherHolder(@NonNull View itemView,ClickedItem ci) {
            super(itemView);

            voucherName = itemView.findViewById(R.id.voucher_name_voucher);
            detailsView = itemView.findViewById(R.id.voucher_item_details_list_view);

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
