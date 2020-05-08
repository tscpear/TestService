package com.service.apiTest.controller.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.service.apiTest.controller.domin.ApiBaseRe;
import com.service.apiTest.service.service.ApiReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/report")
public class ApiReportController {

    @Autowired
    private ApiReportService apiReportService;


    @PostMapping("/do")
    @ResponseBody
    public ApiBaseRe doReport(){
        ApiBaseRe baseRe = new ApiBaseRe();


        return baseRe;
    }

    @PostMapping("/create")
    @ResponseBody
    public void createReport(){

    }

    @PostMapping("/list")
    @ResponseBody
    public ApiBaseRe getReportList( @RequestBody JSONArray testList){

        ApiBaseRe baseRe = new ApiBaseRe();


        if(testList.size()  <=0){
            baseRe.setCode(1);
            baseRe.setData(null);
        }
        try{
            Object data = apiReportService.getReportList(testList);
            baseRe.setData(data);
            baseRe.setCode(1);
        }catch (Exception e){
            baseRe.setCode(0);
            baseRe.setMsg(e.toString());
        }
        return baseRe;
    }

    @PostMapping("/token")
    @ResponseBody
    public ApiBaseRe putToken(@RequestBody Integer groupId){
        return null;
    }

//    @PostMapping("/token")
//    @ResponseBody
//    public ApiBaseRe putToken(@RequestBody JSONArray testIdList){
//        return null;
//    }

}
