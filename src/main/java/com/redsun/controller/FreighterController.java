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


/**
 * SerializedName(value = "userid")
 * private Integer userId;
 *
 * @SerializedName(value = "cargoid")
 * private Integer cargoId;
 * @SerializedName(value = "typeid")
 * private Integer cargoTypeId;
 * @SerializedName(value = "gross")
 * private Integer gross;
 * @SerializedName(value = "containerid")
 * public Integer containerId;
 */
@Controller
@RequestMapping("/api/fr")

public class FreighterController {


}
