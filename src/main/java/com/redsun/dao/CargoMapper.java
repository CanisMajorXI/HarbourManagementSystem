package com.redsun.dao;

import com.redsun.pojo.Cargo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CargoMapper {
    //  List<Cargo> getTotalCargos();
    List<Cargo> getCargos(Cargo cargo);

    int insertCargo(Cargo cargo);
}
