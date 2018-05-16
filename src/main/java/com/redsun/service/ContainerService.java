package com.redsun.service;

import com.redsun.pojo.Cargo;
import com.redsun.pojo.Container;
import com.redsun.pojo.ShipperCargo;
import com.redsun.pojo.ShipperContainer;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ContainerService {

    List<Container> getContainers();

    List<Container> getContainers(Container container);

    boolean addAnEmptyContainer(Container container);

    void addAContainerWithCargo(Container container, List<Cargo> cargos);

    void changeContainerPosition(Integer id, Byte row, Byte column, Byte layer);

    void importAnEmptyContainerFromTask(Integer shipperContainerId, Byte row, Byte column, Byte layer);

    void importAContainerWithCargosFromTask(Integer shipperContainerId, Byte row, Byte column, Byte layer);

    void importCargoIntoContainerFromTask(ShipperCargo shipperCargo, Container selectedContainer, Container containerForPos);

    List<Container> showFeasibleArea(Container container,int flag);

    void importCargoIntoEmptyContainer(Integer shipperCargoId, Integer containerId, Byte row, Byte column, Byte layer);

}
