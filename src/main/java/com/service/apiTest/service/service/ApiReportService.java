package com.service.apiTest.service.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.service.apiTest.controller.domin.PutToken;
import com.service.apiTest.dom.domin.ApiCaseForReport;
import com.service.apiTest.dom.entity.ApiReport;
import com.service.apiTest.service.domian.ApiReportList;
import com.service.apiTest.service.domian.DeviceAndType;
import com.service.apiTest.service.domian.OneReportData;
import com.service.utils.test.dom.ResponseData;
import com.service.utils.test.dom.project.Device;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Service
public interface ApiReportService {
    /**
     * 执行前的报告集
     * @param testIdList
     * @return
     */
    List<ApiReportList> getReportList(JSONArray testIdList);


    long doTest(JSONArray testId,Integer environment,long reportId,List<String> accountValue,Integer projectId,Integer b);


    void putToken(JSONArray s,Integer environment) throws Throwable;

    /**
     * 创建主报告
     */
     void addReportMain(long reportId);

    /**
     * 存入详情
     */
    void addReport(ResponseData data,long reportId,Integer testId,Integer b);

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

    /**
     * 获取账号列表
     * @param deviceList
     * @param projectId
     * @param environment
     * @return
     */
    List<DeviceAndType> getDeviceTypeAndAccountList(JSONArray deviceList, Integer projectId, Integer environment);

    /**
     * 新的固定token的方法
     * @param putToken
     */
    String accountLogin(PutToken putToken,Integer projectId);


}
