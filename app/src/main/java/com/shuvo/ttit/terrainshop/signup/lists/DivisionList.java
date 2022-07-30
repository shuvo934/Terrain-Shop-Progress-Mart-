package com.shuvo.ttit.terrainshop.signup.lists;

public class DivisionList {

    private String divId;
    private String divName;

    public DivisionList(String divId, String divName) {
        this.divId = divId;
        this.divName = divName;
    }

    public String getDivId() {
        return divId;
    }

    public void setDivId(String divId) {
        this.divId = divId;
    }

    public String getDivName() {
        return divName;
    }

    public void setDivName(String divName) {
        this.divName = divName;
    }
}
