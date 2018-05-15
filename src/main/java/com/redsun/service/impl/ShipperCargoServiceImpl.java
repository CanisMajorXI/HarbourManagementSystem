package com.redsun.service.impl;

import com.redsun.dao.CargoAttrMapper;
import com.redsun.dao.ShipperCargoMapper;
import com.redsun.pojo.*;
import com.redsun.service.CargoService;
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
    private ShipperCargoMapper shipperCargoMapper = null;

    @Autowired
    private CargoAttrMapper cargoAttrMapper = null;


    /**
     * 获取货物
     *
     * @param shippercargo
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public List<ShipperCargo> getShipperCargos(ShipperCargo shippercargo) {
        List<ShipperCargo> resultShipperCargos = null;
        resultShipperCargos = shipperCargoMapper.getShipperCargos(shippercargo);
        return resultShipperCargos;
    }

    /**
     * 加入货物
     *
     * @param shippercargo
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public boolean addShipperCargo(ShipperCargo shippercargo) {
        int typeId = shippercargo.getCargoTypeId();
        CargoAttr cargoAttr = new CargoAttr();
        cargoAttr.setTypeId(typeId);
        if (cargoAttrMapper.getCargoAttrs(cargoAttr).size() == 0) {
            return false;
        }
        shipperCargoMapper.insertShipperCargo(shippercargo);
        return true;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public boolean isFullToTheseCargosInTask(ShipperContainer shipperContainer, List<ShipperCargo> uninsertShipperCargos) {

        Integer containerId = shipperContainer.getContainerId();
        ShipperCargo shipperCargo = new ShipperCargo();
        shipperCargo.setContainerId(containerId);
        List<ShipperCargo> shipperCargos = shipperCargoMapper.getShipperCargos(shipperCargo);
        // List<Cargo> cargos = cargoMapper.getCargos(cargo);
        List<CargoAttr> cargoAttrs = cargoAttrMapper.getCargoAttrs(new CargoAttr());
        //大箱子容量为小箱子的两倍
        double remain = shipperContainer.getSize() == Container.SIZE_SMALL ? 1 : 2;
        for (ShipperCargo insertedShipperCargo : shipperCargos) {
            for (CargoAttr cargoAttr : cargoAttrs) {
                if (cargoAttr.getTypeId().equals(insertedShipperCargo.getCargoTypeId())) {
                    remain -= (double) insertedShipperCargo.getGross() * (1 / (double) cargoAttr.getMaximumInAContainer());
                    if (remain <= 0) return false;
                    break;
                }
            }
        }
        System.out.println("remain：" + remain);
        for (ShipperCargo uninsertShipperCargo : uninsertShipperCargos) {
            for (CargoAttr cargoAttr : cargoAttrs) {
                if (cargoAttr.getTypeId().equals(uninsertShipperCargo.getCargoTypeId())) {
                    if (uninsertShipperCargo.getGross() >= cargoAttr.getMaximumInAContainer()) return false;
                    remain -= (double) uninsertShipperCargo.getGross() * (1 / (double) cargoAttr.getMaximumInAContainer());
                    break;
                }
            }
        }
        System.out.println("finalremain：" + remain);
        return remain > 0;
    }
}
