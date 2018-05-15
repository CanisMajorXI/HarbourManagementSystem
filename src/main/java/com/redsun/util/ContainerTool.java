package com.redsun.util;

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
}
