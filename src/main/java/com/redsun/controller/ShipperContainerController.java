package com.redsun.controller;

import com.redsun.pojo.ShipperCargo;
import com.redsun.pojo.ShipperContainer;
import com.redsun.service.ShipperContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.List;

@Controller
@RequestMapping("/api/shippercontainer")
public class ShipperContainerController {
    @Autowired
    private ShipperContainerService shipperContainerService = null;

    /**
     * 操作员
     * @param userId
     * @param containerId
     * @param type
     * @param size
     * @param modelMap
     * @return
     */
    @RequestMapping("/get")
    public ModelAndView getShipperContaienrs(@RequestParam(name = "userId", required = false) Integer userId,
                                         @RequestParam(name = "containerId", required = false) Integer containerId,
                                         @RequestParam(name = "type", required = false) Byte type,
                                         @RequestParam(name = "size", required = false) Byte size,
                                         ModelMap modelMap) {
        try{
            ShipperContainer shipperContaienr = new ShipperContainer();
            shipperContaienr.setUserId(userId);
            shipperContaienr.setContainerId(containerId);
            shipperContaienr.setType(type);
            shipperContaienr.setSize(size);
            ModelAndView mv = new ModelAndView();
            List<ShipperContainer> sContaienrs = shipperContainerService.getShipperContainers(shipperContaienr);
            modelMap.addAttribute("shipperContaienr", sContaienrs);
            mv.setView(new MappingJackson2JsonView());
            return mv;
        }catch(Exception e){
            return null;
        }
    }
    /**
     * 根据货主ID
     */
    @RequestMapping("/getById")
    public ModelAndView getById(@RequestParam(name = "userId", required = false) Integer userId,
                                ModelMap modelMap){
        try{
            ModelAndView mv = new ModelAndView();
            List<ShipperContainer> sContaienrs = shipperContainerService.getById(userId);
            modelMap.addAttribute("shipperContaienr", sContaienrs);
            mv.setView(new MappingJackson2JsonView());
            return mv;
        }catch(Exception e){
            return null;
        }

    }

    /**
     * 加入一个空箱子
     * @param shippercontainer
     */
    @RequestMapping("/add")
    @ResponseBody
    public void insertEmptyContainer(ShipperContainer shippercontainer){
        try{
            shipperContainerService.insertEmptyContainer(shippercontainer);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 加入一个有货的箱子
     * @param shippercargo
     * @param shippercontainer
     */
    @RequestMapping("/add2")
    @ResponseBody
    public void insertContaienrwithCargo(ShipperCargo shippercargo,ShipperContainer shippercontainer){
        try{
            shipperContainerService.insertContaienrwithCargo(shippercargo,shippercontainer);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("/test")
    @ResponseBody
    public boolean test(){
        ShipperContainer shipperContainer = new ShipperContainer();
        shipperContainer.setUserId(10000000);
        shipperContainer.setSize(Byte.valueOf("1"));
        shipperContainer.setType(Byte.valueOf("0"));
        shipperContainer.setContainerId(10000003);

        ShipperCargo shipperCargo = new ShipperCargo();
        shipperCargo.setUserId(10000000);
        shipperCargo.setContainerId(10000003);
        shipperCargo.setGross(100);
        shipperCargo.setCargoTypeId(10000001);
        shipperCargo.setCargoId(10000003);
        try{
            shipperContainerService.insertContaienrwithCargo(shipperCargo,shipperContainer);
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }

}
