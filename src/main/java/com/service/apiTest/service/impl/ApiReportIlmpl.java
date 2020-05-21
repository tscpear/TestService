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
import java.util.ArrayList;
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
    public long doTest(JSONArray testId, String environment, long reportId) {
        Token token = tokenMapper.getData();
        /**
         * testId 自动排序
         * @1获取testId isdepend relyCaseId
         *
         */
        for (Object ids : testId) {
            Integer id = Integer.parseInt(ids.toString());
            DoTestData doTestData = doApiService.getTestData(environment, id, token, reportId);
            ResponseData responseData = httpClientService.getResponse(doTestData);
            addReport(responseData, reportId, id);
        }
        /**
         * 存入成功率
         */
        Integer all = testId.size();
        Integer success = apiReportMapper.countOfSuccess(reportId);
        String value = success * 100 / all + "%";
        apiReportMainMapper.updateSuccess(value, reportId);
        return reportId;
    }

    @Override
    public void putToken(JSONArray testList, String environment) throws Throwable {
        Token tokens = tokenMapper.getData();
        List<String> deviceTypeList = apiCaseMapper.getDeviceType(testList);
        deviceTypeList = b.removeSame(deviceTypeList);

        for (String deviceType : deviceTypeList) {
            String device = "";
            if(deviceType.contains(".")){
                device = deviceType.split("\\.")[0];
            }else {
                device = deviceType;
            }
            DoTestData doTestData = doApiService.getLoginData(environment, device, deviceType);
                ResponseData responseData = httpClientService.getResponse(doTestData);
            String token = doApiService.getToken(responseData);
            switch (deviceType) {
                case "1":
                    tokens.setTireWebToken(token);
                    break;
                case "2.1":
                    tokens.setStore1(token);
                    break;
                case "2.2":
                    tokens.setStore2(token);
                    break;
                case "2.3":
                    tokens.setStore3(token);
                    break;
                case "2.4":
                    tokens.setStore4(token);
                    break;
                case "2.5":
                    tokens.setStore5(token);
                    break;
                case "2.6":
                    tokens.setStore6(token);
                    break;
                case "2.7":
                    tokens.setStore7(token);
                    break;
                case "3.1":
                    tokens.setDriver1(token);
                    break;
                case "3.2":
                    tokens.setDriver2(token);
                    break;
                case "5":
                    tokens.setPdaCookie(token);
                    break;

            }
        }
        tokenMapper.updateToken(tokens);
    }

    @Override
    public void addReportMain(long reportId) {
        apiReportMainMapper.add(reportId);
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
        report.setDevice(api.getDevice());
        report.setDeviceType(apiCase.getDeviceType()    );
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
                    try {
                        List<Object> values = JsonPath.read(data.getResponse(), path);
                        value.put(param.get("name").toString(), values.get(0));
                    } catch (Exception e) {

                    }
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
    public OneReportData getOneReport(Integer id) {
        OneReportData oneReportData = new OneReportData();
        ApiReport apiReport = apiReportMapper.getData(id);
        BeanUtils.copyProperties(apiReport, oneReportData);
        oneReportData.setHeaderParam(b.StringToAO(apiReport.getHeaderParam()));
        oneReportData.setWebformParam(b.StringToAO(apiReport.getWebformParam()));
        try {
            oneReportData.setBodyParam(b.StringToAO(apiReport.getBodyParam()));
        } catch (Exception e) {
            oneReportData.setBodyParam(b.StringToJson(apiReport.getBodyParam()));
        }
        oneReportData.setResponse(b.StringToJson(apiReport.getResponse()));
        return oneReportData;
    }

}
