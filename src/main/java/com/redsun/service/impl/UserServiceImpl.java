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
        List<User> users = userMapper.getUsers(user);
        if (users.size() == 1)
            resultUser = users.get(0);
        return resultUser;
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
