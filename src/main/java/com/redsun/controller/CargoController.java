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
    public ModelAndView getCargos(@RequestParam(name = "cargoid", required = false) Integer cargoid,
                                  @RequestParam(name = "typeid", required = false) Integer typeid,
                                  @RequestParam(name = "gross", required = false) Integer gross,
                                  ModelMap modelMap) {
        Cargo cargo = new Cargo();
        cargo.setCargoid(cargoid);
        cargo.setTypeid(typeid);
        cargo.setGross(gross);
        ModelAndView mv = new ModelAndView();
        List<Cargo> cargos = cargoService.getCargos(cargo);
        modelMap.addAttribute("cargo", cargos);

        mv.setView(new MappingJackson2JsonView());
        return mv;
    }

    @RequestMapping("/add")
    @ResponseBody
    public boolean addABatchCargo(Cargo cargo){
        /**
         * 没有控制合法函数
         * 直接进行数据添加
         */
        return cargoService.addABatchCargo(cargo);
    }
}
