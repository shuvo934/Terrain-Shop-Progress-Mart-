package com.shuvo.ttit.terrainshop.subcategory.lists;

import android.graphics.Bitmap;

public class SubCategoryList {

    private String iem_id;
    private String iem_iem_id;
    private String iem_type;
    private String iem_name;
    private Bitmap iem_image;
    private String subBelow;

    public SubCategoryList(String iem_id, String iem_iem_id, String iem_type, String iem_name, Bitmap iem_image, String subBelow) {
        this.iem_id = iem_id;
        this.iem_iem_id = iem_iem_id;
        this.iem_type = iem_type;
        this.iem_name = iem_name;
        this.iem_image = iem_image;
        this.subBelow = subBelow;
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

    public Bitmap getIem_image() {
        return iem_image;
    }

    public void setIem_image(Bitmap iem_image) {
        this.iem_image = iem_image;
    }

    public String getSubBelow() {
        return subBelow;
    }

    public void setSubBelow(String subBelow) {
        this.subBelow = subBelow;
    }
}
