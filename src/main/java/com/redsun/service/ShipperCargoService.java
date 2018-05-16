package com.redsun.service;

import com.redsun.pojo.ShipperCargo;
import com.redsun.pojo.ShipperContainer;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ShipperCargoService {
    List<ShipperCargo> getShipperCargos(ShipperCargo shippercargo);

    void addShipperCargo(ShipperCargo shippercargo);

    boolean isFullToTheseCargosInTask(ShipperContainer shipperContainer, List<ShipperCargo> shipperCargos);



}
