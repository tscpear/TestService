package com.service.apiTest.service.service;

import com.alibaba.fastjson.JSONArray;
import com.service.apiTest.dom.domin.ApiCaseForReport;
import com.service.apiTest.dom.entity.ApiReport;
import com.service.utils.test.dom.ResponseData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ApiReportService {
    /**
     * 执行前的报告集
     * @param testIdList
     * @return
     */
    List<ApiCaseForReport> getReportList(JSONArray testIdList);


    void doTest(JSONArray testId);
}
