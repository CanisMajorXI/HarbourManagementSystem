package com.redsun.controller;

import com.redsun.pojo.Cargo;
import com.redsun.service.CargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
                                  @RequestParam(name = "maximum", required = false) Integer maximum,
                                  @RequestParam(name = "gross", required = false) Integer gross,
                                  @RequestParam(name = "type", required = false) String type,
                                  ModelMap modelMap) {
        //System.out.println("row" + (row == null));
        Cargo cargo = new Cargo();
        cargo.setId(id);
        cargo.setName(name);
        cargo.setMaximumInAContainer(maximum);
        cargo.setGross(gross);
        cargo.setType(type);


        ModelAndView mv = new ModelAndView();
        List<Cargo> cargos = cargoService.getCargos(cargo);
        modelMap.addAttribute("cargo", cargos);

        mv.setView(new MappingJackson2JsonView());
        return mv;
    }
}
