package com.shuvo.ttit.terrainshop.myorders.lists;

import android.graphics.Bitmap;

public class OrderDetailsList {

    private String sod_item_id;
    private String sod_qty;
    private String item_real_rate;
    private String item_card_rate;
    private String order_rate;
    private String item_name;
    private String dis_type;
    private Bitmap bitmap;
    private String unit;

    public OrderDetailsList(String sod_item_id, String sod_qty, String item_real_rate, String item_card_rate, String order_rate, String item_name,String dis_type, Bitmap bitmap,String unit) {
        this.sod_item_id = sod_item_id;
        this.sod_qty = sod_qty;
        this.item_real_rate = item_real_rate;
        this.item_card_rate = item_card_rate;
        this.order_rate = order_rate;
        this.item_name = item_name;
        this.dis_type = dis_type;
        this.bitmap = bitmap;
        this.unit = unit;
    }

    public String getSod_item_id() {
        return sod_item_id;
    }

    public void setSod_item_id(String sod_item_id) {
        this.sod_item_id = sod_item_id;
    }

    public String getSod_qty() {
        return sod_qty;
    }

    public void setSod_qty(String sod_qty) {
        this.sod_qty = sod_qty;
    }

    public String getItem_real_rate() {
        return item_real_rate;
    }

    public void setItem_real_rate(String item_real_rate) {
        this.item_real_rate = item_real_rate;
    }

    public String getOrder_rate() {
        return order_rate;
    }

    public void setOrder_rate(String order_rate) {
        this.order_rate = order_rate;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getItem_card_rate() {
        return item_card_rate;
    }

    public void setItem_card_rate(String item_card_rate) {
        this.item_card_rate = item_card_rate;
    }

    public String getDis_type() {
        return dis_type;
    }

    public void setDis_type(String dis_type) {
        this.dis_type = dis_type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
