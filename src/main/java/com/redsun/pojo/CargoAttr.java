package com.redsun.pojo;

public class CargoAttr {
    private Integer cargoId;
    private Integer typeId;
    private String name;
    private Integer maximumInAContainer;
    private String type;

    public Integer getCargoId() {
        return cargoId;
    }

    public void setCargoId(Integer cargoId) {
        this.cargoId = cargoId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaximumInAContainer() {
        return maximumInAContainer;
    }

    public void setMaximumInAContainer(Integer maximumInAContainer) {
        this.maximumInAContainer = maximumInAContainer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
