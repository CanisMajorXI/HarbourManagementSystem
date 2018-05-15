package com.redsun.service;

import com.redsun.pojo.Cargo;
import com.redsun.pojo.Container;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ContainerService {

    List<Container> getContainers();

    List<Container> getContainers(Container container);

    boolean addAnEmptyContainer(Container container);

    void addAContainerWithCargo(Container container, List<Cargo> cargos);

    void changeContainerPosition(Container old,Container alter);

}
