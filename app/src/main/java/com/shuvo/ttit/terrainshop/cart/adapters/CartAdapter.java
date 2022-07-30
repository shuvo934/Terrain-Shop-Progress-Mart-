package com.shuvo.ttit.terrainshop.cart.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.cart.OrderCart;
import com.shuvo.ttit.terrainshop.cart.lists.CartItemList;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {

    private final Context context;
    private final ArrayList<CartItemList> cartItemLists;
    private final ClickedItem myClickedItem;

    public CartAdapter(Context context, ArrayList<CartItemList> categoryLists, ClickedItem myClickedItem) {
        this.context = context;
        this.cartItemLists = categoryLists;
        this.myClickedItem = myClickedItem;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item_layout, parent, false);
        return new CartHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {

        CartItemList cartItemList = cartItemLists.get(position);

        holder.productName.setText(cartItemList.getIem_name());

        String packQty = "";

        if (cartItemList.getPackage_qty().equals(" ")) {
            holder.packQty.setVisibility(View.VISIBLE);
            packQty = "1 " + cartItemList.getItem_unit();
            holder.packQty.setText(packQty);
        }
        else {
            holder.packQty.setVisibility(View.VISIBLE);
            packQty = "1 " + cartItemList.getItem_unit() + " ( " + cartItemList.getPackage_qty() + " )";
            holder.packQty.setText(packQty);
        }

        String cP = "৳ "+ cartItemList.getCardRate();
        holder.cardPrice.setText(cP);

        if (cartItemList.getDiscount_value() != null) {
            if (!cartItemList.getDiscount_value().isEmpty()) {
                holder.realPrice.setVisibility(View.VISIBLE);
                String realP = "৳ " + cartItemList.getReal_price();
                holder.realPrice.setText(realP);
                holder.realPrice.setPaintFlags(holder.realPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            else {
                holder.realPrice.setVisibility(View.GONE);
            }
        }
        else {
            holder.realPrice.setVisibility(View.GONE);
        }

        holder.cartItemCount.setText(cartItemList.getItem_qty());
        String tp = "৳ "+ cartItemList.getItem_total_price();
        holder.cartItemWisePrice.setText(tp);
        Glide.with(context)
                .load(cartItemList.getBitmap())
                .fitCenter()
                .into(holder.productImage);

    }

    @Override
    public int getItemCount() {
        return cartItemLists.size();
    }

    public class CartHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView productImage;
        public TextView productName;
        public TextView cardPrice;
        public TextView realPrice;
        public TextView packQty;
        public MaterialButton removeButton;
        public MaterialButton addButton;
        public TextView cartItemCount;
        public TextView cartItemWisePrice;

        ClickedItem mClickedItem;

        public CartHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            productImage = itemView.findViewById(R.id.cart_item_image);
            productName = itemView.findViewById(R.id.cart_item_name);
            cardPrice = itemView.findViewById(R.id.cart_item_card_price);
            realPrice = itemView.findViewById(R.id.cart_item_real_price);
            packQty = itemView.findViewById(R.id.cart_item_package_qty);
            removeButton = itemView.findViewById(R.id.cart_item_remove_button);
            addButton = itemView.findViewById(R.id.cart_item_add_button);
            cartItemCount = itemView.findViewById(R.id.cart_item_qty_text);
            cartItemWisePrice = itemView.findViewById(R.id.cart_item_wise_total_value);

            this.mClickedItem = ci;
            itemView.setOnClickListener(this);

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String text = cartItemCount.getText().toString();
                    int num = Integer.parseInt(text);
                    String stockQty = cartItemLists.get(getAdapterPosition()).getStock_qty();
                    if (stockQty == null) {
                        stockQty = "0";
                        if (stockQty.isEmpty()) {
                            stockQty = "0";
                        }
                    }
                    int stq = Integer.parseInt(stockQty);
                    if (stq > num) {
                        int add = num + 1;
                        int tp = Integer.parseInt(cartItemLists.get(getAdapterPosition()).getCardRate());
                        tp = tp * add;
                        cartItemCount.setText(String.valueOf(add));
                        String topr = "৳ "+ String.valueOf(tp);
                        cartItemWisePrice.setText(topr);
                        cartItemLists.get(getAdapterPosition()).setItem_qty(String.valueOf(add));
                        cartItemLists.get(getAdapterPosition()).setItem_total_price(String.valueOf(tp));
                    }
                    else {
                        Toast.makeText(context,"Sorry, stock limit reached",Toast.LENGTH_SHORT).show();
                    }
                    OrderCart.TotalPriceCheck();


                }
            });

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Drawable drawable = new BitmapDrawable(context.getResources(), cartItemLists.get(getAdapterPosition()).getBitmap());
                    String text = cartItemCount.getText().toString();
                    int num = Integer.parseInt(text);

                    if (num == 1) {
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context,R.style.Theme_MyApp_Dialog_Alert);
                        builder.setTitle(cartItemLists.get(getAdapterPosition()).getIem_name()).setIcon(drawable)
                                .setMessage("Are you want to remove this item from Cart?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        cartItemLists.remove(getAdapterPosition());

                                        OrderCart.TotalPriceCheck();
                                        notifyItemRemoved(getAdapterPosition());

                                    }
                                })
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else {
                        int minus = num -1;
                        int tp = Integer.parseInt(cartItemLists.get(getAdapterPosition()).getCardRate());
                        tp = tp * minus;
                        cartItemCount.setText(String.valueOf(minus));
                        String topr = "৳ "+ String.valueOf(tp);
                        cartItemWisePrice.setText(topr);
                        cartItemLists.get(getAdapterPosition()).setItem_qty(String.valueOf(minus));
                        cartItemLists.get(getAdapterPosition()).setItem_total_price(String.valueOf(tp));

                        OrderCart.TotalPriceCheck();
                    }
                }
            });
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
