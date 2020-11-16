package com.service.apiTest.controller.controller;


import com.service.apiTest.controller.domin.ApiBaseRe;
import com.service.apiTest.dom.domin.ApiListParam;
import com.service.apiTest.dom.mapper.ApiMapper;
import com.service.apiTest.service.domian.ApiDataAU;
import com.service.apiTest.service.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("api")
public class ApiController {


    @Autowired
    private ApiService apiService;
    @Autowired
    private ApiMapper apiMapper;

    @GetMapping("/list")
    @ResponseBody
    public ApiBaseRe apiList(@RequestParam int page,
                             @RequestParam int limit,
                             @RequestParam(required = false) String apiPath,
                             @RequestParam(required = false) String apiMark,
                             @RequestParam(required = false) String device,
                             @RequestHeader(name = "projectId") Integer projectId) {
        ApiBaseRe baseRe = new ApiBaseRe();
        try {
            ApiListParam param = new ApiListParam();
            param.setPageBegin(page * limit - limit);
            param.setPageEnd(limit);
            param.setApiMark(apiMark);
            param.setApiPath(apiPath);
            param.setDevice(device);
            param.setProjectId(projectId);
            Map<String, Object> map = apiService.getApiList(param);
            baseRe.setCode(1);
            baseRe.setData(map.get("list"));
            baseRe.setCount(map.get("count"));

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
    public ApiBaseRe updateApiData(@RequestBody ApiDataAU apiData, @RequestHeader(name = "projectId") Integer projectId,@RequestHeader(name = "userId") Integer userId) {
        ApiBaseRe baseRe = new ApiBaseRe();
        Integer count = apiMapper.getCountReData(apiData.getDevice(), apiData.getApiPath(), projectId,apiData.getApiMethod());
        if (count > 1) {
            baseRe.setCode(0);
            baseRe.setMsg("存在接口路径与设备相同的接口");
        } else {

            try {
                apiService.updateApi(apiData,userId);
                baseRe.setCode(1);
                baseRe.setMsg("编辑成功");
            } catch (Throwable throwable) {
                baseRe.setCode(0);
                baseRe.setMsg(throwable.toString());
            }
        }

        return baseRe;
    }

    @PostMapping("/add")
    @ResponseBody
    public ApiBaseRe addApiData(@RequestBody ApiDataAU apiData, @RequestHeader(name = "projectId") Integer projectId) {
        ApiBaseRe baseRe = new ApiBaseRe();
        Integer count = apiMapper.getCountReData(apiData.getDevice(), apiData.getApiPath(), projectId,apiData.getApiMethod());
        if (count < 0) {
            baseRe.setCode(0);
            baseRe.setMsg("存在接口路径与设备相同的接口");
        } else {
            try {
                apiData.setProjectId(projectId);
                apiService.addApi(apiData);
                baseRe.setCode(1);
                baseRe.setMsg("新增成功");
            } catch (Throwable throwable) {
                baseRe.setCode(0);
                baseRe.setMsg(throwable.toString());
            }
        }
        return baseRe;
    }

    @PostMapping("/del")
    @ResponseBody
    public ApiBaseRe delApiData(@RequestBody Object id) {


        return null;
    }


    @GetMapping("/searchRely")
    @ResponseBody
    public ApiBaseRe searchRely(@RequestParam String path,@RequestHeader(name = "projectId")Integer projectId) {
        ApiBaseRe baseRe = new ApiBaseRe();
        baseRe.setCode(1);
        baseRe.setData(apiService.searchTest(path,projectId));
        return baseRe;
    }


    @GetMapping("/searchRelyName")
    @ResponseBody
    public ApiBaseRe searchRelyName(@RequestParam String path,@RequestParam Integer device,@RequestHeader(name = "projectId") Integer projectId) {
        ApiBaseRe baseRe = new ApiBaseRe();
        baseRe.setCode(1);
        baseRe.setData(apiService.searchRelyName(path,device,projectId));
        return baseRe;
    }
}
