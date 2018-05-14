package com.redsun.dao;

import com.redsun.pojo.ShipperContainer;

import java.util.List;

public interface ShipperContainerMapper {
    List<ShipperContainer> getShipperContainers(ShipperContainer shippercontainer);
    int insertShipperContainer(ShipperContainer shippercontainer);
}
