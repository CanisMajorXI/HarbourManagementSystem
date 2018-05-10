package com.redsun.service;

import com.redsun.pojo.User;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface UserService {
/*hello*/
    User userVerification(User user);

    List<User> getTotalUsers();
}
