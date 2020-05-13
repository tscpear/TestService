package com.service.apiTest.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.service.apiTest.dom.domin.ApiCaseForReport;
import com.service.apiTest.dom.entity.Api;
import com.service.apiTest.dom.entity.ApiCase;
import com.service.apiTest.dom.entity.ApiReport;
import com.service.apiTest.dom.entity.Token;
import com.service.apiTest.dom.mapper.*;
import com.service.apiTest.service.domian.ApiReportList;
import com.service.apiTest.service.domian.OneReportData;
import com.service.apiTest.service.service.ApiReportService;
import com.service.utils.MyBaseChange;
import com.service.utils.test.dom.DoTestData;
import com.service.utils.test.dom.ResponseData;
import com.service.utils.test.method.DoApiService;
import com.service.utils.test.method.HttpClientService;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jayway.jsonpath.JsonPath;

import javax.sound.midi.Soundbank;
import java.text.DecimalFormat;
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
    @Autowired
    private ApiMapper apiMapper;
    @Autowired
    private ApiReportMapper apiReportMapper;
    @Autowired
    private MyBaseChange b;

    @Override
    public List<ApiReportList> getReportList(JSONArray testIdList) {
        List<ApiReportList> apiCaseList = apiCaseMapper.getApiCaseListForReport(testIdList);

        return apiCaseList;
    }

    @Override
    public long doTest(JSONArray testId, String environment) {
        long reportId = System.currentTimeMillis();
        this.addReportMain(testId.toString(), reportId);
        Token token = tokenMapper.getData();
        for (Object ids : testId) {
            Integer id = Integer.parseInt(ids.toString());
            DoTestData doTestData = doApiService.getTestData(environment, id, token);
            ResponseData responseData = httpClientService.getResponse(doTestData);
            addReport(responseData, reportId, id);
        }
        /**
         * 存入成功率
         */
        Integer all = testId.size();
        Integer success = apiReportMapper.countOfSuccess(reportId);
        String value  = success*100/all+"%";
        apiReportMainMapper.updateSuccess(value,reportId);
        return reportId;
    }

    @Override
    public void putToken(JSONArray testList, String environment) throws Throwable {
        Token tokens =tokenMapper.getData();
        List<Integer> apiIdList = apiCaseMapper.getApiIdFromApiCase(testList);
        List<String> deviceList = apiMapper.getDeviceList(apiIdList);
        for (Object device : deviceList) {
            DoTestData doTestData = doApiService.getLoginData(environment, device.toString(), "1");
            ResponseData responseData = httpClientService.getResponse(doTestData);
            String token = doApiService.getToken(responseData);
            switch (device.toString()) {
                case "1":
                    tokens.setTireWebToken(token);
                    break;
                case "2":
                    tokens.setStore1(token);
                    break;
                case "5":
                    tokens.setPdaCookie(token);

            }
        }
        tokenMapper.updateToken(tokens);
    }


    public void addReportMain(String testList, long reportId) {
        apiReportMainMapper.add(testList, reportId);
    }

    public void addReport(ResponseData data, long reportId, Integer testId) {
        ApiReport report = new ApiReport();
        report.setReportId(reportId);
        report.setTestId(testId);
        BeanUtils.copyProperties(data, report);
        ApiCase apiCase = apiCaseMapper.getApiCaseData(testId);
        Api api = apiMapper.getApiData(apiCase.getApiId());
        report.setExpectStatus(apiCase.getStatusAssertion());
        report.setActStatus(data.getStatus());
        if (apiCase.getStatusAssertion().equals(data.getStatus())) {
            report.setResultStatus(1);
        } else {
            report.setResultStatus(0);
        }

        if (report.getResultStatus() == 1) {
            Integer isRely = api.getIsRely();
            if (isRely == 1) {
                JSONObject value = new JSONObject();
                JSONArray relyParams = b.StringToAO(api.getRelyValue());
                for (Object relyParam : relyParams) {
                    JSONObject param = b.StringToJson(relyParam.toString());

                    String path = param.get("value").toString();
                    Object values = JsonPath.read(data.getResponse(), path);
                    value.put(param.get("name").toString(), values);

                }
                report.setRelyValue(value.toString());
            }
        }
        apiReportMapper.putData(report);
    }

    @Override
    public List<ApiReport> getReportDataList(long reportId) {
        return apiReportMapper.getList(reportId);
    }

    @Override
    public OneReportData getOneReport(Integer testId, long reportId) {
        OneReportData oneReportData = new OneReportData();
        ApiReport apiReport = apiReportMapper.getData(testId, reportId);
        BeanUtils.copyProperties(apiReport, oneReportData);
        oneReportData.setHeaderParam(b.StringToAO(apiReport.getHeaderParam()));
        oneReportData.setWebformParam(b.StringToAO(apiReport.getWebformParam()));
        oneReportData.setBodyParam(b.StringToAO(apiReport.getBodyParam()));
        oneReportData.setResponse(b.StringToJson(apiReport.getResponse()));


        return oneReportData;
    }


}
