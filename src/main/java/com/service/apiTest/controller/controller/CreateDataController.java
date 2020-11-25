package com.service.apiTest.controller.controller;

import com.service.CreateTestData.service.ProjectOne;
import com.service.apiTest.controller.domin.ApiBaseRe;
//import com.service.apiTest.service.service.CreateTireDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("createData/tire")
public class CreateDataController {



    @Autowired
    private ProjectOne projectOne;


    @GetMapping("/house")
    @ResponseBody
    public ApiBaseRe getHouse(@RequestHeader(name = "projectId") Integer projectId,
                              @RequestParam Integer environment,
                              @RequestParam Integer tireId,
                              @RequestParam Integer num) throws Throwable {
        projectOne.RarehouseAddTire(tireId, num, environment, projectId, false);
        return null;
    }

    @GetMapping("/completeStoreOrder")
    @ResponseBody
    public ApiBaseRe getCompleteStoreOrder(@RequestHeader(name = "projectId") Integer projectId,
                                           @RequestParam Integer environment,
                                           @RequestParam String orderSn,
                                           @RequestParam Integer type
    ) throws Throwable {
        ApiBaseRe baseRe = new ApiBaseRe();
//        try {
            switch (type) {
                case 2:
                    projectOne.CompleteCKOrder(orderSn, projectId, environment, false);
                    break;
                case 3:
                    projectOne.CompleteStoreOrder(orderSn, projectId, environment);
                    break;
                case 4:
                    projectOne.CompleteDriverOrder(orderSn, projectId, environment);
                    break;
                case 5:
                    projectOne.getVoucher(orderSn, projectId, environment, 1);
                    break;
                case 6:
                    String lpsn = projectOne.getVoucher(orderSn, projectId, environment, -1);
                    baseRe.setData(lpsn);
                    break;
                case 7:
                    projectOne.lpsh(orderSn, environment, type, projectId);
                    break;
                case 8:
                    projectOne.lpsh(orderSn, environment, type, projectId);
                    break;
                case 9:
                    projectOne.lpsh(orderSn, environment, type, projectId);
                    break;

            }
            baseRe.setCode(1);
            baseRe.setMsg("运气不错，没有报错");
//        }catch (Exception e){
//            baseRe.setCode(0);
//            baseRe.setMsg(e.toString());
//        }
        return baseRe;
    }


}
