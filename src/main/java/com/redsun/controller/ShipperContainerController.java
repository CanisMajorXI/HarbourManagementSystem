package com.redsun.controller;

import com.google.gson.*;
import com.redsun.pojo.Container;
import com.redsun.pojo.ShipperCargo;
import com.redsun.pojo.ShipperContainer;
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

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/shippercontainer")
public class ShipperContainerController {
    @Autowired
    private ShipperContainerService shipperContainerService = null;

    @Autowired
    private ShipperCargoService shipperCargoService = null;

    /**
     * 操作员
     *
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
        try {
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
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据货主ID
     */
    @RequestMapping("/getbyid")
    public ModelAndView getById(@RequestParam(name = "userId", required = false) Integer userId,
                                ModelMap modelMap) {
        try {
            ModelAndView mv = new ModelAndView();
            List<ShipperContainer> sContaienrs = shipperContainerService.getById(userId);
            modelMap.addAttribute("shipperContaienr", sContaienrs);
            mv.setView(new MappingJackson2JsonView());
            return mv;
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * @param userId
     * @param containerId
     * @param type
     * @param size
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public boolean insertEmptyContainer(@RequestParam("userid") Integer userId,
                                        @RequestParam("containerid") Integer containerId,
                                        @RequestParam("type") Byte type,
                                        @RequestParam("size") Byte size) {
        if (userId == null || containerId == null || type == null || size == null) return false;
        ShipperContainer shipperContainer = new ShipperContainer();
        shipperContainer.setUserId(userId);
        shipperContainer.setContainerId(containerId);
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
    public boolean insertContainerwithCargo(@RequestBody String json) {
        try {
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonObject rootObject = parser.parse(json).getAsJsonObject();
            JsonObject containerObject = rootObject.get("container").getAsJsonObject();
            JsonArray cargos = rootObject.get("cargos").getAsJsonArray();
            ShipperContainer shipperContainer = gson.fromJson(containerObject, ShipperContainer.class);
            List<ShipperCargo> shipperCargos = new ArrayList<>();
            for (JsonElement cargo : cargos) {
                shipperCargos.add(gson.fromJson(cargo, ShipperCargo.class));
            }
            ShipperContainer container = new ShipperContainer();
            container.setContainerId(12123131);
            container.setSize(Container.SIZE_SMALL);
            return shipperCargoService.isFullToTheseCargosInTask(container, shipperCargos);
            // return shipperContainerService.insertContainerWithCargo(shipperContainer, shipperCargos);

//            JsonObject rootObject = parser.parse(json).getAsJsonObject().get("rank").getAsJsonObject();

            // shipperContainerService.insertContaienrwithCargo(shippercargo, shippercontainer);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/test")
    @ResponseBody
    public boolean test() {
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
        try {
            // shipperContainerService.insertContaienrwithCargo(shipperCargo, shipperContainer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @RequestMapping("/test2")
    @ResponseBody
    public boolean test2() {
        try {
//            ShipperCargo shipperCargo =  new ShipperCargo();
//            shipperCargo.setUserId(10000000);

            //shipperCargoService.isFullToTheseCargosInTask(s);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
