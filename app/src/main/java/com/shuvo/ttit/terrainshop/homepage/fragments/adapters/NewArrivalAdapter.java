package com.shuvo.ttit.terrainshop.homepage.fragments.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.cart.lists.CartItemList;
import com.shuvo.ttit.terrainshop.homepage.HomePage;
import com.shuvo.ttit.terrainshop.homepage.fragments.lists.NewArrivalItemList;
import com.shuvo.ttit.terrainshop.products.Products;

import java.util.ArrayList;

import static com.shuvo.ttit.terrainshop.cart.OrderCart.myCartList;

public class NewArrivalAdapter extends RecyclerView.Adapter<NewArrivalAdapter.NewArrivalItemHolder> {

    private final ArrayList<NewArrivalItemList> newArrivalItemLists;
    private final Context context;
    private final ClickedItem myClickedItem;


    public NewArrivalAdapter(ArrayList<NewArrivalItemList> newArrivalItemLists, Context context, ClickedItem myClickedItem) {
        this.newArrivalItemLists = newArrivalItemLists;
        this.context = context;
        this.myClickedItem = myClickedItem;
    }

    @NonNull
    @Override
    public NewArrivalItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_layout, parent, false);
        return new NewArrivalItemHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull NewArrivalItemHolder holder, int position) {

        NewArrivalItemList list = newArrivalItemLists.get(position);

        if (newArrivalItemLists.size() >= 11) {
            if (position < 10)  {
                holder.itemCard.setVisibility(View.VISIBLE);

                String item_name = list.getIem_name();

                if (list.getDiscount_dtl() != null) {
                    if (!list.getDiscount_dtl().isEmpty()) {
                        if (list.getDiscount_dtl().contains("%")) {
                            item_name = item_name + " ("+list.getDiscount_dtl()+" off)";
                        }
                        else {
                            item_name = item_name + " (৳ "+list.getDiscount_dtl()+" off)";
                        }
                    }
                }

                SpannableStringBuilder ss1 = new SpannableStringBuilder(item_name);
                if (item_name.contains(" off)") ) {
                    int firstIndex = item_name.lastIndexOf("(");
                    int lastIndex = item_name.lastIndexOf(")");

                    ss1.setSpan(new ForegroundColorSpan(Color.rgb(192, 57, 43)), firstIndex, lastIndex+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss1.setSpan(new RelativeSizeSpan(0.8f),firstIndex,lastIndex+1,0);

                }

                holder.itemName.setText(ss1);


                String packQty = "";

                if (list.getPackaging_unit_qty().equals(" ")) {

                    holder.itemPackQty.setVisibility(View.VISIBLE);
                    packQty = "1 " + list.getItem_unit();
                    holder.itemPackQty.setText(packQty);
                } else {
                    holder.itemPackQty.setVisibility(View.VISIBLE);
                    packQty = "1 " + list.getItem_unit() + " ( " + list.getPackaging_unit_qty() + " )";
                    holder.itemPackQty.setText(packQty);
                }

                if (list.getDiscount_dtl() != null) {
                    if (!list.getDiscount_dtl().isEmpty()) {
                        holder.itemRealPrice.setVisibility(View.VISIBLE);
                        String realP = "৳ " + list.getReal_rate();
                        holder.itemRealPrice.setText(realP);
                        holder.itemRealPrice.setPaintFlags(holder.itemRealPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                    else {
                        holder.itemRealPrice.setVisibility(View.GONE);
                    }
                }
                else {
                    holder.itemRealPrice.setVisibility(View.GONE);
                }

                String cardP = "৳ " + list.getCard_rate();
                holder.itemCardPrice.setText(cardP);

                Glide.with(holder.itemView)
                        .load(list.getImage())
                        .fitCenter()
                        .into(holder.imageView);

                holder.viewAll.setVisibility(View.GONE);

                if (list.getNew_tag().equals("NEW")){
                    holder.itemSpecify.setVisibility(View.VISIBLE);
                }
                else {
                    holder.itemSpecify.setVisibility(View.GONE);
                }

                if (list.getStock_availability().equals("StockOut")) {
                    holder.outStockLay.setVisibility(View.VISIBLE);
                    holder.outStockImage.setVisibility(View.VISIBLE);
                    holder.addToCart.setEnabled(false);
                }
                else {
                    holder.outStockLay.setVisibility(View.GONE);
                    holder.outStockImage.setVisibility(View.GONE);
                    holder.addToCart.setEnabled(true);
                }


            } else if (position == 10){
                holder.viewAll.setVisibility(View.VISIBLE);
                holder.itemCard.setVisibility(View.GONE);
            } else {
                holder.viewAll.setVisibility(View.GONE);
                holder.itemCard.setVisibility(View.GONE);
            }
        } else {

            holder.itemCard.setVisibility(View.VISIBLE);

            String item_name = list.getIem_name();

            if (list.getDiscount_dtl() != null) {
                if (!list.getDiscount_dtl().isEmpty()) {
                    if (list.getDiscount_dtl().contains("%")) {
                        item_name = item_name + " ("+list.getDiscount_dtl()+" off)";
                    }
                    else {
                        item_name = item_name + " (৳ "+list.getDiscount_dtl()+" off)";
                    }
                }
            }

            SpannableStringBuilder ss1 = new SpannableStringBuilder(item_name);
            if (item_name.contains(" off)") ) {
                int firstIndex = item_name.lastIndexOf("(");
                int lastIndex = item_name.lastIndexOf(")");

                ss1.setSpan(new ForegroundColorSpan(Color.rgb(192, 57, 43)), firstIndex, lastIndex+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss1.setSpan(new RelativeSizeSpan(0.8f),firstIndex,lastIndex+1,0);

            }

            holder.itemName.setText(ss1);


            String packQty = "";

            if (list.getPackaging_unit_qty().equals(" ")) {
                holder.itemPackQty.setVisibility(View.VISIBLE);
                packQty = "1 " + list.getItem_unit();
                holder.itemPackQty.setText(packQty);
            } else {
                holder.itemPackQty.setVisibility(View.VISIBLE);
                packQty = "1 " + list.getItem_unit() + " ( " + list.getPackaging_unit_qty() + " )";
                holder.itemPackQty.setText(packQty);
            }

            if (list.getDiscount_dtl() != null) {
                if (!list.getDiscount_dtl().isEmpty()) {
                    holder.itemRealPrice.setVisibility(View.VISIBLE);
                    String realP = "৳ " + list.getReal_rate();
                    holder.itemRealPrice.setText(realP);
                    holder.itemRealPrice.setPaintFlags(holder.itemRealPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                else {
                    holder.itemRealPrice.setVisibility(View.GONE);
                }
            }
            else {
                holder.itemRealPrice.setVisibility(View.GONE);
            }

            String cardP = "৳ " + list.getCard_rate();
            holder.itemCardPrice.setText(cardP);

            Glide.with(holder.itemView)
                    .load(list.getImage())
                    .fitCenter()
                    .into(holder.imageView);

            holder.viewAll.setVisibility(View.GONE);

            if (list.getNew_tag().equals("NEW")){
                holder.itemSpecify.setVisibility(View.VISIBLE);
            }
            else {
                holder.itemSpecify.setVisibility(View.GONE);
            }

            if (list.getStock_availability().equals("StockOut")) {
                holder.outStockLay.setVisibility(View.VISIBLE);
                holder.outStockImage.setVisibility(View.VISIBLE);
                holder.addToCart.setEnabled(false);
            }
            else {
                holder.outStockLay.setVisibility(View.GONE);
                holder.outStockImage.setVisibility(View.GONE);
                holder.addToCart.setEnabled(true);
            }
        }

    }

    @Override
    public int getItemCount() {
        return newArrivalItemLists.size();
    }

    public class NewArrivalItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageView;
        public TextView itemName;
        public TextView itemCardPrice;
        public TextView itemRealPrice;
        public TextView itemPackQty;
        public Button addToCart;
        public RelativeLayout viewAll;
        public CardView itemCard;
        public ImageView itemSpecify;
        public ImageView outStockImage;
        public LinearLayout outStockLay;

        ClickedItem mClickedItem;

        public NewArrivalItemHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
            itemName = itemView.findViewById(R.id.item_name);
            itemCardPrice = itemView.findViewById(R.id.item_card_rate_price);
            itemRealPrice = itemView.findViewById(R.id.item_real_rate_price);
            itemPackQty = itemView.findViewById(R.id.item_packaging_qty);
            addToCart = itemView.findViewById(R.id.item_add_to_cart_button);
            viewAll = itemView.findViewById(R.id.view_all_layout);
            itemCard = itemView.findViewById(R.id.item_card);
            itemSpecify = itemView.findViewById(R.id.image_of_type_of_item);
            outStockImage = itemView.findViewById(R.id.out_stock_image);
            outStockLay = itemView.findViewById(R.id.out_of_stock_lay);

            viewAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Intent intent = new Intent(context, Products.class);
                    intent.putExtra("IEM ID", "1");
                    intent.putExtra("IEM NAME", "New Arrivals");
                    activity.startActivity(intent);
                }
            });

            this.mClickedItem = ci;
            itemView.setOnClickListener(this);

            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String item_id = newArrivalItemLists.get(getAdapterPosition()).getIem_item_id();
                    int a = 0;
                    for (int i = 0 ; i < myCartList.size(); i++) {
                        if (item_id.equals(myCartList.get(i).getIem_item_id())) {
                            String qty = myCartList.get(i).getItem_qty();
                            String stockQty = newArrivalItemLists.get(getAdapterPosition()).getStock_qty();
                            int qq = Integer.parseInt(qty);
                            if (stockQty == null) {
                                stockQty = "0";
                                if (stockQty.isEmpty()) {
                                    stockQty = "0";
                                }
                            }

                            int stq = Integer.parseInt(stockQty);
                            if (stq > qq) {
                                int newQQ = 1;
                                qq = qq + newQQ;
                                int total = qq * Integer.parseInt(myCartList.get(i).getCardRate());
                                myCartList.get(i).setItem_qty(String.valueOf(qq));
                                myCartList.get(i).setItem_total_price(String.valueOf(total));
                                myCartList.get(i).setStock_qty(stockQty);
                                Toast.makeText(context,"Item added to Cart",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(context,"Sorry, stock limit reached",Toast.LENGTH_SHORT).show();
                                myCartList.get(i).setStock_qty(stockQty);
                            }

                            a = 1;
                        }
                    }

                    if (a == 0) {
                        String i_qty = "1";
                        String t_price = newArrivalItemLists.get(getAdapterPosition()).getCard_rate();
                        String iem_id = newArrivalItemLists.get(getAdapterPosition()).getIem_id();
                        String iem_iem_id = newArrivalItemLists.get(getAdapterPosition()).getIem_iem_id();
                        String pName = newArrivalItemLists.get(getAdapterPosition()).getIem_name();
                        String pPrice = newArrivalItemLists.get(getAdapterPosition()).getCard_rate();
                        String pQty = newArrivalItemLists.get(getAdapterPosition()).getPackaging_unit_qty();
                        String itemUnit = newArrivalItemLists.get(getAdapterPosition()).getItem_unit();
                        Bitmap frontImage = newArrivalItemLists.get(getAdapterPosition()).getImage();
                        String realPrice = newArrivalItemLists.get(getAdapterPosition()).getReal_rate();
                        String disType = newArrivalItemLists.get(getAdapterPosition()).getDiscount_type();
                        String disValue = newArrivalItemLists.get(getAdapterPosition()).getDiscount_value();
                        String stockQty = newArrivalItemLists.get(getAdapterPosition()).getStock_qty();
                        myCartList.add(new CartItemList(iem_id,iem_iem_id,item_id,pName,pPrice,pQty,itemUnit,frontImage,i_qty,t_price,realPrice,disType,disValue,stockQty));
                        Toast.makeText(context,"Item added to Cart",Toast.LENGTH_SHORT).show();
                    }

                    HomePage.CartItemCheck();
                }
            });

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
}
