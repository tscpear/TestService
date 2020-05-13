package com.service.apiTest.controller.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.xdevapi.JsonArray;
import com.service.apiTest.controller.domin.ApiBaseRe;
import com.service.apiTest.controller.domin.PutToken;
import com.service.apiTest.dom.domin.ApiListParam;
import com.service.apiTest.dom.entity.ApiGroup;
import com.service.apiTest.dom.entity.ApiReport;
import com.service.apiTest.dom.entity.ApiReportMain;
import com.service.apiTest.dom.mapper.ApiGroupMapper;
import com.service.apiTest.dom.mapper.ApiReportMainMapper;
import com.service.apiTest.dom.mapper.ApiReportMapper;
import com.service.apiTest.service.domian.ApiReportList;
import com.service.apiTest.service.service.ApiReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/report")
public class ApiReportController {

    @Autowired
    private ApiReportService apiReportService;
    @Autowired
    private ApiGroupMapper apiGroupMapper;
    @Autowired
    private ApiReportMapper apiReportMapper;
    @Autowired
    private ApiReportMainMapper apiReportMainMapper;


    @PostMapping("/do")
    @ResponseBody
    public ApiBaseRe doReport(@RequestBody PutToken token) {
        ApiBaseRe baseRe = new ApiBaseRe();
        try {
            long reportId = apiReportService.doTest(this.getTestList(token), token.getEnvironment());
            baseRe.setData(apiReportService.getReportDataList(reportId));
            baseRe.setCode(1);
            baseRe.setMsg("执行成功");
        } catch (Exception e) {
            baseRe.setMsg(e.toString());
            baseRe.setCode(0);
        }
        return baseRe;
    }

    @PostMapping("/create")
    @ResponseBody
    public void createReport() {

    }

    @PostMapping("/list")
    @ResponseBody
    public ApiBaseRe getReportList(@RequestBody PutToken token) {
        ApiBaseRe baseRe = new ApiBaseRe();
        try {
            List<ApiReportList> data = apiReportService.getReportList(this.getTestList(token));
            baseRe.setData(data);
            baseRe.setCode(1);
        } catch (Exception e) {
            baseRe.setCode(0);
            baseRe.setMsg(e.toString());
        }
        return baseRe;
    }

    @PostMapping("/token")
    @ResponseBody
    public ApiBaseRe putToken(@RequestBody PutToken token) throws Throwable {
        ApiBaseRe baseRe = new ApiBaseRe();
        try {
            apiReportService.putToken(this.getTestList(token), token.getEnvironment());
            baseRe.setCode(1);
            baseRe.setMsg("token固定成功，确保账号不被重新登入");
        } catch (Exception e) {
            baseRe.setMsg(e.toString());
            baseRe.setCode(0);
        }
        return baseRe;
    }

    @GetMapping("/one")
    @ResponseBody
    public ApiBaseRe getOneReport(@RequestParam Integer testId, @RequestParam long reportId) {
        ApiBaseRe baseRe = new ApiBaseRe();
        try {
            baseRe.setData(apiReportService.getOneReport(testId, reportId));
            baseRe.setCode(1);
        } catch (Exception e) {
            baseRe.setCode(0);
            baseRe.setMsg(e.toString());
        }


        return baseRe;
    }

    @GetMapping("mainList")
    @ResponseBody
    public ApiBaseRe getMainReport(@RequestParam int page,
                                   @RequestParam int limit) {
        ApiBaseRe baseRe = new ApiBaseRe();
        ApiListParam param = new ApiListParam();
        param.setPageBegin(page * limit - limit);
        param.setPageEnd(limit);
        baseRe.setCode(1);
        baseRe.setData(apiReportMainMapper.getList(param));
        baseRe.setCount(apiReportMainMapper.getCount());
        return baseRe;
    }

    /**
     * 获取testList
     */
    public JSONArray getTestList(PutToken token) {
        JSONArray testList = new JSONArray();
        if (token.getTestList().size() >= 1 && token.getGroupId() == 0) {
            testList = token.getTestList();
        } else {
            String s = apiGroupMapper.getTestList(token.getGroupId());
            testList = JSONArray.parseArray(s);
        }
        return testList;
    }


}
