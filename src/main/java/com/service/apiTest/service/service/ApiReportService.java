package com.service.apiTest.service.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.service.apiTest.dom.domin.ApiCaseForReport;
import com.service.apiTest.dom.entity.ApiReport;
import com.service.apiTest.service.domian.ApiReportList;
import com.service.apiTest.service.domian.OneReportData;
import com.service.utils.test.dom.ResponseData;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Service
public interface ApiReportService {
    /**
     * 执行前的报告集
     * @param testIdList
     * @return
     */
    List<ApiReportList> getReportList(JSONArray testIdList);


    long doTest(JSONArray testId,String environment,long reportId);


    void putToken(JSONArray s,String environment) throws Throwable;

    /**
     * 创建主报告
     */
     void addReportMain(long reportId);

    /**
     * 存入详情
     */
    void addReport(ResponseData data,long reportId,Integer testId);

    /**
     * 查询报告结果
     * @param reportId
     * @return
     */
    List<ApiReport> getReportDataList(long reportId);

    /**
     * 获取单个报告
     * @param testId
     * @param reportId
     * @return
     */
    OneReportData getOneReport(Integer id);
}
