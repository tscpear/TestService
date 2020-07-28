package com.service.user.controller.controller;

import com.service.apiTest.controller.domin.ApiBaseRe;
import com.service.user.controller.domian.CUser;
import com.service.user.service.domain.UserBaseRe;
import com.service.user.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("project")
    @ResponseBody
    public ApiBaseRe project(){
        ApiBaseRe baseRe = new ApiBaseRe();
        try {
            baseRe.setData(userService.getProject());
            baseRe.setCode(1);
        }catch (Exception e){
            baseRe.setCode(0);
            baseRe.setMsg(e.toString());
        }
        return baseRe;
    }

}
