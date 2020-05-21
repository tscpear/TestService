package com.service.apiTest.controller.controller;

import com.service.apiTest.controller.domin.ApiBaseRe;
import com.service.apiTest.service.service.CreateTireDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("createData")
public class CreateDataController {


    @Autowired
    private CreateTireDataService createTireDataService;

    @GetMapping("/tire")
    @ResponseBody
    public ApiBaseRe getTireData(@RequestParam(required = false) String orderSn,@RequestParam String environment) {
        ApiBaseRe baseRe = new ApiBaseRe();
        try {
            createTireDataService.getTireTestData(environment, orderSn);
            baseRe.setCode(1);
            baseRe.setMsg("OK了，如果数量不对那就再跑一次");
            return baseRe;
        } catch (Throwable throwable) {
            baseRe.setCode(0);
            baseRe.setMsg(throwable.toString());
        }
        return baseRe;
    }
}
