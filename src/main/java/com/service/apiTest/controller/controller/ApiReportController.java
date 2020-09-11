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
import com.service.utils.MyBaseChange;
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
    @Autowired
    private MyBaseChange b;


    @PostMapping("/do")
    @ResponseBody
    public ApiBaseRe doReport(@RequestBody PutToken token,@RequestHeader(name = "projectId")Integer projectId) {
        ApiBaseRe baseRe = new ApiBaseRe();
        long reportId;
        if(token.getReportId()<10000){
            reportId = System.currentTimeMillis();
            apiReportService.addReportMain(reportId);
        }else {
            reportId = token.getReportId();
        }
        apiReportService.doTest(this.getTestList(token), token.getEnvironment(), reportId,token.getAccountValue(),projectId,1);
        JSONObject data = new JSONObject();
        data.put("list",apiReportService.getReportDataList(reportId));
        data.put("reportId",reportId);
        baseRe.setData(data);
        baseRe.setCode(1);
        baseRe.setMsg("执行成功");

//

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
            List<ApiReportList> list = apiReportService.getReportList(this.getTestList(token));
            JSONObject data = new JSONObject();
            data.put("list",list);
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
    public ApiBaseRe putToken(@RequestBody PutToken token,@RequestHeader(name = "projectId")Integer projectId) throws Throwable {
        ApiBaseRe baseRe = new ApiBaseRe();
//        try {
            String msg = apiReportService.accountLogin(token,projectId);
            if(msg.equals("")){
                baseRe.setCode(1);
                baseRe.setMsg("token固定成功，确保账号不被重新登入");
            }else {
                baseRe.setCode(0);
                baseRe.setMsg(msg);
            }

//        } catch (Exception e) {
//            baseRe.setMsg(e.toString());
//            baseRe.setCode(0);
//        }
        return baseRe;
    }

    @GetMapping("/one")
    @ResponseBody
    public ApiBaseRe getOneReport(@RequestParam Integer id) {
        ApiBaseRe baseRe = new ApiBaseRe();
        try {
            baseRe.setData(apiReportService.getOneReport(id));
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
                                   @RequestParam int limit,
                                   @RequestHeader(name = "projectId") Integer projectId) {
        ApiBaseRe baseRe = new ApiBaseRe();
        ApiListParam param = new ApiListParam();
        param.setPageBegin(page * limit - limit);
        param.setPageEnd(limit);
        param.setProjectId(projectId);
        baseRe.setCode(1);
        baseRe.setData(apiReportMainMapper.getList(param));
        baseRe.setCount(apiReportMainMapper.getCount(projectId));
        return baseRe;
    }
    /**
     * 获取报告数据
     */
    @PostMapping("reportList")
    @ResponseBody
    public ApiBaseRe getReportTableList(@RequestBody PutToken token){
        ApiBaseRe baseRe = new ApiBaseRe();
        JSONObject data = new JSONObject();
        data.put("list",apiReportService.getReportDataList(token.getReportId()));
        data.put("reportId",token.getReportId());
        baseRe.setData(data);
        baseRe.setCode(1);
        baseRe.setMsg("执行成功");
        return baseRe;
    }


    /**
     * 获取账号列表与信息
     */
    @PostMapping("account")
    @ResponseBody
    public ApiBaseRe getAccout(@RequestBody PutToken putToken,@RequestHeader(name = "projectId") Integer projectId){
        ApiBaseRe baseRe = new ApiBaseRe();
        baseRe.setData(apiReportService.getDeviceTypeAndAccountList(this.getTestList(putToken),projectId,putToken.getEnvironment()));
        baseRe.setCode(1);
        return baseRe;

    }



    /**
     * 获取testList
     */
    public JSONArray getTestList(PutToken token) {
        JSONArray testList;
        if(token.getReportId()>0){
            testList = b.StringToArray(apiReportMapper.getNowDoTestId(token.getReportId()).toString());
        }else if(token.getGroupId() > 0){
            String s = apiGroupMapper.getTestList(token.getGroupId());
            testList = JSONArray.parseArray(s);
        }else {
            testList = token.getTestList();
        }
        return testList;
    }
}
