package com.redsun.dao;

import com.redsun.pojo.ShipperCargo;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ShipperCargoMapper {
    List<ShipperCargo> getShipperCargos(ShipperCargo shippercargo);
    int insertShipperCargo(ShipperCargo shippercargo);
}
