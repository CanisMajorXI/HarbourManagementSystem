package com.redsun.service;

import com.redsun.pojo.ShipperCargo;
import com.redsun.pojo.ShipperContainer;

import java.util.List;

public interface ShipperCargoService {
    List<ShipperCargo> getShipperCargos(ShipperCargo shippercargo);

    boolean addShipperCargo(ShipperCargo shippercargo);

    boolean isFullToTheseCargosInTask(ShipperContainer shipperContainer,List<ShipperCargo> shipperCargos);
}
