package com.shuvo.ttit.terrainshop.homepage.fragments.lists;

import android.graphics.Bitmap;

public class NewArrivalItemList {

    private String iem_id;
    private String iem_iem_id;
    private String iem_type;
    private String iem_name;
    private Bitmap image;
    private String iem_item_id;
    private String item_unit;
    private String packaging_unit_qty;
    private String card_rate;
    private String card_other_info;
    private String discount_dtl;
    private String new_tag;
    private String stock_availability;
    private String real_rate;
    private String itemDetails;
    private String discount_type;
    private String discount_value;
    private String stock_qty;

    public NewArrivalItemList(String iem_id, String iem_iem_id, String iem_type, String iem_name, Bitmap image, String iem_item_id, String item_unit, String packaging_unit_qty, String card_rate, String card_other_info, String discount_dtl, String new_tag, String stock_availability, String real_rate, String itemDetails, String discount_type, String discount_value,String stock_qty) {
        this.iem_id = iem_id;
        this.iem_iem_id = iem_iem_id;
        this.iem_type = iem_type;
        this.iem_name = iem_name;
        this.image = image;
        this.iem_item_id = iem_item_id;
        this.item_unit = item_unit;
        this.packaging_unit_qty = packaging_unit_qty;
        this.card_rate = card_rate;
        this.card_other_info = card_other_info;
        this.discount_dtl = discount_dtl;
        this.new_tag = new_tag;
        this.stock_availability = stock_availability;
        this.real_rate = real_rate;
        this.itemDetails = itemDetails;
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

    public String getIem_type() {
        return iem_type;
    }

    public void setIem_type(String iem_type) {
        this.iem_type = iem_type;
    }

    public String getIem_name() {
        return iem_name;
    }

    public void setIem_name(String iem_name) {
        this.iem_name = iem_name;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getIem_item_id() {
        return iem_item_id;
    }

    public void setIem_item_id(String iem_item_id) {
        this.iem_item_id = iem_item_id;
    }

    public String getItem_unit() {
        return item_unit;
    }

    public void setItem_unit(String item_unit) {
        this.item_unit = item_unit;
    }

    public String getPackaging_unit_qty() {
        return packaging_unit_qty;
    }

    public void setPackaging_unit_qty(String packaging_unit_qty) {
        this.packaging_unit_qty = packaging_unit_qty;
    }

    public String getCard_rate() {
        return card_rate;
    }

    public void setCard_rate(String card_rate) {
        this.card_rate = card_rate;
    }

    public String getNew_tag() {
        return new_tag;
    }

    public void setNew_tag(String new_tag) {
        this.new_tag = new_tag;
    }

    public String getStock_availability() {
        return stock_availability;
    }

    public void setStock_availability(String stock_availability) {
        this.stock_availability = stock_availability;
    }

    public String getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(String itemDetails) {
        this.itemDetails = itemDetails;
    }

    public String getCard_other_info() {
        return card_other_info;
    }

    public void setCard_other_info(String card_other_info) {
        this.card_other_info = card_other_info;
    }

    public String getDiscount_dtl() {
        return discount_dtl;
    }

    public void setDiscount_dtl(String discount_dtl) {
        this.discount_dtl = discount_dtl;
    }

    public String getReal_rate() {
        return real_rate;
    }

    public void setReal_rate(String real_rate) {
        this.real_rate = real_rate;
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
