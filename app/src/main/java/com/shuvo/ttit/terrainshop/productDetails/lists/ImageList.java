package com.shuvo.ttit.terrainshop.productDetails.lists;

import android.graphics.Bitmap;

public class ImageList {

    private Bitmap imageUrl;
    private String description;

    public ImageList(Bitmap imageUrl, String description) {
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Bitmap getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Bitmap imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
