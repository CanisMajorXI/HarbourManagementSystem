package com.redsun.service;

import com.redsun.pojo.Cargo;
import com.redsun.pojo.CargoAttr;
import com.redsun.pojo.Container;

import java.util.List;

public interface CargoService {
    List<Cargo> getCargos(Cargo cargo);

    Integer getTypeIdByName(String name);

    String getNameByTypeId(Integer typeId);

    CargoAttr getAllByTypeId(Integer typeId);

    void addACargoAttr(CargoAttr cargoAttr);

    void addACargo(Cargo cargo);

    boolean isEmpty(Container container);

    boolean isFullToThisCargo(Container container, Cargo cargo);

    void addABatchCargo(Cargo cargo);

}
