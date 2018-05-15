package com.redsun.service;

import com.redsun.pojo.ShipperCargo;
import com.redsun.pojo.ShipperContainer;

import java.util.List;

public interface ShipperContainerService {
    /**
     * 用于管理员使用
     * @param shipperContainer
     * @return
     */
    List<ShipperContainer> getShipperContainers(ShipperContainer shipperContainer);

    /**
     * 反馈货主提交的货物
     * @param id
     * @return
     */
    List<ShipperContainer> getById(Integer id);

    /**
     * 加入一个空箱子
     * @param shippercontainer
     */
    void insertEmptyContainer(ShipperContainer shippercontainer);
    /**
     * 加入一个有货的箱子
     */
    void insertContaienrwithCargo(ShipperCargo shippercargo,ShipperContainer shippercontainer);

}
