package com.redsun.dao;

import com.redsun.pojo.Container;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContainerMapper {
    List<Container> getTotalContainers();
    List<Container> getContainers(Container container);
    int insertContainer(Container container);
}
