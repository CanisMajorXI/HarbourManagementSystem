package com.redsun.controller;

import com.redsun.pojo.User;
import com.redsun.service.UserService;
import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Properties;
import java.util.Random;

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
    public boolean verifyUser(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              @RequestParam("type") Byte type,
                              HttpSession session) {
        if (username == null || password == null || type == null) return false;
        User user = new User();
        user.setPassword(password);
        if (typeOfUsername(username) == TYPEOFID) {
            user.setId(Integer.parseInt(username));
        } else if (typeOfUsername(username) == TYPEOFEMAIL) {
            user.setEmail(username);
        }
        user.setType(type);
        try {
            if (userService.userVerification(user) == null) return false;
            session.setAttribute("user", user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/register/getcode")
    public void sendVerificationCode(@RequestParam("email") String email, HttpSession session) {
        if (!email.matches("[0-9a-zA-Z_]+(\\.[0-9a-zA-Z_]+)*@[0-9a-zA-Z_]+(\\.[0-9a-zA-Z_]+)+")) {
            return;
        }
        int code = getRandomCode();
        if (sendCodeToEmail(email, code)) {
            session.setAttribute("verificationCode", code);
        }
    }

    @RequestMapping("/register/isnotduplicate")
    @ResponseBody
    public boolean isNotDuplicate(@RequestParam(name = "username", required = false) String _username, @RequestParam(value = "email", required = false) String email) {
        Integer username = null;
        if (_username == null && email == null) {
            System.out.println("全为空！");
            return false;
        }
        if (_username != null) {
            try {
                username = Integer.parseInt(_username);
            } catch (Exception e) {
                return false;
            }
        }
        System.out.println(username + "  " + email);
        try {
            return userService.isNotDuplicate(username, email);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/register/addanuser")
    @ResponseBody
    public int addAnUser(@RequestParam(name = "username") Integer id,
                         @RequestParam(name = "password") String password,
                         @RequestParam(name = "email") String email,
                         @RequestParam(name = "type") Byte type,
                         @RequestParam(name = "vericode") Integer code, HttpSession session) {
        if (id == null || password == null || email == null || type == null || code == null) return 2;
//
        if (session.getAttribute("verificationCode") == null || !((Integer) session.getAttribute("verificationCode")).equals(code)) {
            System.out.println("用户输入的验证码" + code);
            System.out.println("实际验证码" + session.getAttribute("verificationCode"));
            return 1;
        }
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPassword(password);
        user.setType(type);
        try {
            userService.addAnUser(user);
            return 0;
        } catch (Exception e) {
            return 2;
        }
//        System.out.println(id);
//        System.out.println(password);
//        System.out.println(email);
//        System.out.println(type);
//        System.out.println(code);
    }


    @RequestMapping("/test")
    public void test() {
        User user = new User();
        user.setType(User.TYPE_OPERATOR);
        user.setId(42345213);
        user.setEmail("2323232222@qq.com");
        user.setPassword("19890604");
        userService.addAnUser(user);
//        try {
//
//        }catch (Exception e){
//            System.out.println("检测到异常！");
//        }


    }

    //通过正则表达式判断username类型，是id还是邮箱
    private int typeOfUsername(String username) {
        if (username.matches("\\d{8}")) return TYPEOFID;
        if (username.matches("\\w+(.\\w+)*@\\w+(.\\w)+")) return TYPEOFEMAIL;
        return TYPEOFERROR;
    }

    //随机生成6位验证码
    private int getRandomCode() {
        return new Random().nextInt(899999) + 100000;
    }

    //发送验证码到邮箱
    private boolean sendCodeToEmail(String email, int code) {

        // 1.创建连接对象javax.mail.Session
        // 2.创建邮件对象 javax.mail.Message
        // 3.发送一封激活邮件
        String from = "513768474@qq.com";// 发件人电子邮箱
        String host = "smtp.qq.com"; // 指定发送邮件的主机smtp.qq.com(QQ)|smtp.163.com(网易)

        Properties properties = System.getProperties();// 获取系统属性

        properties.setProperty("mail.smtp.host", host);// 设置邮件服务器
        properties.setProperty("mail.smtp.auth", "true");// 打开认证

        try {
            //QQ邮箱需要下面这段代码，163邮箱不需要
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory", sf);


            // 1.获取默认session对象
            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("513768474@qq.com", "inivhogfaitibhed"); // 发件人邮箱账号、授权码
                }
            });

            // 2.创建邮件对象
            Message message = new MimeMessage(session);
            // 2.1设置发件人
            message.setFrom(new InternetAddress(from));
            // 2.2设置接收人
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            // 2.3设置邮件主题
            message.setSubject("红太阳港口管理系统验证码");
            // 2.4设置邮件内容
            String content = "<html><head></head><body><h1>你的验证码是:" + code + "</h1>请在3分钟内输入该验证码完成注册</body></html>";
            message.setContent(content, "text/html;charset=UTF-8");
            // 3.发送邮件
            Transport.send(message);
            System.out.println("邮件成功发送!");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
