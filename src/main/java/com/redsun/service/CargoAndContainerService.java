package com.redsun.service;

import com.redsun.pojo.Cargo;
import com.redsun.pojo.CargoAndContainer;
import com.redsun.pojo.Container;

import java.util.List;

public interface CargoAndContainerService {
    List<CargoAndContainer> getCargoAndContainer(CargoAndContainer cargoandcontainer);
    boolean addContainerWithCargo(Container container, Cargo cargo,CargoAndContainer cargoandcontainer);
    void putCargoIntoContainer(Container container, Cargo cargo,CargoAndContainer cargoandcontainer);
}
