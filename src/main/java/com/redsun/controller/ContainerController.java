package com.redsun.controller;

import com.redsun.pojo.Cargo;
import com.redsun.pojo.Container;
import com.redsun.service.CargoService;
import com.redsun.service.ContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
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
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/container")
public class ContainerController {
    @Autowired
    private ContainerService containerService = null;

    @Autowired
    private CargoService cargoService = null;

    /**
     * 获取箱子信息
     *
     * @param id
     * @param row
     * @param column
     * @param layer
     * @param type
     * @param size
     * @param modelMap
     * @return
     */
    @RequestMapping("/get")
    public ModelAndView getContainers(@RequestParam(name = "id", required = false) Integer id,
                                      @RequestParam(name = "row", required = false) Byte row,
                                      @RequestParam(name = "column", required = false) Byte column,
                                      @RequestParam(name = "layer", required = false) Byte layer,
                                      @RequestParam(name = "type", required = false) Byte type,
                                      @RequestParam(name = "size", required = false) Byte size,
                                      ModelMap modelMap) {
        try {
            Container container = new Container();
            container.setId(id);
            container.setRow(row);
            container.setColumn(column);
            container.setLayer(layer);
            container.setType(type);
            container.setSize(size);
            System.out.println(container.getId());
            System.out.println(container.getRow());
            System.out.println(container.getColumn());
            System.out.println(container.getLayer());
            System.out.println(container.getType());
            System.out.println(container.getSize());

            ModelAndView mv = new ModelAndView();
            List<Container> containers = containerService.getContainers(container);
            modelMap.addAttribute("container", containers);
            mv.setView(new MappingJackson2JsonView());
            return mv;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 加入一个空箱子
     *
     * @param container
     */
    @RequestMapping("/add")
    @ResponseBody
    public boolean addAnEmptyContainer(Container container) {
        try {
            if (!Container.checkTentativeValidity(container)) return false;
            containerService.addAnEmptyContainer(container);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //初步判断集装箱属性合法性，即数据是否在设计范围
    @RequestMapping("/addwithcargo")
    @ResponseBody
    public boolean addAContainerWithCargo() {
        Container container = new Container();
        container.setId(88888888);
        container.setRow((byte) 8);
        container.setColumn((byte) 2);
        container.setLayer((byte) 2);
        container.setType((byte) 0);
        container.setSize((byte) 0);
        List<Cargo> cargos = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Cargo cargo = new Cargo();
            cargo.setTypeId(10000000 + i);
            cargo.setGross(10);
            cargo.setCargoId(10000000 + i);
            cargos.add(cargo);
        }
        try {
            containerService.addAContainerWithCargo(container, cargos);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/shift")
    @ResponseBody
    public boolean shift(@RequestParam("id") Integer id, @RequestParam("row") Byte row, @RequestParam("column") Byte column, @RequestParam("layer") Byte layer) {

        try {
            if (id == null || row == null || column == null || layer == null) return false;
            containerService.changeContainerPosition(id,row,column,layer);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/test")
    @ResponseBody
    public boolean test() {
        try {
            Container container = new Container();
            container.setId(10000000);
            return cargoService.isEmpty(container);
//            Cargo cargo = new Cargo();
//            cargo.setTypeId(10000000);
//            cargo.setGross(370);
//            cargo.setCargoId(10000003);
//            return cargoService.isFullToThisCargo(new Container.Builder().setId(88888888).setSize(Container.SIZE_LARGE).build(),cargo);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * NB
     * @param id
     * @param row
     * @param column
     * @param layer
     * @param type
     * @param size
     * @param modelMap
     * @return
     */
    @RequestMapping("/show")
    @ResponseBody
    public ModelAndView show(@RequestParam(name = "id", required = false) Integer id,
                             @RequestParam(name = "row", required = false) Byte row,
                             @RequestParam(name = "column", required = false) Byte column,
                             @RequestParam(name = "layer", required = false) Byte layer,
                             @RequestParam(name = "type", required = false) Byte type,
                             @RequestParam(name = "size", required = false) Byte size,
                             ModelMap modelMap){
        try{
            Container container = new Container();
            container.setId(10000000);
            container.setRow(row);
            container.setColumn(column);
            container.setLayer(layer);
            container.setType(Container.TYPE_HAZARD);
            container.setSize(Container.SIZE_SMALL);
            ModelAndView mv = new ModelAndView();
            List<Container> containers = containerService.showFeasibleArea(container);
            modelMap.addAttribute("container", containers);
            mv.setView(new MappingJackson2JsonView());
            return mv;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

