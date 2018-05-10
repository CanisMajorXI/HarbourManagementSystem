package com.redsun.dao;

import com.redsun.pojo.Cargo;
import com.redsun.pojo.CargoAndContainer;
import com.redsun.pojo.Container;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CargoAndContainerMapper {
    List<CargoAndContainer> getCargoAndContainer(CargoAndContainer cargoandcontainer);
    int insertCargoAndContainer(CargoAndContainer cargoandcontainer);
}
