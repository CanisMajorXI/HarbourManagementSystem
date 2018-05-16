package com.redsun.dao;

import com.redsun.pojo.ShipperContainer;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipperContainerMapper {
    List<ShipperContainer> getShipperContainers(ShipperContainer shippercontainer);

    void insertShipperContainer(ShipperContainer shippercontainer);

    void deleteShipperContainerById(Integer shipperContainerId);
}
