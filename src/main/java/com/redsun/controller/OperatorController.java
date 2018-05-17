package com.redsun.controller;

import com.redsun.pojo.Container;
import com.redsun.pojo.ShipperContainer;
import com.redsun.service.ContainerService;
import com.redsun.service.ShipperCargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/api/op")
@ResponseBody
public class OperatorController {

    @Autowired
    private ContainerService containerService = null;

    @Autowired
    private ShipperCargoService shipperCargoService = null;

    /**
     * @param shipperContainerId
     * @param row
     * @param column
     * @param layer
     * @return
     */
    @RequestMapping("/importemptycontainer")
    public boolean importAnEmptyContainerFromTask(@RequestParam("id") Integer shipperContainerId,
                                                  @RequestParam("row") Byte row,
                                                  @RequestParam("column") Byte column,
                                                  @RequestParam("layer") Byte layer) {
        if (shipperContainerId == null || row == null || column == null || layer == null) return false;
        try {
            containerService.importAnEmptyContainerFromTask(shipperContainerId, row, column, layer);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/importcontainerwithcargo")
    public boolean importAContainerWithCargosFromTask(@RequestParam("id") Integer shipperContainerId,
                                                      @RequestParam("row") Byte row,
                                                      @RequestParam("column") Byte column,
                                                      @RequestParam("layer") Byte layer) {
        if (shipperContainerId == null || row == null || column == null || layer == null) return false;
        try {
            containerService.importAContainerWithCargosFromTask(shipperContainerId, row, column, layer);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/importcargointoempty")
    @ResponseBody
    public boolean importCargoIntoEmptyContainer(@RequestParam("cargoid") Integer shipperCargoId,
                                                 @RequestParam("containerid") Integer containerId,
                                                 @RequestParam("row") Byte row,
                                                 @RequestParam("column") Byte column,
                                                 @RequestParam("layer") Byte layer) {
        if (shipperCargoId == null || containerId == null || row == null || column == null || layer == null)
            return false;
        try {
            containerService.importCargoIntoEmptyContainer(shipperCargoId,containerId,row,column,layer);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
//@ResponseBody("/getarea")
//    public ModelAndView getArea(@RequestParam("id") Integer contianerId){
//        ModelAndView modelAndView = new ModelAndView();
//        try {
//            Container tempContainer = new Container();
//            tempContainer.setId(contianerId);
//           Container container = containerService.getContainers(tempContainer).get(0);
//            container.setId(contianerId);
//            containerService.showFeasibleArea(container);
//            return modelAndView;
//        }catch (Exception e){
//            e.printStackTrace();
//           return modelAndView;
//        }
}

//}
