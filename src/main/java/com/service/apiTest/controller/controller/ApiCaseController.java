package com.service.apiTest.controller.controller;

import com.service.apiTest.controller.domin.ApiBaseRe;
import com.service.apiTest.dom.domin.ApiCaseListParam;
import com.service.apiTest.dom.mapper.ApiMapper;
import com.service.apiTest.controller.domin.ApiCaseData;
import com.service.apiTest.service.domian.ApiForCase;
import com.service.apiTest.service.service.ApiCaseService;
import com.service.apiTest.service.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/apicase")
public class ApiCaseController {

    @Autowired
    private ApiCaseService apiCaseService;
    @Autowired
    private ApiMapper apiMapper;
    @Autowired
    private ApiService apiService;

    @GetMapping("/caseAdd")
    @ResponseBody
    public ApiBaseRe getApiForCaseData(@RequestParam int apiId) {
        ApiBaseRe baseRe = new ApiBaseRe();
        try {
            ApiForCase apiForCase = apiCaseService.getApiDataForAddCase(apiId);
            baseRe.setCode(1);
            baseRe.setData(apiForCase);

        } catch (Exception e) {
            baseRe.setCode(0);
            baseRe.setMsg(e.toString());
        }

        return baseRe;
    }

    @PostMapping("/add")
    @ResponseBody
    public ApiBaseRe addApiCase(@RequestBody ApiCaseData apiCaseData) {
        ApiBaseRe baseRe = new ApiBaseRe();
        try {
            apiCaseService.addApiCaseData(apiCaseData);
            baseRe.setCode(1);
            baseRe.setMsg("新增成功");
        } catch (Exception e) {
            baseRe.setCode(0);
            baseRe.setMsg(e.toString());
        }

        return baseRe;
    }

    @GetMapping("/list")
    @ResponseBody
    public ApiBaseRe getApiCaseList(@RequestParam Integer page,
                                    @RequestParam Integer limit,
                                    @RequestParam(required = false) String apiPath,
                                    @RequestParam(required = false) String apiCaseMark,
                                    @RequestParam(required = false) String device) {
        ApiBaseRe baseRe = new ApiBaseRe();

        try{
            ApiCaseListParam param = new ApiCaseListParam();
            param.setPageBegin(page * limit - limit);
            param.setPageEnd(page * limit);
            param.setApiPath(apiPath);
            param.setDevice(device);
            param.setApiCaseMark(apiCaseMark);


            baseRe.setData(apiCaseService.getApiCaseList(param));
            baseRe.setCode(1);

        }catch (Exception e){
            baseRe.setCode(0);
            baseRe.setMsg(e.toString());
        }
        return baseRe;

    }

    @GetMapping("/caseUpdate")
    @ResponseBody
    public ApiBaseRe getCaseAdd(@RequestParam Integer id){
        ApiBaseRe baseRe = new ApiBaseRe();
        try{
            baseRe.setData(apiCaseService.getApiCaseData(id));
            baseRe.setCode(1);
        }catch (Exception e){
            baseRe.setCode(0);
            baseRe.setMsg(e.toString());
        }
        return baseRe;
    }

    @PostMapping("/update")
    @ResponseBody
    public ApiBaseRe updateApiCase(@RequestBody ApiCaseData apiCaseData ){
       ApiBaseRe baseRe = new ApiBaseRe();
       try{
           apiCaseService.updateApiCaseData(apiCaseData);
           baseRe.setCode(1);
           baseRe.setMsg("新增成功");
       }catch (Exception e){
           baseRe.setCode(0);
           baseRe.setMsg(e.toString());
       }
       return baseRe;
    }
}
