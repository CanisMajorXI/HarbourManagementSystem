package com.redsun.dao;

import com.redsun.pojo.Cargo;
import com.redsun.pojo.CargoAttr;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CargoAttrMapper {

    List<CargoAttr> getCargoAttrs(CargoAttr cargoAttr);

    void insertCargoAttr(CargoAttr cargoAttr);


}
