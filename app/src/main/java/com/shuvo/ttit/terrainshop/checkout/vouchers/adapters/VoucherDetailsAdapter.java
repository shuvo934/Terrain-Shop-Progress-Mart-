package com.shuvo.ttit.terrainshop.checkout.vouchers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.checkout.vouchers.lists.VoucherDetailsList;

import java.util.ArrayList;

public class VoucherDetailsAdapter extends RecyclerView.Adapter<VoucherDetailsAdapter.VoucherDetailsHolder> {

    private final ArrayList<VoucherDetailsList> voucherDetailsLists;
    private final Context context;

    public VoucherDetailsAdapter(ArrayList<VoucherDetailsList> voucherDetailsLists, Context context) {
        this.voucherDetailsLists = voucherDetailsLists;
        this.context = context;
    }

    @NonNull
    @Override
    public VoucherDetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.voucher_item_details_layout,parent,false);
        return new VoucherDetailsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherDetailsHolder holder, int position) {
        VoucherDetailsList voucherDetailsList = voucherDetailsLists.get(position);

        String distype = voucherDetailsList.getDis_type();
        String disVal = voucherDetailsList.getDis_val();
        String fromAmnt = voucherDetailsList.getFrom_amnt();
        String toAmnt = voucherDetailsList.getTo_amnt();

        String text = "";
        if (distype.contains("%")) {
            text = "* Delivery fee " + disVal + " % off when total between " + "৳" + fromAmnt + " and ৳" + toAmnt;
        }
        else if (distype.contains("SR")){
            text = "* Delivery fee ৳" + disVal + " off when total between " + "৳" + fromAmnt + " and ৳" + toAmnt;
        }

        holder.vDetails.setText(text);
    }

    @Override
    public int getItemCount() {
        return voucherDetailsLists.size();
    }

    public static class VoucherDetailsHolder extends RecyclerView.ViewHolder {

        public TextView vDetails;

        public VoucherDetailsHolder(@NonNull View itemView) {
            super(itemView);
            vDetails = itemView.findViewById(R.id.voucher_details_voucher);
        }

    }
}
