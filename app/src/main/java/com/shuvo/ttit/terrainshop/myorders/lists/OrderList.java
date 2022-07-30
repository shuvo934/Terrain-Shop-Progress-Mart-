package com.shuvo.ttit.terrainshop.myorders.lists;

public class OrderList {

    private String som_id;
    private String som_order_no;
    private String som_date;
    private String som_order_no_id;
    private String delivery_date;
    private String delivery_address;
    private String discount_type;
    private String discount_value;
    private String advance_amnt;
    private String delivery_charge;
    private String total_order_price;
    private String delivery_time;
    private String order_tracking;

    public OrderList(String som_id, String som_order_no, String som_date, String som_order_no_id, String delivery_date, String delivery_address, String discount_type, String discount_value, String advance_amnt, String delivery_charge, String total_order_price,String delivery_time,String order_tracking) {
        this.som_id = som_id;
        this.som_order_no = som_order_no;
        this.som_date = som_date;
        this.som_order_no_id = som_order_no_id;
        this.delivery_date = delivery_date;
        this.delivery_address = delivery_address;
        this.discount_type = discount_type;
        this.discount_value = discount_value;
        this.advance_amnt = advance_amnt;
        this.delivery_charge = delivery_charge;
        this.total_order_price = total_order_price;
        this.delivery_time = delivery_time;
        this.order_tracking = order_tracking;
    }

    public String getSom_id() {
        return som_id;
    }

    public void setSom_id(String som_id) {
        this.som_id = som_id;
    }

    public String getSom_order_no() {
        return som_order_no;
    }

    public void setSom_order_no(String som_order_no) {
        this.som_order_no = som_order_no;
    }

    public String getSom_date() {
        return som_date;
    }

    public void setSom_date(String som_date) {
        this.som_date = som_date;
    }

    public String getSom_order_no_id() {
        return som_order_no_id;
    }

    public void setSom_order_no_id(String som_order_no_id) {
        this.som_order_no_id = som_order_no_id;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
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

    public String getAdvance_amnt() {
        return advance_amnt;
    }

    public void setAdvance_amnt(String advance_amnt) {
        this.advance_amnt = advance_amnt;
    }

    public String getTotal_order_price() {
        return total_order_price;
    }

    public void setTotal_order_price(String total_order_price) {
        this.total_order_price = total_order_price;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(String delivery_charge) {
        this.delivery_charge = delivery_charge;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getOrder_tracking() {
        return order_tracking;
    }

    public void setOrder_tracking(String order_tracking) {
        this.order_tracking = order_tracking;
    }
}
