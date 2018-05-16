package com.redsun.service.impl;

import com.redsun.dao.CargoAttrMapper;
import com.redsun.dao.CargoMapper;
import com.redsun.dao.ShipperCargoMapper;
import com.redsun.pojo.*;
import com.redsun.service.CargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CargoServiceImpl implements CargoService {
    @Autowired
    private CargoMapper cargoMapper = null;

    @Autowired
    private CargoAttrMapper cargoAttrMapper = null;

    @Autowired
    private ShipperCargoMapper shipperCargoMapper = null;

    /**
     * 获取货物
     *
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

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public Integer getTypeIdByName(String name) {
        CargoAttr cargoAttr = new CargoAttr();
        cargoAttr.setName(name);
        List<CargoAttr> cargoAttrs = cargoAttrMapper.getCargoAttrs(cargoAttr);
        return cargoAttrs.get(0).getTypeId();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public String getNameByTypeId(Integer typeId) {
        CargoAttr cargoAttr = new CargoAttr();
        cargoAttr.setTypeId(typeId);
        List<CargoAttr> cargoAttrs = cargoAttrMapper.getCargoAttrs(cargoAttr);
        return cargoAttrs.get(0).getName();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public CargoAttr getAllByTypeId(Integer typeId) {
        CargoAttr cargoAttr = new CargoAttr();
        cargoAttr.setTypeId(typeId);
        return cargoAttrMapper.getCargoAttrs(cargoAttr).get(0);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public void addACargoAttr(CargoAttr cargoAttr) {
        cargoAttrMapper.insertCargoAttr(cargoAttr);
    }

    /**
     * 判断箱子里是否有货
     * 空箱返回true
     * @param container
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public boolean isEmpty(Container container) {
        Integer containerId = container.getId();
        Cargo cargo = new Cargo();
        cargo.setContainerId(containerId);
        List<Cargo> cargos = cargoMapper.getCargos(cargo);
        return cargos.size() == 0;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public boolean isEmptyInTask(Container container) {
        Integer containerId = container.getId();
        ShipperCargo shipperCargo = new ShipperCargo();
        shipperCargo.setContainerId(containerId);
        List<ShipperCargo> shipperCargos = shipperCargoMapper.getShipperCargos(shipperCargo);
        //int aa=4;
        return shipperCargos.size() == 0;
    }

    //检查该箱子是否还能装得下该货物
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public boolean isFullToThisCargo(Container container, Cargo uninsertCargo) {

        Integer containerId = container.getId();
        Cargo cargo = new Cargo();
        cargo.setContainerId(containerId);
        List<Cargo> cargos = cargoMapper.getCargos(cargo);
        List<CargoAttr> cargoAttrs = cargoAttrMapper.getCargoAttrs(new CargoAttr());
        //大箱子容量为小箱子的两倍
        double remain = container.getSize() == Container.SIZE_SMALL ? 1 : 2;
        for (Cargo cargo2 : cargos) {
            for (CargoAttr cargoAttr : cargoAttrs) {
                if (cargoAttr.getTypeId().equals(cargo2.getTypeId())) {
                    remain -= (double) cargo2.getGross() * (1 / (double) cargoAttr.getMaximumInAContainer());
                    if (remain <= 0) return false;
                    break;
                }
            }
        }
        System.out.println("remain：" + remain);
        for (CargoAttr cargoAttr : cargoAttrs) {
            if (cargoAttr.getTypeId().equals(uninsertCargo.getTypeId())) {
                if (uninsertCargo.getGross() >= cargoAttr.getMaximumInAContainer()) return false;
                if ((double) uninsertCargo.getGross() * (1 / (double) cargoAttr.getMaximumInAContainer()) <= remain) {
                    return true;
                }
            }
        }
        return false;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public boolean isFullToTheseCargos(Container container, List<Cargo> uninsertCargos) {
        Integer containerId = container.getId();
        Cargo cargo = new Cargo();
        cargo.setContainerId(containerId);
        List<Cargo> cargos = cargoMapper.getCargos(cargo);
        List<CargoAttr> cargoAttrs = cargoAttrMapper.getCargoAttrs(new CargoAttr());
        //大箱子容量为小箱子的两倍
        double remain = container.getSize() == Container.SIZE_SMALL ? 1 : 2;
        for (Cargo insertedCargo : cargos) {
            for (CargoAttr cargoAttr : cargoAttrs) {
                if (cargoAttr.getTypeId().equals(insertedCargo.getTypeId())) {
                    remain -= (double) insertedCargo.getGross() * (1 / (double) cargoAttr.getMaximumInAContainer());
                    if (remain <= 0) return false;
                    break;
                }
            }
        }
        System.out.println("remain：" + remain);
        for (Cargo uninsertCargo : uninsertCargos) {
            for (CargoAttr cargoAttr : cargoAttrs) {
                if (cargoAttr.getTypeId().equals(uninsertCargo.getTypeId())) {
                    if (uninsertCargo.getGross() >= cargoAttr.getMaximumInAContainer()) return false;
                    remain -= (double) uninsertCargo.getGross() * (1 / (double) cargoAttr.getMaximumInAContainer());
                    break;
                }
            }
        }
        System.out.println("finalremain：" + remain);
        return remain < 0;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public void addACargo(Cargo cargo) {
    }

    /**
     * 加入一批货物
     *
     * @param cargo
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public void addABatchCargo(Cargo cargo) {
        cargoMapper.insertCargo(cargo);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public List<NameAndUnit> getCargosNameAndUnit() {
        List<NameAndUnit> nameAndUnits = new ArrayList<>();
        List<CargoAttr> cargoAttrs = cargoAttrMapper.getCargoAttrs(new CargoAttr());
        for (CargoAttr cargoAttr : cargoAttrs) {
            NameAndUnit nameAndUnit = new NameAndUnit();
            nameAndUnit.setId(cargoAttr.getTypeId());
            nameAndUnit.value.setName(cargoAttr.getName());
            nameAndUnit.value.setUnit(cargoAttr.getUnitType());
            nameAndUnits.add(nameAndUnit);
        }
        return nameAndUnits;
    }
}
