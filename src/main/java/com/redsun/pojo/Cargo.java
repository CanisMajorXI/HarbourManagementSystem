package com.redsun.pojo;

public class Cargo {
    private Integer id;
    private String name;
    private Integer maximumInAContainer;
    private Integer gross;
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getGross() {
        return gross;
    }

    public void setGross(Integer gross) {
        this.gross = gross;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
