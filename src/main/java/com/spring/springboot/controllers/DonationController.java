package com.spring.springboot.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.springboot.models.Donation;
import com.spring.springboot.models.DonationCenter;
import com.spring.springboot.models.User;
import com.spring.springboot.services.DonationCenterService;
import com.spring.springboot.services.DonationService;
import com.spring.springboot.services.UserService;

@Controller
public class DonationController {
    @Autowired
    private DonationCenterService donationCenterService;

    @Autowired
    private UserService userService;

    @Autowired
    private DonationService donationService;

    @RequestMapping(value = "/donation", method = RequestMethod.GET)
    public String donationPage(Model model, @CookieValue(name = "userid", defaultValue = "") String userId) {

        if (userId.equals("")) {
            // user not logged-in
            return "redirect:/login";
        }

        Donation obj = new Donation();

        List<DonationCenter> list = this.donationCenterService.findAllCentres();
        HashMap<Long, String> centerMap = new HashMap<>();

        for (DonationCenter c : list) {
            String cname = c.getName() + ", " + c.getCity().getCity();
            centerMap.put(c.getId(), cname);
        }

        model.addAttribute("donation", obj);
        model.addAttribute("donation_centers", centerMap);

        return "donation/form";
    }

    @RequestMapping(value = "/donation/add", method = RequestMethod.POST)
    public String addDonation(Model model, Donation donation,
            @CookieValue(name = "userid", defaultValue = "") String userId) {
        User user = this.userService.findById(Long.parseLong(userId));
        donation.setUserId(user);
        donation.setBlood_group(user.getBlood_group());
        donation.setStatus("pending");
        Donation res = this.donationService.save(donation);

        if (res == null) {
            model.addAttribute("errorMessage", "Sorry, Something went wrong!");
            return "redirect:/home";
        }

        return "redirect:/donation/success/" + Long.toString(res.getId());
    }

    @RequestMapping(value = "/donation/success/{id}")
    public String donationSuccessHandler(@PathVariable String id, Model model) {
        try {
            Long donationId = Long.parseLong(id);
            Donation donation = this.donationService.findById(donationId);
            if (donation == null)
                return "redirect:/donation";
            else {
                Date date = donation.getDate();
                String newDate = date.toString().substring(0, 11);
                model.addAttribute("date", newDate);
                model.addAttribute("data", donation);

                return "donation/success";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/home";
        }
    }
}
