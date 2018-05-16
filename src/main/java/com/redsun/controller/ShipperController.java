package com.redsun.controller;

import com.google.gson.*;
import com.redsun.pojo.NameAndUnit;
import com.redsun.pojo.ShipperCargo;
import com.redsun.pojo.ShipperContainer;
import com.redsun.pojo.User;
import com.redsun.service.CargoService;
import com.redsun.service.ShipperCargoService;
import com.redsun.service.ShipperContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
@RequestMapping("/api/sh")
public class ShipperController {
    /*
   userid: 77777777,
                        typeid: 10000002,
                        containerid: 32324444,
                        gross: 11
                       http://localhost:8080/api/fr/addcargo?userid=19530615&typeid=10000002&gross=30
                         http://localhost:8080/api/fr/addempty?userid=19530615&containerid=98765432&type=0&size=1
                         http://localhost:8080/api/fr/addempty?userid=19530615&containerid=98765432&type=0&size=1
           http://localhost:8080/api/op/importcargointoempty?cargoid=19&containerid=10000000&row=8&column=3&layer=3
* */

    @Autowired
    private ShipperCargoService shipperCargoService = null;

    @Autowired
    private ShipperContainerService shipperContainerService = null;

    @Autowired
    private CargoService cargoService = null;


    @RequestMapping("/addempty")
    @ResponseBody
    public boolean insertEmptyContainer(@RequestParam("type") Byte type,
                                        @RequestParam("size") Byte size, HttpSession session) {
        if (session == null) return false;
        User user = (User) session.getAttribute("user");

        if (!user.getType().equals(User.TYPE_SHIPPER)) return false;
        if (type == null || size == null) return false;
        ShipperContainer shipperContainer = new ShipperContainer();
        //  System.currentTimeMillis();
        shipperContainer.setUserId(user.getId());
        shipperContainer.setContainerId(getIdByTime());
        shipperContainer.setSize(size);
        shipperContainer.setType(type);
        try {
            shipperContainerService.insertEmptyContainer(shipperContainer);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/addwithcargo")
    @ResponseBody
    public boolean insertContainerWithCargo(@RequestBody String json) {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        try {
            JsonObject rootObject = parser.parse(json).getAsJsonObject();
            JsonObject containerObject = rootObject.get("container").getAsJsonObject();
            JsonArray cargos = rootObject.get("cargos").getAsJsonArray();
            ShipperContainer shipperContainer = gson.fromJson(containerObject, ShipperContainer.class);
            if (shipperContainer.getContainerId() == null
                    || shipperContainer.getSize() == null
                    || shipperContainer.getType() == null
                    || shipperContainer.getUserId() == null) return false;
            List<ShipperCargo> shipperCargos = new ArrayList<>();
            for (JsonElement cargo : cargos) {
                ShipperCargo shipperCargo = gson.fromJson(cargo, ShipperCargo.class);
                if (shipperCargo.getContainerId() == null
                        || shipperCargo.getCargoTypeId() == null
                        || shipperCargo.getGross() == null
                        || shipperCargo.getUserId() == null) return false;
                if (!shipperCargo.getContainerId().equals(shipperContainer.getContainerId())) return false;
                shipperCargos.add(shipperCargo);
            }
            return shipperContainerService.insertContainerWithCargo(shipperContainer, shipperCargos);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/addcargo")
    @ResponseBody
    public boolean addBulkCargo(@RequestParam("userid") Integer userId,
                                @RequestParam("typeid") Integer typeId,
                                @RequestParam("gross") Integer gross) {
        if (userId == null || typeId == null || gross == null) return false;
        ShipperCargo shipperCargo = new ShipperCargo();
        shipperCargo.setUserId(userId);
        shipperCargo.setCargoTypeId(typeId);
        shipperCargo.setGross(gross);
        try {
            shipperCargoService.addShipperCargo(shipperCargo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/getnameandunit")
    public ModelAndView getNameAndUnit(ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new MappingJackson2JsonView());
        try {
            List<NameAndUnit> nameAndUnits = cargoService.getCargosNameAndUnit();
            modelMap.addAttribute("nameandunit", nameAndUnits);
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            return modelAndView;
        }
    }

    public int getIdByTime() {
        String str = String.valueOf(System.currentTimeMillis());
        str = str.substring(5, str.length());
        return Integer.parseInt(str);
    }
}
