package com.service.apiTest.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.service.apiTest.dom.domin.ApiCaseForReport;
import com.service.apiTest.dom.entity.Token;
import com.service.apiTest.dom.mapper.ApiCaseMapper;
import com.service.apiTest.dom.mapper.ApiReportMainMapper;
import com.service.apiTest.dom.mapper.TokenMapper;
import com.service.apiTest.service.service.ApiReportService;
import com.service.utils.test.dom.DoTestData;
import com.service.utils.test.dom.ResponseData;
import com.service.utils.test.method.DoApiService;
import com.service.utils.test.method.HttpClientService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ApiReportIlmpl implements ApiReportService {

    @Autowired
    private ApiCaseMapper apiCaseMapper;
    @Autowired
    private DoApiService doApiService;
    @Autowired
    private HttpClientService httpClientService;
    @Autowired
    private TokenMapper tokenMapper;
    @Autowired
    private ApiReportMainMapper apiReportMainMapper;


    @Override
    public List<ApiCaseForReport> getReportList(JSONArray testIdList) {
        List<ApiCaseForReport>  apiCaseList = apiCaseMapper.getApiCaseListForReport(testIdList);



        return apiCaseList;
    }

    @Override
    public void doTest(JSONArray testId) {
        
    }

    @Override
    public String putToken(Integer groupId) throws Throwable {
        Token tokens = new Token();
      DoTestData doTestData = doApiService.getLoginData("uat","1","1");
      ResponseData responseData = httpClientService.getResponse(doTestData);
      String token = doApiService.getToken(responseData);
      tokens.setTireWebToken(token);
      tokenMapper.updateToken(tokens);
      return token;
    }

    @Override
    public void addReportMain(Integer groupId,Integer id) {
        apiReportMainMapper.add(groupId,id);
    }

}
