package com.service.user.controller.controller;

import com.service.user.controller.domian.CUser;
import com.service.user.service.domain.UserBaseRe;
import com.service.user.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class LoginController {
    @Autowired
    private UserService userService;


    @PostMapping("login")
    @ResponseBody
    public UserBaseRe login(@RequestBody CUser cUser){
      return userService.login(cUser);
    }
}
