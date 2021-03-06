package com.spring.springboot.apicontrollers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spring.springboot.models.BloodAvailable;
import com.spring.springboot.models.BloodGroupAvailable;
import com.spring.springboot.models.Donation;
import com.spring.springboot.services.BloodGroupAvaliableService;
import com.spring.springboot.services.DonationService;

@RestController
@RequestMapping(value = "/api/donation")
public class DonationApiController {

    @Autowired
    private DonationService donationService;

    @Autowired
    private BloodGroupAvaliableService bloodGroupAvaliableService;

    @RequestMapping(value = "/status", method = RequestMethod.PUT)
    public Map<String, Object> setStatus(@RequestBody Map<String, String> body) {

        HashMap<String, Object> map = new HashMap<>();

        try {
            String dId = body.get("id");
            String dStatus = body.get("status");

            Donation donation = this.donationService.findById(Long.parseLong(dId));
            if (donation != null) {

                donation.setStatus(dStatus);

                if (dStatus.equals("completed")) {
                    BloodAvailable ba = donation.getDonationCenter().getCity();
                    BloodGroupAvailable bga = this.bloodGroupAvaliableService.findByCityAndBloodGroup(ba,
                            donation.getBlood_group());

                    if (bga != null) {
                        bga.setQuantity(bga.getQuantity() + 1);
                        this.bloodGroupAvaliableService.save(bga);
                    }
                }

                Donation donationBloodStatus = this.donationService.save(donation);

                if (donationBloodStatus == null) {
                    map.put("success", false);
                    return map;
                }

                map.put("success", true);
            } else {
                map.put("success", false);
                map.put("message","Id does not exist");
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
            return map;
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public Map<String, Object> addNewCity(@RequestBody Map<String, String> payload) {
        HashMap<String, Object> map = new HashMap<>();

        try {
            String id = (String) payload.get("id");
            Donation donation = this.donationService.findById(Long.parseLong(id));
            this.donationService.delete(donation);
            map.put("success", true);

        } catch (Exception e) {
            map.put("success", false);
        }

        return map;
    }
}
