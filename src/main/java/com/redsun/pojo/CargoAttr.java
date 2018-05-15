package com.redsun.pojo;

public class CargoAttr {
    //typeId为8位 10000000到79999999为普通货物,80000000到899999999为只能放冷冻区，90000000到99999999危险区

    private Integer typeId;
    private String name;
    private Integer maximumInAContainer;
    private String unitType;

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

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }
}
