package com.redsun.dao;

import com.redsun.pojo.Container;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContainerMapper {
    List<Container> getTotalContainers();

    List<Container> getContainers(Container container);

    int insertContainer(Container container);

    int deleteContainer(Container container);

    //目前的功能只有更换位置
    int updateContainerPosition(Container container);
}
