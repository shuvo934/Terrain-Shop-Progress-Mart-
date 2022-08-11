package com.shuvo.ttit.terrainshop.adminlogin.reports.itemstock.lists;

public class WareHouseQtyList {

    private String serial_no;
    private String warehouse;
    private String qty;

    public WareHouseQtyList(String serial_no, String warehouse, String qty) {
        this.serial_no = serial_no;
        this.warehouse = warehouse;
        this.qty = qty;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
