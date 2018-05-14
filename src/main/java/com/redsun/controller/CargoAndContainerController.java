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

    @RequestMapping("/get")
    public ModelAndView getCargoAndContainer(@RequestParam(name = "cargoid", required = false) Integer cargoid,
                                             @RequestParam(name = "containerid", required = false) Integer containerid,
                                             @RequestParam(name = "units", required = false) Integer units,
                                             ModelMap modelMap){
        CargoAndContainer cargoandcontainer = new CargoAndContainer();
        cargoandcontainer.setCargoid(cargoid);
        cargoandcontainer.setContainerid(containerid);
        cargoandcontainer.setUnits(units);

        ModelAndView mv = new ModelAndView();
        List<CargoAndContainer> cargoandcontainers = cargoandcontainerService.getCargoAndContainer(cargoandcontainer);
        modelMap.addAttribute("cargoandcontainer", cargoandcontainers);

        mv.setView(new MappingJackson2JsonView());
        return mv;
    }
    @RequestMapping("/add")
    @ResponseBody
    public boolean addContainerWithCargo(Container container, Cargo cargo,CargoAndContainer cargoandcontainer){
        /*
        container.setId(10000010);
        container.setRow(Byte.valueOf("1"));
        container.setColumn(Byte.valueOf("1"));
        container.setLayer(Byte.valueOf("3"));
        container.setType(Byte.valueOf("0"));
        container.setSize(Byte.valueOf("0"));
        cargo.setId(10000001);
        cargo.setName("棉花");
        cargo.setMaximumInAContainer(100);
        cargo.setGross(134);
        cargo.setType("箱");
        */
        return cargoandcontainerService.addContainerWithCargo(container,cargo,cargoandcontainer);
    }
    @RequestMapping("/put")
    @ResponseBody
    public void putCargoIntoContainer(Container container, Cargo cargo,CargoAndContainer cargoandcontainer){
        cargoandcontainerService.putCargoIntoContainer(container,cargo,cargoandcontainer);
    }

    @RequestMapping("/test")
    @ResponseBody
    public boolean test(){
        Container container = new Container();
        Cargo cargo = new Cargo();
        CargoAndContainer cargoandcontainer = new CargoAndContainer();
        container.setId(10000005);
        container.setRow(Byte.valueOf("1"));
        container.setColumn(Byte.valueOf("1"));
        container.setLayer(Byte.valueOf("3"));
        container.setType(Byte.valueOf("0"));
        container.setSize(Byte.valueOf("0"));
        cargo.setId(10004273);
        cargo.setName("棉花");
        cargo.setMaximumInAContainer(100);
        cargo.setGross(134);
        cargo.setType("箱");
        cargoandcontainerService.putCargoIntoContainer(container,cargo,cargoandcontainer);
        return true;
    }
}
