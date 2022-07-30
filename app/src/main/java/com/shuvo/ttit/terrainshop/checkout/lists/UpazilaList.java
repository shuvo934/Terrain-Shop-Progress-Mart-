package com.shuvo.ttit.terrainshop.checkout.lists;

public class UpazilaList {

    private String ddId;
    private String thanaName;

    public UpazilaList(String ddId, String thanaName) {
        this.ddId = ddId;
        this.thanaName = thanaName;
    }

    public String getDdId() {
        return ddId;
    }

    public void setDdId(String ddId) {
        this.ddId = ddId;
    }

    public String getThanaName() {
        return thanaName;
    }

    public void setThanaName(String thanaName) {
        this.thanaName = thanaName;
    }
}
