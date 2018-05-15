package com.redsun.controller;


import com.redsun.pojo.Cargo;
import com.redsun.pojo.ShipperCargo;
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
        try{
            ShipperCargo shipperCargo = new ShipperCargo();
            shipperCargo.setUserId(userId);
            shipperCargo.setCargoId(cargoId);
            shipperCargo.setCargoTypeId(cargoTypeId);
            shipperCargo.setGross(gross);
            shipperCargo.setContainerId(containerId);
            ModelAndView mv = new ModelAndView();
            List<ShipperCargo> sCargos = shipperCargoService.getShipperCargos(shipperCargo);
            modelMap.addAttribute("shipperCargo", sCargos);
            mv.setView(new MappingJackson2JsonView());
            return mv;
        }catch(Exception e){
            return null;
        }
    }
    @RequestMapping("/add")
    @ResponseBody
    public void addShipperCargo(ShipperCargo shippercargo){
        try{
            shipperCargoService.addShipperCargo(shippercargo);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
