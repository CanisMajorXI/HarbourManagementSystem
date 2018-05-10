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

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public List<Cargo> getCargos(Cargo cargo) {
        List<Cargo> resultCargos = null;
        try {
            resultCargos = cargoMapper.getCargos(cargo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            return resultCargos;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public boolean addABatchCargo(Cargo cargo){
        boolean result = true;
        try {
            int i = cargoMapper.insertCargo(cargo);
            System.out.println("受影响的行数:"+i);
        } catch (Exception e) {
            result = false;
            throw new RuntimeException(e);
        } finally {
            return result;
        }
    }
}
