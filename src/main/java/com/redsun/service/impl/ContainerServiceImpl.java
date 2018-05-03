package com.redsun.service.impl;

import com.redsun.dao.ContainerMapper;
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

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public List<Container> getContainers(Container container) {
        List<Container> resultContainers = null;
        try {
            resultContainers = containerMapper.getContainers(container);
            //System.out.println("resultContainers == null?"+(resultContainers==null));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            return resultContainers;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public boolean addAnEmptyContainer(Container container) {
        boolean result = true;
        try {
            int i = containerMapper.insertContainer(container);
            System.out.println("受影响的行数:"+i);
        } catch (Exception e) {
            result = false;
            throw new RuntimeException(e);
        } finally {
            return result;
        }
    }

    @Override
    public boolean addContainers(List<Container> containers) {
        try {

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            return true;
        }

    }

    //检查插入的位置是否可插入，例如放在高层的时候底层必须有集装箱
    private boolean isPosEnabled(Container con) {
        List<Container> containers = containerMapper.getContainers(new Container.Builder().setRow(con.getRow()).build());

        return true;
    }
}

