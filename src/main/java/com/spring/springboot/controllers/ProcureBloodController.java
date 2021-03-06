package com.spring.springboot.controllers;

import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.springboot.common.Constants;
import com.spring.springboot.helpers.BloodTable;
import com.spring.springboot.models.BloodAvailable;
import com.spring.springboot.models.ProcureBlood;
import com.spring.springboot.models.User;
import com.spring.springboot.services.BloodAvailableService;
import com.spring.springboot.services.DonationCenterService;
import com.spring.springboot.services.ProcureBloodService;
import com.spring.springboot.services.UserService;

@Controller
public class ProcureBloodController {

    @Autowired
    private ProcureBloodService procureBloodService;

    @Autowired
    private BloodAvailableService bloodAvailableService;

    @Autowired
    private UserService userService;

    @Autowired
    private DonationCenterService donationCenterService;

    @RequestMapping(value = "/procure", method = RequestMethod.GET)
    public String procureBloodHandler(Model model) {

        ProcureBlood obj = new ProcureBlood();

        model.addAttribute("bloodGroups", Constants.BLOOD_GROUPS);
        model.addAttribute("procure", obj);
        return "procure/form";
    }

    @RequestMapping(value = "/procure/add", method = RequestMethod.POST)
    public String addBloodHandler(ProcureBlood pb, Model model,
            @CookieValue(name = "userid", defaultValue = "") String userId) {

        User user = null;

        if (!userId.equals("")) {
            user = this.userService.findById(Long.parseLong(userId));
        }

        String city = pb.getCity();
        int quantity = pb.getQuantity();
        BloodTable helper = new BloodTable();

        ProcureBlood obj1 = new ProcureBlood();

        BloodAvailable su = this.bloodAvailableService.findByCity(city);

        model.addAttribute("bloodGroups", Constants.BLOOD_GROUPS);
        model.addAttribute("procure", obj1);

        if (su == null) {
            model.addAttribute("error", "The city is not serviceable");
            return "procure/form";
        }

        TreeMap<String, Integer> list = helper.getBloodQuantityList(su.getBlood_groups());
        int availableQuantity = list.get(pb.getBlood_group());
        if (quantity > availableQuantity) {
            model.addAttribute("error", "The requsted blood quatity is not available");
            return "procure/form";
        }

        pb.setStatus("pending");

        if (user != null) {
            pb.setUser_id(user);
        }

        ProcureBlood obj = this.procureBloodService.save(pb);

        if (obj == null) {
            model.addAttribute("errorMessage", "Sorry, Something went wrong!");
            return "procure/form";
        }

        return "redirect:/procure/success/" + Long.toString(obj.getId());
    }

    @RequestMapping(value = "/procure/success/{id}")
    public String procureSuccessHandler(@PathVariable String id, Model model) {

        Long procureId = Long.parseLong(id);
        ProcureBlood procureBlood = this.procureBloodService.findById(procureId);

        if (procureBlood == null)
            return "redirect:/procure";

        model.addAttribute("data", procureBlood);

        BloodAvailable ba = this.bloodAvailableService.findByCity(procureBlood.getCity());
        model.addAttribute("centers", this.donationCenterService.findAllCentresByCity(ba));

        return "procure/success";
    }
}
