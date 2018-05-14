package com.redsun.service;

import com.redsun.pojo.ShipperCargo;

import java.util.List;

public interface ShipperCargoService {
    List<ShipperCargo> getShipperCargos(ShipperCargo shippercargo);
    void addShipperCargo(ShipperCargo shippercargo);
}
