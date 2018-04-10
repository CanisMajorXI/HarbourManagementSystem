package com.redsun.service;

import com.redsun.pojo.User;
import org.springframework.transaction.annotation.Transactional;


public interface UserService {
    @Transactional
    User userVerification(String email, String password);
    @Transactional
    User userVerification(int id,String password);
}
