package com.redsun.service.impl;

import com.redsun.dao.CargoAndContainerMapper;
import com.redsun.dao.CargoMapper;
import com.redsun.dao.ContainerMapper;
import com.redsun.pojo.Cargo;
import com.redsun.pojo.CargoAndContainer;
import com.redsun.pojo.Container;
import com.redsun.service.CargoAndContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class CargoAndContainerServiceImpl implements CargoAndContainerService {

    @Autowired
    private CargoMapper cargoMapper = null;
    @Autowired
    private ContainerMapper containerMapper = null;
    @Autowired
    private CargoAndContainerMapper cargoandcontainerMapper = null;

    /**
     * 获取箱子
     * @param cargoandcontainer
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public List<CargoAndContainer> getCargoAndContainer(CargoAndContainer cargoandcontainer) {
        List<CargoAndContainer> resultCargoAndContainers = null;
        resultCargoAndContainers = cargoandcontainerMapper.getCargoAndContainer(cargoandcontainer);
        return resultCargoAndContainers;
    }

    /**
     * 加入一个有货的箱子
     * @param container
     * @param cargo
     * @param cargoandcontainer
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public void addContainerWithCargo(Container container, Cargo cargo,CargoAndContainer cargoandcontainer) {
        int i = containerMapper.insertContainer(container);
        System.out.println("加入箱子成功");
        int j = cargoMapper.insertCargo(cargo);
        System.out.println("加入货物成功");
        cargoandcontainer.setCargoid(cargo.getCargoid());
        cargoandcontainer.setContainerid(container.getId());
        cargoandcontainer.setUnits(cargo.getGross());
        int k = cargoandcontainerMapper.insertCargoAndContainer(cargoandcontainer);
        System.out.println("加入箱货成功");
    }
    /**
     * 将货加入到箱子中
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public void putCargoIntoContainer(Container container, Cargo cargo, CargoAndContainer cargoandcontainer) {
        cargoandcontainer.setCargoid(cargo.getCargoid());
        cargoandcontainer.setContainerid(container.getId());
        cargoandcontainer.setUnits(cargo.getGross());
        cargoandcontainerMapper.insertCargoAndContainer(cargoandcontainer);
    }
}
