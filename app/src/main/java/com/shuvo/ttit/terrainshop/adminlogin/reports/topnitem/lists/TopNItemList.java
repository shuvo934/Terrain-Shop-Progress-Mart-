package com.shuvo.ttit.terrainshop.adminlogin.reports.topnitem.lists;

public class TopNItemList {
    private String sl_no;
    private String item_id;
    private String item_code;
    private String hs_code;
    private String item_name;
    private String part_no;
    private String unit;
    private String cat_id;
    private String cat_name;
    private String subCat_id;
    private String subCat_name;
    private String quantity;
    private String amount;

    public TopNItemList(String sl_no, String item_id, String item_code, String hs_code, String item_name, String part_no, String unit, String cat_id, String cat_name, String subCat_id, String subCat_name, String quantity, String amount) {
        this.sl_no = sl_no;
        this.item_id = item_id;
        this.item_code = item_code;
        this.hs_code = hs_code;
        this.item_name = item_name;
        this.part_no = part_no;
        this.unit = unit;
        this.cat_id = cat_id;
        this.cat_name = cat_name;
        this.subCat_id = subCat_id;
        this.subCat_name = subCat_name;
        this.quantity = quantity;
        this.amount = amount;
    }

    public String getSl_no() {
        return sl_no;
    }

    public void setSl_no(String sl_no) {
        this.sl_no = sl_no;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getHs_code() {
        return hs_code;
    }

    public void setHs_code(String hs_code) {
        this.hs_code = hs_code;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getPart_no() {
        return part_no;
    }

    public void setPart_no(String part_no) {
        this.part_no = part_no;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getSubCat_id() {
        return subCat_id;
    }

    public void setSubCat_id(String subCat_id) {
        this.subCat_id = subCat_id;
    }

    public String getSubCat_name() {
        return subCat_name;
    }

    public void setSubCat_name(String subCat_name) {
        this.subCat_name = subCat_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}

