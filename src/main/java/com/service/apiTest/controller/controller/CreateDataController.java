package com.service.apiTest.controller.controller;

import com.service.CreateTestData.service.ProjectOne;
import com.service.apiTest.controller.domin.ApiBaseRe;
import com.service.apiTest.service.service.CreateTireDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("createData/tire")
public class CreateDataController {


    @Autowired
    private CreateTireDataService createTireDataService;
    @Autowired
    private ProjectOne projectOne;

    @GetMapping("/tire")
    @ResponseBody
    public ApiBaseRe getTireData(@RequestParam(required = false) String orderSn, @RequestParam Integer environment) {
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


    @GetMapping("/house")
    @ResponseBody
    public ApiBaseRe getHouse(@RequestHeader(name = "projectId") Integer projectId,
                              @RequestParam Integer environment,
                              @RequestParam Integer tireId,
                              @RequestParam Integer num) {

        projectOne.RarehouseAddTire(tireId,num,environment,projectId,false);
        return null;
    }

    @GetMapping("/completeStoreOrder")
    @ResponseBody
    public ApiBaseRe getCompleteStoreOrder(@RequestHeader(name = "projectId") Integer projectId,
                                           @RequestParam Integer environment,
                                           @RequestParam String orderSn,
                                           @RequestParam Integer type
    ) throws InterruptedException {
        switch (type){
            case 2:
                projectOne.CompleteCKOrder(orderSn,projectId,environment,false);
                break;
            case 3:
                projectOne.CompleteStoreOrder(orderSn,projectId,environment);
                break;

        }
       return null;
    }


}
