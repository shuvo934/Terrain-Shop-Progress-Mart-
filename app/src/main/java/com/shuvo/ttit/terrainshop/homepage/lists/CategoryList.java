package com.shuvo.ttit.terrainshop.homepage.lists;

public class CategoryList {

    private String iem_id;
    private String iem_type;
    private String categoryName;
    private String belowCat;
    private int color;

    public CategoryList(String iem_id, String iem_type, String categoryName, String belowCat, int color) {
        this.iem_id = iem_id;
        this.iem_type = iem_type;
        this.categoryName = categoryName;
        this.belowCat = belowCat;
        this.color = color;
    }

    public String getIem_id() {
        return iem_id;
    }

    public void setIem_id(String iem_id) {
        this.iem_id = iem_id;
    }

    public String getIem_type() {
        return iem_type;
    }

    public void setIem_type(String iem_type) {
        this.iem_type = iem_type;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBelowCat() {
        return belowCat;
    }

    public void setBelowCat(String belowCat) {
        this.belowCat = belowCat;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
