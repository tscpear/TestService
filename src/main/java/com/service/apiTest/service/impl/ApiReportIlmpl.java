package com.service.apiTest.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.service.apiTest.dom.domin.ApiCaseForReport;
import com.service.apiTest.dom.mapper.ApiCaseMapper;
import com.service.apiTest.service.service.ApiReportService;
import com.service.utils.test.dom.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ApiReportIlmpl implements ApiReportService {

    @Autowired
    private ApiCaseMapper apiCaseMapper;


    @Override
    public List<ApiCaseForReport> getReportList(JSONArray testIdList) {
        List<ApiCaseForReport>  apiCaseList = apiCaseMapper.getApiCaseListForReport(testIdList);



        return apiCaseList;
    }

    @Override
    public void doTest(JSONArray testId) {
        
    }
}
