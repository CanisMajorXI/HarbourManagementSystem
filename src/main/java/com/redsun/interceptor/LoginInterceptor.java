package com.redsun.interceptor;

import com.redsun.pojo.User;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("preHandle");
        HttpSession session = request.getSession(false);
        return true;
//        if(session == null) {
//            sendRedirect(response);
//            return false;
//        }
//        return true;
    }
    public void sendRedirect(HttpServletResponse response) throws Exception {
        System.out.println("用户未登录！");
        response.sendRedirect("/index.html");
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle");
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        System.out.println("afterHandle");
        super.afterCompletion(request, response, handler, ex);
    }
}