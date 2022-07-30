package com.shuvo.ttit.terrainshop.search.adapters;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shuvo.ttit.terrainshop.R;
import com.shuvo.ttit.terrainshop.cart.lists.CartItemList;
import com.shuvo.ttit.terrainshop.products.Products;
import com.shuvo.ttit.terrainshop.products.adapters.ProductAdapter;
import com.shuvo.ttit.terrainshop.products.lists.ProductList;
import com.shuvo.ttit.terrainshop.search.SearchProduct;
import com.shuvo.ttit.terrainshop.search.lists.SearchProductList;

import java.util.ArrayList;

import static com.shuvo.ttit.terrainshop.cart.OrderCart.myCartList;

public class SearchProductAdapter extends RecyclerView.Adapter<SearchProductAdapter.SearchHolder> {

    private final ArrayList<SearchProductList> productLists;
    private final Context context;
    private final ClickedItem myClickedItem;

    public SearchProductAdapter(ArrayList<SearchProductList> productLists, Context context, ClickedItem myClickedItem) {
        this.productLists = productLists;
        this.context = context;
        this.myClickedItem = myClickedItem;
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == 2) {
            view = LayoutInflater.from(context).inflate(R.layout.products_item_layout,parent,false);
        }
        else {
            view = LayoutInflater.from(context).inflate(R.layout.products_item_layout,parent,false);
        }
        return new SearchHolder(view,myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHolder holder, int position) {
        SearchProductList list = productLists.get(position);

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

    @Override
    public int getItemViewType(int position) {

        if (productLists.size() % 2 != 0) {
            if (position == productLists.size() -1) {
                return 1;
            }
            else {
                return 1;
            }
        } else  {
            return 1;
        }

    }

    @Override
    public int getItemCount() {
        return productLists.size();
    }

    public class SearchHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageView;
        public TextView itemName;
        public TextView itemCardPrice;
        public TextView itemRealPrice;
        public TextView itemPackQty;
        public Button addToCart;
        public ImageView itemSpecify;
        public ImageView outStockImage;
        public LinearLayout outStockLay;

        ClickedItem mClickedItem;

        public SearchHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            imageView = itemView.findViewById(R.id.products_item_image);
            itemName = itemView.findViewById(R.id.products_item_name);
            itemCardPrice = itemView.findViewById(R.id.products_item_card_rate_price);
            itemRealPrice = itemView.findViewById(R.id.products_item_real_rate_price);
            itemPackQty = itemView.findViewById(R.id.products_item_packaging_qty);
            addToCart = itemView.findViewById(R.id.products_item_add_to_cart_button);
            itemSpecify = itemView.findViewById(R.id.image_of_type_of_item_products);
            outStockImage = itemView.findViewById(R.id.out_stock_image_products);
            outStockLay = itemView.findViewById(R.id.out_of_stock_lay_products);

            this.mClickedItem = ci;
            itemView.setOnClickListener(this);

            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String item_id = productLists.get(getAdapterPosition()).getIem_item_id();
                    int a = 0;
                    for (int i = 0 ; i < myCartList.size(); i++) {
                        if (item_id.equals(myCartList.get(i).getIem_item_id())) {
                            String qty = myCartList.get(i).getItem_qty();
                            String stockQty = productLists.get(getAdapterPosition()).getStock_qty();
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
                        String t_price = productLists.get(getAdapterPosition()).getCard_rate();
                        String iem_id = productLists.get(getAdapterPosition()).getIem_id();
                        String iem_iem_id = productLists.get(getAdapterPosition()).getIem_iem_id();
                        String pName = productLists.get(getAdapterPosition()).getIem_name();
                        String pPrice = productLists.get(getAdapterPosition()).getCard_rate();
                        String pQty = productLists.get(getAdapterPosition()).getPackaging_unit_qty();
                        String itemUnit = productLists.get(getAdapterPosition()).getItem_unit();
                        Bitmap frontImage = productLists.get(getAdapterPosition()).getImage();
                        String realPrice = productLists.get(getAdapterPosition()).getReal_rate();
                        String disType = productLists.get(getAdapterPosition()).getDiscount_type();
                        String disValue = productLists.get(getAdapterPosition()).getDiscount_value();
                        String stockQty = productLists.get(getAdapterPosition()).getStock_qty();
                        myCartList.add(new CartItemList(iem_id,iem_iem_id,item_id,pName,pPrice,pQty,itemUnit,frontImage,i_qty,t_price,realPrice,disType,disValue,stockQty));
                        Toast.makeText(context,"Item added to Cart",Toast.LENGTH_SHORT).show();
                    }

                    SearchProduct.CartItemCheckSearchProducts();
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
