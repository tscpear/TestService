package com.service.apiTest.controller.controller;

import com.service.apiTest.controller.domin.ApiBaseRe;
import com.service.apiTest.dom.domin.ApiCaseListParam;
import com.service.apiTest.dom.entity.Api;
import com.service.apiTest.dom.mapper.ApiCaseMapper;
import com.service.apiTest.dom.mapper.ApiMapper;
import com.service.apiTest.controller.domin.ApiCaseData;
import com.service.apiTest.service.domian.ApiForCase;
import com.service.apiTest.service.service.ApiCaseService;
import com.service.apiTest.service.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/apicase")
public class ApiCaseController {

    @Autowired
    private ApiCaseService apiCaseService;

    @GetMapping("/caseAdd")
    @ResponseBody
    public ApiBaseRe getApiForCaseData(@RequestParam int apiId, @RequestParam Integer userId, @RequestHeader(name = "projectId") Integer projectId) {
        ApiBaseRe baseRe = new ApiBaseRe();
//        try {
            ApiForCase apiForCase = apiCaseService.getApiDataForAddCase(apiId, userId, projectId);
            baseRe.setCode(1);
            baseRe.setData(apiForCase);

//        } catch (Exception e) {
//            baseRe.setCode(0);
//            baseRe.setMsg(e.toString());
//        }

        return baseRe;
    }

    @PostMapping("/add")
    @ResponseBody
    public ApiBaseRe addApiCase(@RequestBody ApiCaseData apiCaseData, @RequestHeader(name = "projectId") Integer projectId,@RequestHeader(name = "userId") Integer userId) {
        ApiBaseRe baseRe = new ApiBaseRe();
//        try {
            apiCaseService.addApiCaseData(apiCaseData, projectId,userId);
            baseRe.setCode(1);
            baseRe.setMsg("新增成功");
//        } catch (Exception e) {
//            baseRe.setCode(0);
//            baseRe.setMsg(e.toString());
//        }

        return baseRe;
    }

    @GetMapping("/list")
    @ResponseBody
    public ApiBaseRe getApiCaseList(@RequestParam Integer page,
                                    @RequestParam Integer limit,
                                    @RequestParam(required = false) String apiPath,
                                    @RequestParam(required = false) String apiCaseMark,
                                    @RequestParam(required = false) Integer device,
                                    @RequestParam(required = false) Integer apiId,
                                    @RequestParam(required = false) Integer apiCaseType,
                                    @RequestHeader(name = "projectId") Integer projectId) {

        ApiBaseRe baseRe = new ApiBaseRe();

        try {
            ApiCaseListParam param = new ApiCaseListParam();
            param.setPageBegin(page * limit - limit);
            param.setPageEnd(limit);
            param.setApiPath(apiPath);
            param.setDevice(device);
            param.setApiCaseMark(apiCaseMark);
            param.setApiId(apiId);
            param.setProjectId(projectId);
            param.setApiCaseType(apiCaseType);
            Map<String, Object> map = apiCaseService.getApiCaseList(param);
            baseRe.setData(map.get("list"));
            baseRe.setCode(1);
            baseRe.setCount(map.get("count"));
        } catch (Exception e) {
            baseRe.setCode(0);
            baseRe.setMsg(e.toString());
        }
        return baseRe;

    }

    @GetMapping("/caseUpdate")
    @ResponseBody
    public ApiBaseRe getCaseAdd(@RequestParam Integer id, @RequestParam Integer userId, @RequestHeader(name = "projectId") Integer projectId) {
        ApiBaseRe baseRe = new ApiBaseRe();
//        try {
            baseRe.setData(apiCaseService.getApiCaseData(id, userId,projectId));
            baseRe.setCode(1);
//        } catch (Exception e) {
//            baseRe.setCode(0);
//            baseRe.setMsg(e.toString());
//        }
        return baseRe;
    }

    @PostMapping("/update")
    @ResponseBody
    public ApiBaseRe updateApiCase(@RequestBody ApiCaseData apiCaseData, @RequestHeader(name = "projectId") Integer projectId,@RequestHeader(name = "userId") Integer userId) {
        ApiBaseRe baseRe = new ApiBaseRe();
        try {
            apiCaseService.updateApiCaseData(apiCaseData, projectId, userId);
            baseRe.setCode(1);
            baseRe.setMsg("更新成功");
        } catch (Exception e) {
            baseRe.setCode(0);
            baseRe.setMsg(e.toString());
        }
        return baseRe;
    }


    @PostMapping("/del")
    @ResponseBody
    public ApiBaseRe delApiCase(@RequestParam Integer id, @RequestParam Integer userId) {
        ApiBaseRe baseRe = new ApiBaseRe();
        try {
            apiCaseService.delApiCase(id, userId);
            baseRe.setCode(1);
            baseRe.setMsg("删除成功");
        } catch (Throwable throwable) {
            baseRe.setCode(0);
            String msg = throwable.toString();
            msg = msg.split(":")[1];
            baseRe.setMsg(msg);
        }
        return baseRe;

    }
}
