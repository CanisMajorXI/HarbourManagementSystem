package com.redsun.service.impl;

import com.redsun.dao.UserMapper;
import com.redsun.pojo.User;
import com.redsun.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper = null;

    @Override
    public User userVerification(String email, String password) {
        User user = null;
        try {
            List<User> users = userMapper.LoginByEmailVerification(email, password);
            if (users.size() == 1) {
                user = users.get(0);
            } else if (users.size() >= 1) {
                System.out.println("用户数据库发生重复用户名！请联系管理员进行检查！");
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);

        } finally {
            return user;
        }
    }

    @Override
    public User userVerification(int id, String password) {

       User user = null;
        try {
            List<User> users = userMapper.LoginByIdVerification(id, password);
            if (users.size() == 1) {
                user = users.get(0);
            } else if (users.size() >= 1) {
                System.out.println("用户数据库发生重复用户名！请联系管理员进行检查！");
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);

        } finally {
            return user;
        }
    }
}
