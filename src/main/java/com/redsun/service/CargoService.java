package com.redsun.service;

import com.redsun.pojo.Cargo;

import java.util.List;

public interface CargoService {
   // List<Cargo> getTotalCargos();
    //List<Cargo> getCargos();
    List<Cargo> getCargos(Cargo cargo);
    void addABatchCargo(Cargo cargo);
}
