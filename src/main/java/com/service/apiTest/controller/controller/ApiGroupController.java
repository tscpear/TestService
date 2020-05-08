package com.service.apiTest.controller.controller;

import com.service.apiTest.controller.domin.ApiBaseRe;
import com.service.apiTest.dom.domin.ApiGroupParamList;
import com.service.apiTest.dom.domin.ApiListParam;
import com.service.apiTest.dom.mapper.ApiGroupMapper;
import com.service.apiTest.service.service.ApiGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/apigroup")
public class ApiGroupController {
    @Autowired
    private ApiGroupService apiGroupService;

    @GetMapping("/list")
    @ResponseBody
    public ApiBaseRe getList(@RequestParam int page,
                             @RequestParam int limit) {
        ApiBaseRe baseRe = new ApiBaseRe();
        ApiGroupParamList param = new ApiGroupParamList();
        param.setPageBegin(page * limit - limit);
        param.setPageEnd(page * limit);
        baseRe.setCode(1);
        baseRe.setData(apiGroupService.getList(param));
        baseRe.setMsg("查询成功");
        return baseRe;
    }


}
