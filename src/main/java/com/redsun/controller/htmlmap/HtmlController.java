package com.redsun.controller.htmlmap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HtmlController {
    //外部路径到内部路径的映射
    @RequestMapping("/*.html")
    public String GetInternalPath(HttpServletRequest request){
        //System.out.println(request.getRequestURI());
        return request.getRequestURI();
    }

}
