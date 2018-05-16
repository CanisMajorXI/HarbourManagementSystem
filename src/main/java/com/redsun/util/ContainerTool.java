package com.redsun.util;

import com.redsun.pojo.Container;

import java.util.List;

public class ContainerTool {
    // private ContainerTool(){};
    private Byte row;
    private Byte column;
    private Byte layer;
    private Byte size;

    public Byte getSize() {
        return size;
    }

    public void setSize(Byte size) {
        this.size = size;
    }

    public Byte getRow() {
        return row;
    }

    public void setRow(Byte row) {
        this.row = row;
    }

    public Byte getColumn() {
        return column;
    }

    public void setColumn(Byte column) {
        this.column = column;
    }

    public Byte getLayer() {
        return layer;
    }

    public void setLayer(Byte layer) {
        this.layer = layer;
    }

    public static boolean isPostionInArea(List<Container> containerList, Byte row, Byte column, Byte layer) {
        Container container = new Container.Builder().setRow(row).setColumn(column).setLayer(layer).build();
        for (Container container1 : containerList) {
            if (container1.getRow().equals(container.getRow())
                    && container1.getColumn().equals(container.getColumn())
                    && container1.getLayer().equals(container.getLayer())) {
                return true;
            }
        }
        return false;
    }
}
