package com.redsun.service.impl;

import com.redsun.dao.CargoMapper;
import com.redsun.pojo.Cargo;
import com.redsun.service.CargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CargoServiceImpl implements CargoService {
    @Autowired
    private CargoMapper cargoMapper = null;

    /**
     * 获取货物
     * @param cargo
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public List<Cargo> getCargos(Cargo cargo) {
        List<Cargo> resultCargos = null;
        resultCargos = cargoMapper.getCargos(cargo);
        return resultCargos;
    }

    /**
     *加入一批货物
     * @param cargo
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public void addABatchCargo(Cargo cargo){
        cargoMapper.insertCargo(cargo);
    }
}
