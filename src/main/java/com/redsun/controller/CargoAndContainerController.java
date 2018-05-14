package com.redsun.controller;


import com.redsun.pojo.Cargo;
import com.redsun.pojo.CargoAndContainer;
import com.redsun.pojo.Container;
import com.redsun.service.CargoAndContainerService;
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
@RequestMapping("/api/cargoandcontainer")
public class CargoAndContainerController {
    @Autowired
    private CargoAndContainerService cargoandcontainerService = null;

    /**
     * 获取箱子
     * @param cargoid
     * @param containerid
     * @param units
     * @param modelMap
     * @return
     */
    @RequestMapping("/get")
    public ModelAndView getCargoAndContainer(@RequestParam(name = "cargoid", required = false) Integer cargoid,
                                             @RequestParam(name = "containerid", required = false) Integer containerid,
                                             @RequestParam(name = "units", required = false) Integer units,
                                             ModelMap modelMap){
        try{
            CargoAndContainer cargoandcontainer = new CargoAndContainer();
            cargoandcontainer.setCargoid(cargoid);
            cargoandcontainer.setContainerid(containerid);
            cargoandcontainer.setUnits(units);

            ModelAndView mv = new ModelAndView();
            List<CargoAndContainer> cargoandcontainers = cargoandcontainerService.getCargoAndContainer(cargoandcontainer);
            modelMap.addAttribute("cargoandcontainer", cargoandcontainers);

            mv.setView(new MappingJackson2JsonView());
            return mv;
        }catch (Exception e){
            return null;
        }

    }

    /**
     * 加入一个有货的箱子
     * @param container
     * @param cargo
     * @param cargoandcontainer
     */
    @RequestMapping("/add")
    @ResponseBody
    public void addContainerWithCargo(Container container, Cargo cargo,CargoAndContainer cargoandcontainer){
         try{
             cargoandcontainerService.addContainerWithCargo(container,cargo,cargoandcontainer);
         }catch (Exception e){
             e.printStackTrace();
         }
    }
}
