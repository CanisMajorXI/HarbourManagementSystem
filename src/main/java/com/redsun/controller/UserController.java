package com.redsun.controller;

import com.redsun.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/user")
public class UserController {
    //验证登陆状态
    @RequestMapping("/login")
    @ResponseBody
    public boolean verifyUser(User user, HttpSession session) {
        //todo
        return false;
    }
    //注册新账户
    @RequestMapping("/register")
    @ResponseBody
    public boolean registerANewUser() {
        //todo
        return false;
    }
}
