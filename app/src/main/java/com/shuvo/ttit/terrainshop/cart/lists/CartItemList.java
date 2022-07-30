package com.shuvo.ttit.terrainshop.cart.lists;

import android.graphics.Bitmap;

public class CartItemList {

    private String iem_id;
    private String iem_iem_id;
    private String iem_item_id;
    private String iem_name;
    private String cardRate;
    private String package_qty;
    private String item_unit;
    private Bitmap bitmap;
    private String item_qty;
    private String item_total_price;
    private String real_price;
    private String discount_type;
    private String discount_value;
    private String stock_qty;

    public CartItemList(String iem_id, String iem_iem_id, String iem_item_id, String iem_name, String cardRate, String package_qty, String item_unit, Bitmap bitmap, String item_qty, String item_total_price, String real_price, String discount_type, String discount_value, String stock_qty) {
        this.iem_id = iem_id;
        this.iem_iem_id = iem_iem_id;
        this.iem_item_id = iem_item_id;
        this.iem_name = iem_name;
        this.cardRate = cardRate;
        this.package_qty = package_qty;
        this.item_unit = item_unit;
        this.bitmap = bitmap;
        this.item_qty = item_qty;
        this.item_total_price = item_total_price;
        this.real_price = real_price;
        this.discount_type = discount_type;
        this.discount_value = discount_value;
        this.stock_qty = stock_qty;
    }

    public String getIem_id() {
        return iem_id;
    }

    public void setIem_id(String iem_id) {
        this.iem_id = iem_id;
    }

    public String getIem_iem_id() {
        return iem_iem_id;
    }

    public void setIem_iem_id(String iem_iem_id) {
        this.iem_iem_id = iem_iem_id;
    }

    public String getIem_item_id() {
        return iem_item_id;
    }

    public void setIem_item_id(String iem_item_id) {
        this.iem_item_id = iem_item_id;
    }

    public String getIem_name() {
        return iem_name;
    }

    public void setIem_name(String iem_name) {
        this.iem_name = iem_name;
    }

    public String getCardRate() {
        return cardRate;
    }

    public void setCardRate(String cardRate) {
        this.cardRate = cardRate;
    }

    public String getPackage_qty() {
        return package_qty;
    }

    public void setPackage_qty(String package_qty) {
        this.package_qty = package_qty;
    }

    public String getItem_unit() {
        return item_unit;
    }

    public void setItem_unit(String item_unit) {
        this.item_unit = item_unit;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getItem_qty() {
        return item_qty;
    }

    public void setItem_qty(String item_qty) {
        this.item_qty = item_qty;
    }

    public String getItem_total_price() {
        return item_total_price;
    }

    public void setItem_total_price(String item_total_price) {
        this.item_total_price = item_total_price;
    }

    public String getReal_price() {
        return real_price;
    }

    public void setReal_price(String real_price) {
        this.real_price = real_price;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    public String getDiscount_value() {
        return discount_value;
    }

    public void setDiscount_value(String discount_value) {
        this.discount_value = discount_value;
    }

    public String getStock_qty() {
        return stock_qty;
    }

    public void setStock_qty(String stock_qty) {
        this.stock_qty = stock_qty;
    }
}
