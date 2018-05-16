package com.redsun.pojo;

import com.google.gson.annotations.SerializedName;

public class ShipperContainer {

    @SerializedName(value = "userid")
    private Integer userId;

    @SerializedName(value = "containerid")
    private Integer containerId;

    @SerializedName(value = "type")
    private Byte type;

    @SerializedName(value = "size")
    private Byte size;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getContainerId() {
        return containerId;
    }

    public void setContainerId(Integer containerId) {
        this.containerId = containerId;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getSize() {
        return size;
    }

    public void setSize(Byte size) {
        this.size = size;
    }
}
