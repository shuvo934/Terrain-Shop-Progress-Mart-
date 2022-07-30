package com.shuvo.ttit.terrainshop.homepage.lists;

public class SliderItem {

    private String imageUrl;
    private int drawable;
    private String description;

    public SliderItem(int imageUrl, String description) {
        this.drawable = imageUrl;
        this.description = description;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
