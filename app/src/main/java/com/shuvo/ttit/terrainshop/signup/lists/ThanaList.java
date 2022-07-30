package com.shuvo.ttit.terrainshop.signup.lists;

public class ThanaList {

    private String ddId;
    private String thanaName;
    private String dist_id;

    public ThanaList(String ddId, String thanaName, String dist_id) {
        this.ddId = ddId;
        this.thanaName = thanaName;
        this.dist_id = dist_id;
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

    public String getDist_id() {
        return dist_id;
    }

    public void setDist_id(String dist_id) {
        this.dist_id = dist_id;
    }
}
