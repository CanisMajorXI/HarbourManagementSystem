package com.redsun.service.impl;

import com.redsun.dao.UserMapper;
import com.redsun.pojo.User;
import com.redsun.service.UserService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper = null;
    Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public User userVerification(User user) {
        User resultUser = null;
        try {
            List<User> users = userMapper.getUsers(user);
            if (users.size() == 1) {
                resultUser = users.get(0);
                System.out.println(user.getId() + " " + user.getEmail() + " " + user.getPassword());
            } else if (users.size() >= 1) {
                System.out.println("用户数据库发生重复用户名！请联系管理员进行检查！");
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e);
            throw new RuntimeException(e);
        } finally {
            return resultUser;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public List<User> getTotalUsers() {
        List<User> users = null;
        User user = new User();
        try {
            users = userMapper.getUsers(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            return users;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public boolean isNotDuplicate(Integer username, String email) {
        User user = new User();
        user.setId(username);
        user.setEmail(email);
        List<User> userList = userMapper.getUsers(user);
        return userList.size() == 0;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    @Override
    public void addAnUser(User user) {
        userMapper.insertUser(user);
    }
}
