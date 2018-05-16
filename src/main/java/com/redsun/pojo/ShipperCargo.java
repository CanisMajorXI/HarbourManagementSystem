package com.redsun.pojo;

import com.google.gson.annotations.SerializedName;

public class ShipperCargo {

    @SerializedName(value = "userid")
    private Integer userId;

    @SerializedName(value = "cargoid")
    private Integer cargoId;

    @SerializedName(value = "typeid")
    private Integer cargoTypeId;

    @SerializedName(value = "gross")
    private Integer gross;

    @SerializedName(value = "containerid")
    public Integer containerId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCargoId() {
        return cargoId;
    }

    public void setCargoId(Integer cargoId) {
        this.cargoId = cargoId;
    }

    public Integer getCargoTypeId() {
        return cargoTypeId;
    }

    public void setCargoTypeId(Integer cargoTypeId) {
        this.cargoTypeId = cargoTypeId;
    }

    public Integer getGross() {
        return gross;
    }

    public void setGross(Integer gross) {
        this.gross = gross;
    }

    public Integer getContainerId() {
        return containerId;
    }

    public void setContainerId(Integer containerId) {
        this.containerId = containerId;
    }
}
