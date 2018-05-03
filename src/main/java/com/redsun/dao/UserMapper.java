package com.redsun.dao;

import com.redsun.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserMapper {


    List<User> loginByEmailVerification(@Param("email") String email,@Param("password") String password);

    List<User> loginByIdVerification(@Param("id") int id, @Param("password") String password);

    List<User> getUsers(User user);
}
