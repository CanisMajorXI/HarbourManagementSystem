package com.redsun.service.impl;

import com.redsun.dao.CargoMapper;
import com.redsun.dao.ContainerMapper;
import com.redsun.pojo.Cargo;
import com.redsun.pojo.Container;
import com.redsun.service.ContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContainerServiceImpl implements ContainerService {
    @Autowired
    private ContainerMapper containerMapper = null;
    @Autowired
    private CargoMapper cargoMapper = null;

    /**
     * 未实现
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public List<Container> getContainers() {
        List<Container> containers = null;
        try {
            containers = containerMapper.getTotalContainers();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            return containers;
        }
    }

    /**
     * 获取箱子信息
     *
     * @param container
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public List<Container> getContainers(Container container) {
        List<Container> resultContainers = null;
        resultContainers = containerMapper.getContainers(container);
        return resultContainers;
    }

    /**
     * 添加一个空箱子
     *
     * @param container
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public boolean addAnEmptyContainer(Container container) {
        containerMapper.insertContainer(container);
        return true;
    }


    //检查插入的位置是否可插入，例如放在高层的时候底层必须有集装箱
    private boolean isPosEnabled(Container con) {
        List<Container> containers = containerMapper.getContainers(new Container.Builder().setRow(con.getRow()).build());
        return true;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public void addAContainerWithCargo(Container container, List<Cargo> cargos) {
        containerMapper.insertContainer(container);
        for (Cargo cargo : cargos) {
            cargo.setContainerId(container.getId());
            cargoMapper.insertCargo(cargo);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public void changeContainerPosition(Integer id, Byte row, Byte column, Byte layer) {

        //todo位置检查
        containerMapper.updateContainerPosition(id, row, column, layer);
    }
}

