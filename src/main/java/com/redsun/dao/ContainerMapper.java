package com.redsun.dao;

import com.redsun.pojo.Container;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContainerMapper {
    List<Container> getTotalContainers();

    List<Container> getContainers(Container container);

    void insertContainer(Container container);

    int deleteContainer(Integer id);

    //目前的功能只有更换位置
    void updateContainerPosition(@Param("id") Integer id, @Param("row") Byte row, @Param("column") Byte column, @Param("layer") Byte layer);
}
