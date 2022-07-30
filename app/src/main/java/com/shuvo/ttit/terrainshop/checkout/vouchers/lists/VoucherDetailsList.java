package com.shuvo.ttit.terrainshop.checkout.vouchers.lists;

public class VoucherDetailsList {

    private String dpcd_id;
    private String dpcd_dpcm_id;
    private String from_amnt;
    private String dis_type;
    private String dis_val;
    private String to_amnt;

    public VoucherDetailsList(String dpcd_id, String dpcd_dpcm_id, String from_amnt, String dis_type, String dis_val, String to_amnt) {
        this.dpcd_id = dpcd_id;
        this.dpcd_dpcm_id = dpcd_dpcm_id;
        this.from_amnt = from_amnt;
        this.dis_type = dis_type;
        this.dis_val = dis_val;
        this.to_amnt = to_amnt;
    }

    public String getDpcd_id() {
        return dpcd_id;
    }

    public void setDpcd_id(String dpcd_id) {
        this.dpcd_id = dpcd_id;
    }

    public String getDpcd_dpcm_id() {
        return dpcd_dpcm_id;
    }

    public void setDpcd_dpcm_id(String dpcd_dpcm_id) {
        this.dpcd_dpcm_id = dpcd_dpcm_id;
    }

    public String getFrom_amnt() {
        return from_amnt;
    }

    public void setFrom_amnt(String from_amnt) {
        this.from_amnt = from_amnt;
    }

    public String getDis_type() {
        return dis_type;
    }

    public void setDis_type(String dis_type) {
        this.dis_type = dis_type;
    }

    public String getDis_val() {
        return dis_val;
    }

    public void setDis_val(String dis_val) {
        this.dis_val = dis_val;
    }

    public String getTo_amnt() {
        return to_amnt;
    }

    public void setTo_amnt(String to_amnt) {
        this.to_amnt = to_amnt;
    }
}
