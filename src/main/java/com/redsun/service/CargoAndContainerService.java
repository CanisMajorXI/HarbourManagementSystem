package com.redsun.service;

import com.redsun.pojo.Cargo;
import com.redsun.pojo.CargoAndContainer;
import com.redsun.pojo.Container;

import java.util.List;

public interface CargoAndContainerService {
    List<CargoAndContainer> getCargoAndContainer(CargoAndContainer cargoandcontainer);
    void addContainerWithCargo(Container container, Cargo cargo,CargoAndContainer cargoandcontainer);
}
