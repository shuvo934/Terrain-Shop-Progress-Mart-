package com.shuvo.ttit.terrainshop.adminlogin.reports.deliveryregister.lists;

public class DeliveryOrderList {

    private String sm_id;
    private String delivery_no;
    private String delivery_date;
    private String client_name;
    private String order_type;
    private String order_no;
    private String order_date;
    private String invoice_amnt;
    private String delivery_charge;
    private String ad_id;
    private String edd;
    private String ad_code;
    private String target_address;
    private String contact_person;
    private String contact_number;
    private String contact_email;
    private String division_name;
    private String location_name;

    public DeliveryOrderList(String sm_id, String delivery_no, String delivery_date, String client_name, String order_type, String order_no, String order_date, String invoice_amnt, String delivery_charge, String ad_id, String edd, String ad_code, String target_address, String contact_person, String contact_number, String contact_email, String division_name, String location_name) {
        this.sm_id = sm_id;
        this.delivery_no = delivery_no;
        this.delivery_date = delivery_date;
        this.client_name = client_name;
        this.order_type = order_type;
        this.order_no = order_no;
        this.order_date = order_date;
        this.invoice_amnt = invoice_amnt;
        this.delivery_charge = delivery_charge;
        this.ad_id = ad_id;
        this.edd = edd;
        this.ad_code = ad_code;
        this.target_address = target_address;
        this.contact_person = contact_person;
        this.contact_number = contact_number;
        this.contact_email = contact_email;
        this.division_name = division_name;
        this.location_name = location_name;
    }

    public String getSm_id() {
        return sm_id;
    }

    public void setSm_id(String sm_id) {
        this.sm_id = sm_id;
    }

    public String getDelivery_no() {
        return delivery_no;
    }

    public void setDelivery_no(String delivery_no) {
        this.delivery_no = delivery_no;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getInvoice_amnt() {
        return invoice_amnt;
    }

    public void setInvoice_amnt(String invoice_amnt) {
        this.invoice_amnt = invoice_amnt;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(String delivery_charge) {
        this.delivery_charge = delivery_charge;
    }

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }

    public String getEdd() {
        return edd;
    }

    public void setEdd(String edd) {
        this.edd = edd;
    }

    public String getAd_code() {
        return ad_code;
    }

    public void setAd_code(String ad_code) {
        this.ad_code = ad_code;
    }

    public String getTarget_address() {
        return target_address;
    }

    public void setTarget_address(String target_address) {
        this.target_address = target_address;
    }

    public String getContact_person() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getDivision_name() {
        return division_name;
    }

    public void setDivision_name(String division_name) {
        this.division_name = division_name;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }
}
