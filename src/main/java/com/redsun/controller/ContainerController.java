package com.redsun.controller;

import com.redsun.pojo.Container;
import com.redsun.service.ContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/api/container")
public class ContainerController {
    @Autowired
    private ContainerService containerService = null;

    @RequestMapping("/get")
    public ModelAndView getContainers(@RequestParam(name = "id", required = false) Integer id,
                                      @RequestParam(name = "row", required = false) Byte row,
                                      @RequestParam(name = "column", required = false) Byte column,
                                      @RequestParam(name = "layer", required = false) Byte layer,
                                      @RequestParam(name = "type", required = false) Byte type,
                                      @RequestParam(name = "size", required = false) Byte size,
                                      ModelMap modelMap) {
        //System.out.println("row" + (row == null));
        Container container = new Container();
        container.setId(id);
        container.setRow(row);
        container.setColumn(column);
        container.setLayer(layer);
        container.setType(type);
        container.setSize(size);
       /* System.out.println("row:"+container.getRow());
        System.out.println("column:"+container.getColumn());
        System.out.println("layer:"+container.getLayer());*/
//        if(errors.hasErrors()) {
//            List<FieldError>fieldErrors = errors.getFieldErrors();
//            for(FieldError fieldError: fieldErrors) {
//                System.out.println("field"+fieldError.getField()+"\nmessage:"+fieldError.getDefaultMessage());
//            }
//        }
        ModelAndView mv = new ModelAndView();
        List<Container> containers = containerService.getContainers(container);
        modelMap.addAttribute("container", containers);
        mv.setView(new MappingJackson2JsonView());
        return mv;
    }

    @RequestMapping("/add")
    @ResponseBody
    public boolean addAnEmptyContainer(Container container) {
        if (!Container.checkTentativeValidity(container)) return false;
        System.out.println(container.getRow());
        System.out.println(container.getColumn());
        System.out.println(container.getLayer());
        System.out.println(container.getType());
        System.out.println(container.getSize());
//        if (!Container.checkTentativeValidity(container)) return false;
//
        return containerService.addAnEmptyContainer(container);
    }

    //初步判断集装箱属性合法性，即数据是否在设计范围
}
