package com.redsun.controller;

import com.redsun.pojo.Cargo;
import com.redsun.service.CargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import java.util.List;

@Controller
@RequestMapping("/api/cargo")
public class CargoController {
    @Autowired
    private CargoService cargoService = null;

    @RequestMapping("/get")
    public ModelAndView getCargos(@RequestParam(name = "id", required = false) Integer id,
                                  @RequestParam(name = "name", required = false) String name,
                                  @RequestParam(name = "maximumInAContainer", required = false) Integer maximumInAContainer,
                                  @RequestParam(name = "gross", required = false) Integer gross,
                                  @RequestParam(name = "type", required = false) String type,
                                  ModelMap modelMap) {
        Cargo cargo = new Cargo();
        cargo.setId(id);
        cargo.setName(name);
        cargo.setMaximumInAContainer(maximumInAContainer);
        cargo.setGross(gross);
        cargo.setType(type);


        ModelAndView mv = new ModelAndView();
        List<Cargo> cargos = cargoService.getCargos(cargo);
        modelMap.addAttribute("cargo", cargos);

        mv.setView(new MappingJackson2JsonView());
        return mv;
    }

    @RequestMapping("/add")
    @ResponseBody
    public boolean addABatchCargo(Cargo cargo){
        System.out.println(cargo.getId());
        System.out.println(cargo.getName());
        System.out.println(cargo.getGross());
        System.out.println(cargo.getMaximumInAContainer());
        System.out.println(cargo.getType());
        /**
         * 没有控制合法函数
         * 直接进行数据添加
         */
            return cargoService.addABatchCargo(cargo);
    }
}
