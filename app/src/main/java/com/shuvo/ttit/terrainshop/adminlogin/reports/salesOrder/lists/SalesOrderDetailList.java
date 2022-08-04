package com.shuvo.ttit.terrainshop.adminlogin.reports.salesOrder.lists;

public class SalesOrderDetailList {

    private String item_name;
    private String hs_code;
    private String part_no;
    private String qty;
    private String unit;
    private String item_price;
    private String dis_val;
    private String dis_type;
    private String item_actual_rate;
    private String total_amnt;
    private String delivered_qty;
    private String return_qty;
    private String balance_qty;
    private String sample_qty;
    private String sample_balance_qty;
    private String delivered_samp_qty;

    public SalesOrderDetailList(String item_name, String hs_code, String part_no, String qty, String unit, String item_price, String dis_val, String dis_type, String item_actual_rate, String total_amnt, String delivered_qty, String return_qty, String balance_qty, String sample_qty, String sample_balance_qty, String delivered_samp_qty) {
        this.item_name = item_name;
        this.hs_code = hs_code;
        this.part_no = part_no;
        this.qty = qty;
        this.unit = unit;
        this.item_price = item_price;
        this.dis_val = dis_val;
        this.dis_type = dis_type;
        this.item_actual_rate = item_actual_rate;
        this.total_amnt = total_amnt;
        this.delivered_qty = delivered_qty;
        this.return_qty = return_qty;
        this.balance_qty = balance_qty;
        this.sample_qty = sample_qty;
        this.sample_balance_qty = sample_balance_qty;
        this.delivered_samp_qty = delivered_samp_qty;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getHs_code() {
        return hs_code;
    }

    public void setHs_code(String hs_code) {
        this.hs_code = hs_code;
    }

    public String getPart_no() {
        return part_no;
    }

    public void setPart_no(String part_no) {
        this.part_no = part_no;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getDis_val() {
        return dis_val;
    }

    public void setDis_val(String dis_val) {
        this.dis_val = dis_val;
    }

    public String getDis_type() {
        return dis_type;
    }

    public void setDis_type(String dis_type) {
        this.dis_type = dis_type;
    }

    public String getItem_actual_rate() {
        return item_actual_rate;
    }

    public void setItem_actual_rate(String item_actual_rate) {
        this.item_actual_rate = item_actual_rate;
    }

    public String getTotal_amnt() {
        return total_amnt;
    }

    public void setTotal_amnt(String total_amnt) {
        this.total_amnt = total_amnt;
    }

    public String getDelivered_qty() {
        return delivered_qty;
    }

    public void setDelivered_qty(String delivered_qty) {
        this.delivered_qty = delivered_qty;
    }

    public String getReturn_qty() {
        return return_qty;
    }

    public void setReturn_qty(String return_qty) {
        this.return_qty = return_qty;
    }

    public String getBalance_qty() {
        return balance_qty;
    }

    public void setBalance_qty(String balance_qty) {
        this.balance_qty = balance_qty;
    }

    public String getSample_qty() {
        return sample_qty;
    }

    public void setSample_qty(String sample_qty) {
        this.sample_qty = sample_qty;
    }

    public String getSample_balance_qty() {
        return sample_balance_qty;
    }

    public void setSample_balance_qty(String sample_balance_qty) {
        this.sample_balance_qty = sample_balance_qty;
    }

    public String getDelivered_samp_qty() {
        return delivered_samp_qty;
    }

    public void setDelivered_samp_qty(String delivered_samp_qty) {
        this.delivered_samp_qty = delivered_samp_qty;
    }
}
