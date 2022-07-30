package com.shuvo.ttit.terrainshop.checkout.lists;

public class DistrictList {

    private String distId;
    private String distName;

    public DistrictList(String distId, String distName) {
        this.distId = distId;
        this.distName = distName;
    }

    public String getDistId() {
        return distId;
    }

    public void setDistId(String distId) {
        this.distId = distId;
    }

    public String getDistName() {
        return distName;
    }

    public void setDistName(String distName) {
        this.distName = distName;
    }
}
