package com.redsun.controller;

import com.redsun.pojo.User;
import com.redsun.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/api/user")
public class UserController {
    private static final int TYPEOFID = 1;
    private static final int TYPEOFEMAIL = 2;
    private static final int TYPEOFERROR = 0;

    @Autowired
    private UserService userService = null;

    //验证登陆状态,username 可以是id也可以是邮箱
    @RequestMapping("/login")
    @ResponseBody
    public boolean verifyUser(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) {
        //todo

        User user = new User();
        user.setPassword(password);
        if (typeOfUsername(username) == TYPEOFID) {
            user.setId(Integer.parseInt(username));
        } else if (typeOfUsername(username) == TYPEOFEMAIL) {
            user.setEmail(username);
        }
        if (userService.userVerification(user) == null) return false;
        session.setAttribute("user", user);
        return true;
    }

    //注册新账户
    @RequestMapping("/register")
    @ResponseBody
    public boolean registerANewUser() {
        //todo
        return false;
    }

    @RequestMapping("/gettotalusers1")
    public ModelAndView getAllUsers1(ModelMap modelMap) {
        modelMap.addAttribute("users", userService.getTotalUsers());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new MappingJackson2JsonView());
        return modelAndView;
    }
    @RequestMapping("/gettotalusers")
    @ResponseBody
    public List<User> getAllUsers() {
       return userService.getTotalUsers();
    }

    //通过正则表达式判断username类型，是id还是邮箱
    private int typeOfUsername(String username) {
        if (username.matches("\\d{8}")) return TYPEOFID;
        if (username.matches("\\w+(.\\w+)*@\\w+(.\\w)+")) return TYPEOFEMAIL;
        return TYPEOFERROR;
    }
}
