package com.redsun.pojo;



public class Container {

    //对应箱子尺寸
    public static final byte SIZE_SMALL = 0;
    public static final byte SIZE_LARGE = 1;
    //对应箱子类型
    public static final byte TYPE_ORDINARY = 0;
    public static final byte TYPE_FREEZE = 1;
    public static final byte TYPE_HAZARD = 2;

    //对应箱子所在的区域
    public static final byte AREA_EMPTY = 0;
    public static final byte AREA_ORDINARY = 1;
    public static final byte AREA_FREEZE = 2;
    public static final byte AREA_HAZARD = 3;
    public static final byte AREA_ERROR = 4;
    //对应箱子的范围;
    public static final byte LOWER_LIMIT_EMPTY = 1;
    public static final byte UPPER_LIMIT_EMPTY = 5;
    public static final byte LOWER_LIMIT_ORDINARY = 6;
    public static final byte UPPER_LIMIT_ORDINARY = 15;
    public static final byte LOWER_LIMIT_FREEZE = 16;
    public static final byte UPPER_LIMIT_FREEZE = 21;
    public static final byte LOWER_LIMIT_HAZARD = 22;
    public static final byte UPPER_LIMIT_HAZARD = 27;

    public static final byte TOTAL_ROWS_EMPTY = 5;
    public static final byte TOTAL_ROWS_ORDINARY = 10;
    public static final byte TOTAL_ROWS_FREEZE = 6;
    public static final byte TOTAL_ROWS_HAZARD = 6;

    public static final byte TOTAL_COLUMNS_EMPTY = 10;
    public static final byte TOTAL_COLUMNS_ORDINARY = 20;
    public static final byte TOTAL_COLUMNS_FREEZE = 10;
    public static final byte TOTAL_COLUMNS_HAZARD = 10;

    public static final byte TOTAL_LAYERS_EMPTY = 6;
    public static final byte TOTAL_LAYERS_ORDINARY = 4;
    public static final byte TOTAL_LAYERS_FREEZE = 4;
    public static final byte TOTAL_LAYERS_HAZARD = 2;

    private Integer id;

    private Byte row;

    private Byte column;

    private Byte layer;

    private Byte type;

    private Byte size;

    /*
    * 场地分为A、B、C、D四个区
    从1开始不是从0开始
A区：空箱区，5行，每行10个箱位，最高6层
B区：重箱区，10行，每行20个箱位，最高4层
C区：冷冻箱区，6行，每行10个箱位，最高4层
D区：危险品箱区，6行，每行10个箱位，最高2层
*/

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    private static byte getArea(Container container) {
        int row = container.getRow();
        if (row >= LOWER_LIMIT_EMPTY && row <= UPPER_LIMIT_EMPTY) return AREA_EMPTY;
        if (row >= LOWER_LIMIT_ORDINARY && row <= UPPER_LIMIT_ORDINARY) return AREA_ORDINARY;
        if (row >= LOWER_LIMIT_FREEZE && row <= UPPER_LIMIT_FREEZE) return AREA_FREEZE;
        if (row >= LOWER_LIMIT_HAZARD && row <= UPPER_LIMIT_HAZARD) return AREA_HAZARD;
        return AREA_ERROR;
    }

    //初步判定，即简单判定位置是否合法
    public static boolean checkTentativeValidity(Container container) {
        if (container.getId() == null
                || container.getColumn() == null
                || container.getSize() == null
                || container.getRow() == null
                || container.getLayer() == null
                || container.getType() == null) return false;
        switch (getArea(container)) {
            case AREA_EMPTY:
                return ((container.getSize() == SIZE_LARGE && container.getColumn() < TOTAL_COLUMNS_EMPTY - 1) || container.getSize() == SIZE_SMALL)
                        && container.getColumn() >= 1
                        && container.getColumn() <= TOTAL_COLUMNS_EMPTY
                        && container.getLayer() >= 1
                        && container.getLayer() <= TOTAL_LAYERS_EMPTY;

            case AREA_ORDINARY:
                return ((container.getSize() == SIZE_LARGE && container.getColumn() < TOTAL_COLUMNS_ORDINARY - 1) || container.getSize() == SIZE_SMALL)
                        && container.getColumn() >= 1
                        && container.getColumn() <= TOTAL_COLUMNS_ORDINARY
                        && container.getLayer() >= 1
                        && container.getLayer() <= TOTAL_LAYERS_ORDINARY;

            case AREA_FREEZE:
                return ((container.getSize() == SIZE_LARGE && container.getColumn() < TOTAL_COLUMNS_FREEZE - 1) || container.getSize() == SIZE_SMALL)
                        && container.getColumn() >= 1
                        && container.getColumn() <= TOTAL_COLUMNS_FREEZE
                        && container.getLayer() >= 1
                        && container.getLayer() <= TOTAL_LAYERS_FREEZE;

            case AREA_HAZARD:
                return ((container.getSize() == SIZE_LARGE && container.getColumn() < TOTAL_COLUMNS_HAZARD - 1) || container.getSize() == SIZE_SMALL)
                        && container.getColumn() >= 1
                        && container.getColumn() <= TOTAL_COLUMNS_HAZARD
                        && container.getLayer() >= 1
                        && container.getLayer() <= TOTAL_LAYERS_HAZARD;
            default:
                return false;
        }
    }

    //container的建造者模式
    public static class Builder {
        private Container container = new Container();

        public Builder setId(Integer id) {
            container.id = id;
            return this;
        }

        public Builder setRow(Byte row) {
            container.row = row;
            return this;
        }

        public Builder setColumn(Byte column) {
            container.column = column;
            return this;
        }

        public Builder setLayer(Byte layer) {
            container.layer = layer;
            return this;
        }

        public Builder setType(Byte type) {
            container.type = type;
            return this;
        }

        public Builder setSize(Byte size) {
            container.size = size;
            return this;
        }

        public Container build() {
            return container;
        }
    }
}
