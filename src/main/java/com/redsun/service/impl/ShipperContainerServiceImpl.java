package com.redsun.service.impl;

import com.redsun.dao.ShipperCargoMapper;
import com.redsun.dao.ShipperContainerMapper;
import com.redsun.pojo.ShipperCargo;
import com.redsun.pojo.ShipperContainer;
import com.redsun.service.ShipperContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class ShipperContainerServiceImpl implements ShipperContainerService {
    @Autowired
    private ShipperContainerMapper shipperContainerMapper = null;
    @Autowired
    private ShipperCargoMapper shipperCargoMapper = null;

    /**
     * 操作员get
     * @param shipperContainer
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public List<ShipperContainer> getShipperContainers(ShipperContainer shipperContainer) {
        List<ShipperContainer> resultShipperContainer = null;
        resultShipperContainer = shipperContainerMapper.getShipperContainers(shipperContainer);
        return resultShipperContainer;
    }

    /**
     * 根据ID查找
     * @param id
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public List<ShipperContainer> getById(Integer id) {
       List<ShipperContainer> resultById = null;
       ShipperContainer SC = new ShipperContainer();
       SC.setUserId(id);
       resultById = shipperContainerMapper.getShipperContainers(SC);
       return resultById;
    }

    /**
     * 加入一个空箱子
     * @param shippercontainer
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public void insertEmptyContainer(ShipperContainer shippercontainer) {
        shipperContainerMapper.insertShipperContainer(shippercontainer);
    }

    /**
     * 加入一个有货的箱子
     * @param shippercargo
     * @param shippercontainer
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public void insertContaienrwithCargo(ShipperCargo shippercargo, ShipperContainer shippercontainer) {
        shipperContainerMapper.insertShipperContainer(shippercontainer);
        shippercargo.setContainerId(shippercontainer.getContainerId());
        shipperCargoMapper.insertShipperCargo(shippercargo);
    }
}
