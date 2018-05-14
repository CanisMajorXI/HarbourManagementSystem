package com.redsun.dao;

import com.redsun.pojo.ShipperCargo;

import java.util.List;

public interface ShipperCargoMapper {
    List<ShipperCargo> getShipperCargos(ShipperCargo shippercargo);
    int insertShipperCargo(ShipperCargo shippercargo);
}
