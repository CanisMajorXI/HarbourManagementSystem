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

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public List<CargoAndContainer> getCargoAndContainer(CargoAndContainer cargoandcontainer) {
        List<CargoAndContainer> resultCargoAndContainers = null;
        try {
            resultCargoAndContainers = cargoandcontainerMapper.getCargoAndContainer(cargoandcontainer);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            return resultCargoAndContainers;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public boolean addContainerWithCargo(Container container, Cargo cargo,CargoAndContainer cargoandcontainer) {
        boolean result = true;
        try {
            int i = containerMapper.insertContainer(container);
            System.out.println("加入箱子成功");
            int j = cargoMapper.insertCargo(cargo);
            System.out.println("加入货物成功");
            cargoandcontainer.setCargoid(cargo.getId());
            cargoandcontainer.setContainerid(container.getId());
            cargoandcontainer.setUnits(cargo.getGross());
            int k = cargoandcontainerMapper.insertCargoAndContainer(cargoandcontainer);
            System.out.println("加入箱货成功");
        } catch (Exception e) {
            result = false;
            throw new RuntimeException(e);
        } finally {
            return result;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public void putCargoIntoContainer(Container container, Cargo cargo, CargoAndContainer cargoandcontainer) {
        try{
            cargoandcontainer.setCargoid(cargo.getId());
            cargoandcontainer.setContainerid(container.getId());
            cargoandcontainer.setUnits(cargo.getGross());
            int i = cargoandcontainerMapper.insertCargoAndContainer(cargoandcontainer);
            System.out.println("成功把货加到箱子里！");
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}
