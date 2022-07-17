package com.spring.springboot.apicontrollers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spring.springboot.models.Donation;
import com.spring.springboot.models.User;
import com.spring.springboot.services.DonationService;
import com.spring.springboot.services.UserService;

@RestController
@RequestMapping(value = "/api/user")
public class UserApiController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/dp", method = RequestMethod.PUT)
    public Map<String, Object> updateProfilePicture(@RequestBody Map<String, String> payload) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            String uId = payload.get("id");
            String udpLink = payload.get("dp");
            User user = this.userService.findById(Long.parseLong(uId));
            user.setDp(udpLink);

            User userDp = this.userService.save(user);
            if (userDp == null) {
                map.put("success", false);
                return map;
            }
            map.put("success", true);
            return map;

        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
            return map;
        }

    }

    @RequestMapping(value = "/active-donation-status", method = RequestMethod.PUT)
    public Map<String, Object> updateActivelyDonationg(@RequestBody Map<String, Object> payload) {
        HashMap<String, Object> map = new HashMap<>();

        try {
            String dId = (String) payload.get("id");
            Boolean dAtvDonStatus = (Boolean) payload.get("status");

            User user = this.userService.findById(Long.parseLong(dId));
            user.setActivelyDonating(dAtvDonStatus);

            User result = this.userService.save(user);
            if (result == null) {
                map.put("success", false);
                return map;
            }
            map.put("success", true);
            return map;

        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
            return map;
        }

    }

}
