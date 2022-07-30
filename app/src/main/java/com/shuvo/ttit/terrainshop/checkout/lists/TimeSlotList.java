package com.shuvo.ttit.terrainshop.checkout.lists;

public class TimeSlotList {
    private String tsm_id;
    private String tsm_name;
    private String tsm_active_status;
    private String tsm_details;

    public TimeSlotList(String tsm_id, String tsm_name, String tsm_active_status, String tsm_details) {
        this.tsm_id = tsm_id;
        this.tsm_name = tsm_name;
        this.tsm_active_status = tsm_active_status;
        this.tsm_details = tsm_details;
    }

    public String getTsm_id() {
        return tsm_id;
    }

    public void setTsm_id(String tsm_id) {
        this.tsm_id = tsm_id;
    }

    public String getTsm_name() {
        return tsm_name;
    }

    public void setTsm_name(String tsm_name) {
        this.tsm_name = tsm_name;
    }

    public String getTsm_active_status() {
        return tsm_active_status;
    }

    public void setTsm_active_status(String tsm_active_status) {
        this.tsm_active_status = tsm_active_status;
    }

    public String getTsm_details() {
        return tsm_details;
    }

    public void setTsm_details(String tsm_details) {
        this.tsm_details = tsm_details;
    }
}
