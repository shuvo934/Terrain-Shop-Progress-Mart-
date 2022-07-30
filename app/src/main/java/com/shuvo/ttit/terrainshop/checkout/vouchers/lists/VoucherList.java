package com.shuvo.ttit.terrainshop.checkout.vouchers.lists;

import java.util.ArrayList;

public class VoucherList {

    private String dpcm_id;
    private String dpcm_div_id;
    private String voucher_code;
    private String from_date;
    private String to_date;
    private ArrayList<VoucherDetailsList> voucherDetailsLists;

    public VoucherList(String dpcm_id, String dpcm_div_id, String voucher_code, String from_date, String to_date, ArrayList<VoucherDetailsList> voucherDetailsLists) {
        this.dpcm_id = dpcm_id;
        this.dpcm_div_id = dpcm_div_id;
        this.voucher_code = voucher_code;
        this.from_date = from_date;
        this.to_date = to_date;
        this.voucherDetailsLists = voucherDetailsLists;
    }

    public String getDpcm_id() {
        return dpcm_id;
    }

    public void setDpcm_id(String dpcm_id) {
        this.dpcm_id = dpcm_id;
    }

    public String getDpcm_div_id() {
        return dpcm_div_id;
    }

    public void setDpcm_div_id(String dpcm_div_id) {
        this.dpcm_div_id = dpcm_div_id;
    }

    public String getVoucher_code() {
        return voucher_code;
    }

    public void setVoucher_code(String voucher_code) {
        this.voucher_code = voucher_code;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public ArrayList<VoucherDetailsList> getVoucherDetailsLists() {
        return voucherDetailsLists;
    }

    public void setVoucherDetailsLists(ArrayList<VoucherDetailsList> voucherDetailsLists) {
        this.voucherDetailsLists = voucherDetailsLists;
    }
}
