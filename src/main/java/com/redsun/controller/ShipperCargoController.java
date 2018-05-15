package com.redsun.controller;


import com.redsun.pojo.Cargo;
import com.redsun.pojo.ShipperCargo;
import com.redsun.pojo.ShipperContainer;
import com.redsun.service.ShipperCargoService;
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
@RequestMapping("/api/shippercargo")
public class ShipperCargoController {
    @Autowired
    private ShipperCargoService shipperCargoService = null;

    @RequestMapping("/get")
    public ModelAndView getShipperCargos(@RequestParam(name = "userId", required = false) Integer userId,
                                         @RequestParam(name = "cargoId", required = false) Integer cargoId,
                                         @RequestParam(name = "cargoTypeId", required = false) Integer cargoTypeId,
                                         @RequestParam(name = "gross", required = false) Integer gross,
                                         @RequestParam(name = "containerId", required = false) Integer containerId,
                                         ModelMap modelMap) {
        try {
            ShipperCargo shipperCargo = new ShipperCargo();
            shipperCargo.setUserId(userId);
            shipperCargo.setCargoId(cargoId);
            shipperCargo.setCargoTypeId(cargoTypeId);
            shipperCargo.setGross(gross);
            shipperCargo.setContainerId(containerId);
            ModelAndView mv = new ModelAndView();
            List<ShipperCargo> sCargos = shipperCargoService.getShipperCargos(shipperCargo);
            modelMap.addAttribute("shippercargo", sCargos);
            mv.setView(new MappingJackson2JsonView());
            return mv;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param cargoTypeId
     * @param gross
     * @param userId
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public boolean addShipperCargo(@RequestParam("typeid") Integer cargoTypeId,
                                   @RequestParam("gross") Integer gross,
                                   @RequestParam("userid") Integer userId) {
        if (cargoTypeId == null || gross == null || userId == null) return false;
        ShipperCargo shipperCargo = new ShipperCargo();
        shipperCargo.setCargoTypeId(cargoTypeId);
        shipperCargo.setGross(gross);
        shipperCargo.setUserId(userId);
        try {
            return shipperCargoService.addShipperCargo(shipperCargo);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/test")
    @ResponseBody
    public boolean test() {
        ShipperCargo shipperCargo = new ShipperCargo();
        shipperCargo.setUserId(10000000);
        shipperCargo.setCargoId(10000001);
        shipperCargo.setCargoTypeId(10000001);
        shipperCargo.setGross(100);
        // shipperCargo.setContainerId(null);
        try {
            shipperCargoService.addShipperCargo(shipperCargo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
