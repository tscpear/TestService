package com.service.apiTest.controller.controller;


import com.service.apiTest.controller.domin.ApiBaseRe;
import com.service.apiTest.dom.domin.ApiListParam;
import com.service.apiTest.service.domian.ApiDataAU;
import com.service.apiTest.service.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api")
public class ApiController {


    @Autowired
    private ApiService apiService;

    @GetMapping("/list")
    @ResponseBody
    public ApiBaseRe apiList(@RequestParam int page,
                             @RequestParam int limit,
                             @RequestParam(required = false) String apiPath,
                             @RequestParam(required = false) String apiMark,
                             @RequestParam(required = false) Integer device) {
        ApiBaseRe baseRe = new ApiBaseRe();
        try {
            ApiListParam param = new ApiListParam();
            param.setPageBegin(page * limit - limit);
            param.setPageEnd(page * limit);
            param.setApiMark(apiMark);
            param.setApiPath(apiPath);
            param.setDevice(device);
            baseRe.setCode(1);
            baseRe.setData(apiService.getApiList(param));

        } catch (Exception e) {
            baseRe.setCode(0);
            baseRe.setMsg(e.toString());
        }
        return baseRe;
    }


    @GetMapping("/api")
    @ResponseBody
    public ApiBaseRe getApiData(@RequestParam int id) {
        ApiBaseRe baseRe = new ApiBaseRe();
        try {
            baseRe.setData(apiService.getApi(id));
            baseRe.setCode(1);
        } catch (Exception e) {
            baseRe.setCode(0);
            baseRe.setMsg(e.toString());
        }
        return baseRe;
    }


    @PostMapping("/update")
    @ResponseBody
    public ApiBaseRe updateApiData(@RequestBody ApiDataAU apiData) {
        ApiBaseRe baseRe = new ApiBaseRe();
        try {
            apiService.updateApi(apiData);
            baseRe.setCode(1);
            baseRe.setMsg("编辑成功");
        } catch (Throwable throwable) {
            baseRe.setCode(0);
            baseRe.setMsg(throwable.toString());
        }
        return baseRe;
    }

    @PostMapping("/add")
    @ResponseBody
    public ApiBaseRe addApiData(@RequestBody ApiDataAU apiData) {
        ApiBaseRe baseRe =  new ApiBaseRe();
        try {
            apiService.addApi(apiData);
            baseRe.setCode(1);
            baseRe.setMsg("新增成功");
        } catch (Throwable throwable) {
            baseRe.setCode(0);
            baseRe.setMsg(throwable.toString());
        }
        return baseRe;
    }

    @PostMapping("/del")
    @ResponseBody
    public ApiBaseRe delApiData(@RequestBody Object id) {


        return null;
    }
}
