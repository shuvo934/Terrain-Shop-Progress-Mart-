package com.shuvo.ttit.terrainshop.adminlogin.reports.itemstock.lists;

import java.util.ArrayList;

public class ItemWiseStockList {

    private String slNo;
    private String cate_name;
    private String sub_cate_name;
    private String item_name;
    private String item_code;
    private String item_unit;
    private ArrayList<WareHouseQtyList> item_qty;
    private String vat;
    private String purchase_amnt;
    private String sales_price;
    private String hs_code;

    public ItemWiseStockList(String slNo, String cate_name, String sub_cate_name, String item_name, String item_code, String item_unit, ArrayList<WareHouseQtyList> item_qty, String vat, String purchase_amnt, String sales_price, String hs_code) {
        this.slNo = slNo;
        this.cate_name = cate_name;
        this.sub_cate_name = sub_cate_name;
        this.item_name = item_name;
        this.item_code = item_code;
        this.item_unit = item_unit;
        this.item_qty = item_qty;
        this.vat = vat;
        this.purchase_amnt = purchase_amnt;
        this.sales_price = sales_price;
        this.hs_code = hs_code;
    }

    public String getSlNo() {
        return slNo;
    }

    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    public String getSub_cate_name() {
        return sub_cate_name;
    }

    public void setSub_cate_name(String sub_cate_name) {
        this.sub_cate_name = sub_cate_name;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getItem_unit() {
        return item_unit;
    }

    public void setItem_unit(String item_unit) {
        this.item_unit = item_unit;
    }

    public ArrayList<WareHouseQtyList> getItem_qty() {
        return item_qty;
    }

    public void setItem_qty(ArrayList<WareHouseQtyList> item_qty) {
        this.item_qty = item_qty;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getPurchase_amnt() {
        return purchase_amnt;
    }

    public void setPurchase_amnt(String purchase_amnt) {
        this.purchase_amnt = purchase_amnt;
    }

    public String getSales_price() {
        return sales_price;
    }

    public void setSales_price(String sales_price) {
        this.sales_price = sales_price;
    }

    public String getHs_code() {
        return hs_code;
    }

    public void setHs_code(String hs_code) {
        this.hs_code = hs_code;
    }
}
