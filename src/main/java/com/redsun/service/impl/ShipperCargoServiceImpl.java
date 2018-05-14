package com.redsun.service.impl;

import com.redsun.dao.ShipperCargoMapper;
import com.redsun.pojo.ShipperCargo;
import com.redsun.service.ShipperCargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class ShipperCargoServiceImpl implements ShipperCargoService {
    @Autowired
    private ShipperCargoMapper shippercargoMapper = null;

    /**
     * 获取货物
     * @param shippercargo
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public List<ShipperCargo> getShipperCargos(ShipperCargo shippercargo) {
        List<ShipperCargo> resultShipperCargos = null;
        resultShipperCargos = shippercargoMapper.getShipperCargos(shippercargo);
        return resultShipperCargos;
    }

    /**
     * 加入货物
     * @param shippercargo
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public void addShipperCargo(ShipperCargo shippercargo) {
        shippercargoMapper.insertShipperCargo(shippercargo);
    }
}
