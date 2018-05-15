package com.redsun.controller;

import com.redsun.pojo.Cargo;
import com.redsun.pojo.CargoAttr;
import com.redsun.service.CargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/api/cargo")
public class CargoController {
    @Autowired
    private CargoService cargoService = null;


    @RequestMapping("/gettypeidbyname")
    @ResponseBody
    public Integer getTypeIdByName(@RequestParam("name") String name) {
        try {
            return getTypeIdByName(name);
        } catch (Exception e) {
            return null;
        }
    }

    @RequestMapping("/get")
    public ModelAndView getCargos(@RequestParam(name = "cargoid", required = false) Integer cargoId,
                                  @RequestParam(name = "cargotypeid", required = false) Integer cargoTypeId,
                                  @RequestParam(name = "containerid", required = false) Integer containerId,
                                  @RequestParam(name = "cargoname", required = false) String cargoName,
                                  ModelMap modelMap) {
        try {
            if (cargoName != null) {
                cargoTypeId = cargoService.getTypeIdByName(cargoName);
            }
            Cargo cargo = new Cargo();
            cargo.setTypeId(cargoTypeId);
            cargo.setCargoId(cargoId);
            cargo.setContainerId(containerId);
            ModelAndView mv = new ModelAndView();
            List<Cargo> cargos = cargoService.getCargos(cargo);
            modelMap.addAttribute("cargo", cargos);
            mv.setView(new MappingJackson2JsonView());
            return mv;
        } catch (Exception e) {
            return null;
        }
    }

/*
    private Integer typeId;
    private String name;
    private Integer maximumInAContainer;
    private String unitType;
*/

    //增加一个货物的参数，该操作只有管理员才能使用
    @RequestMapping("/addacargoattr")
    @ResponseBody
    public boolean addACargoAttr(@RequestParam("typeid") Integer typeId,
                                 @RequestParam("name") String name,
                                 @RequestParam("max") Integer maximumInAContainer,
                                 @RequestParam("unittype") String unitType,
                                 HttpSession session) {
        CargoAttr cargoAttr = new CargoAttr();
        cargoAttr.setTypeId(typeId);
        cargoAttr.setName(name);
        cargoAttr.setMaximumInAContainer(maximumInAContainer);
        cargoAttr.setUnitType(unitType);
        try {
            cargoService.addACargoAttr(cargoAttr);
            return true;
        } catch (Exception e) {
            return false;
        }
        //System.out.println(cargoAttr.getName());
    }
}
