package com.service.utils.test.impl;

import com.mysql.cj.xdevapi.JsonArray;
import com.service.apiTest.dom.entity.Api;
import com.service.apiTest.dom.entity.ApiCase;
import com.service.apiTest.dom.mapper.ApiCaseMapper;
import com.service.apiTest.dom.mapper.ApiMapper;
import com.service.utils.MyBaseChange;
import com.service.utils.test.dom.*;
import com.service.utils.test.method.DoApiService;
import com.service.utils.test.method.HttpClientService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.lang.invoke.SwitchPoint;
import java.util.HashMap;
import java.util.Map;

@Component
public class DoApiImpl implements DoApiService {
    @Autowired
    private StoreHost storeHost;
    @Autowired
    private HttpClientService httpClientService;
    @Autowired
    private TWebHost tWebHost;
    @Autowired
    private PdaHost pdaHost;
    @Autowired
    private ApiMapper apiMapper;
    @Autowired
    private ApiCaseMapper apiCaseMapper;
    @Autowired
    private MyBaseChange b;


    public String getAccount(String device, String environment, String deviceType) {
        MyHost myHost = this.selectHost(device);
        Integer i = Integer.parseInt(deviceType);
        if (environment.equals("uat") || environment.equals("prod")) {
            return myHost.getAccount().get(i);
        } else {
            return myHost.getAccounts().get(i);
        }
    }


    public String getHost(MyHost myHost, String environment) {
        String host = null;
        switch (environment) {
            case "uat":
                host = myHost.getUat();
                break;
            case "prod":
                host = myHost.getProd();
                break;
            case "test":
                host = myHost.getTest();
                break;
            case "tests":
                host = myHost.getTests();
                break;
        }
        return host;


    }


    public String getBasic(String device, String environment) {
        MyHost myHost = this.selectHost(device);
        if (environment.equals("uat") || environment.equals("prod")) {
            return myHost.getBasic();
        } else {
            return myHost.getBasics();
        }

    }

    public String getToken(MyHost host, String environment, String basic) {
        DoTestData data = new DoTestData();
        data.setApiMethod("2");
        if (environment.equals("uat")) {
            data.setHost("https://auth.t.zhilunkeji.com");
        }
        data.setApiPath("/sms/code");
        data.setBodyParam("{\"deviceId\":\"719910247738029\",\"mobile\":\"12900000001\"}");

        httpClientService.getResponse(data).toString();
        data.setApiPath("/oauth/token");
        data.setAuthorization("Basic TU9CSUxFX1NFUlZJQ0VfQ0FSOjU4NjgzZmU4ZWU2MDRmN2I5MTlhYzM0YTFmMjVkOGUy");
        data.setBodyParam(null);
        JSONArray objects = new JSONArray("[{\"name\":\"smsCode\",\"value\":\"cf79ae6addba60ad018347359bd144d2\"},{\"name\":\"deviceId\",\"value\":\"719910247738029\"},{\"name\":\"mobile\",\"value\":\"12900000001\"},{\"name\":\"version\",\"value\":\"2.7.7\"},{\"name\":\"grant_type\",\"value\":\"sms_code\"}]");
        data.setWebformParam(objects);
        return httpClientService.getResponse(data).toString();


    }

    public MyHost selectHost(String device) {
        switch (device) {
            case "1":
                return tWebHost;
            case "2":
                return storeHost;
            default:
                return null;

        }


    }

    /**
     * 获取一个登入的dotestdata
     *
     * @param environment
     * @param device
     * @param userType
     * @return
     */
    @Override
    public DoTestData getLoginData(String environment, String device, String userType) {
        DoTestData data = new DoTestData();
        MyHost myHostData = selectHost(device);
        data.setHost(getHost(myHostData, environment));
        data.setApiMethod("2");
        JSONArray webform = new JSONArray();
        if (device.equals("7")) {
            data.setApiPath("/scan/login.do");
            JSONObject username = new JSONObject();
            JSONObject password = new JSONObject();
            JSONObject scanVersion = new JSONObject();
            JSONObject y = new JSONObject();
            JSONObject x = new JSONObject();
            JSONObject deviceNum = new JSONObject();
            username.put("username", myHostData.getName());
            password.put("password", myHostData.getPassword());
            scanVersion.put("scanVersion", "20200303");
            x.put("x", "113.236565");
            y.put("y", "35.250118");
            deviceNum.put("deviceNum", "ZX1G42CPJD");
        } else {
            data.setApiPath("/oauth/token");
            JSONObject username = new JSONObject();
            JSONObject password = new JSONObject();
            JSONObject grantType = new JSONObject();
            username.put("name", "username");
            username.put("value", myHostData.getName());
            password.put("name", "password");
            password.put("value", myHostData.getPassword());
            grantType.put("name", "grant_type");
            grantType.put("value", "password");
            webform.put(username);
            webform.put(password);
            webform.put(grantType);
        }
        data.setWebformParam(webform);
        data.setAuthorization("Basic " + getBasic(device, environment));
        return data;
    }


    public DoTestData getSmCodeData(String environment, String device, String userType) {
        DoTestData data = new DoTestData();
        MyHost myHostData = selectHost(device);
        data.setHost(getHost(myHostData, environment));
        data.setApiPath("/sms/code");
        JSONArray array = new JSONArray();
        JSONObject smsCode = new JSONObject();
        JSONObject deviceId = new JSONObject();
        JSONObject mobile = new JSONObject();
        JSONObject grant = new JSONObject();
        return null;
    }

    @Override
    public DoTestData getTestData(String environment, Integer testId) {
        DoTestData data = new DoTestData();
        ApiCase apiCase = apiCaseMapper.getApiCaseData(testId);
        Integer apiId = apiCase.getApiId();
        Api api = apiMapper.getApiData(apiId);
        //请求方法
        data.setApiMethod(api.getApiMethod());
        //uri
        data.setApiPath(api.getApiPath());
        //获取host基础数据
        MyHost myHostData = selectHost(api.getDevice());
        //host
        data.setHost(getHost(myHostData, environment));
        //apiParam
        String apiParamType = api.getApiParamType();
        switch (apiParamType) {
            case "1":
                data.setApiParam(api.getApiFiexdParam());
                break;
            case "3":
                data.setApiParam(apiCase.getApiHandleParam());
                break;
            case "2":
                break;
        }
        //header
        data.setHeaderParam(this.OAddOs(api.getHeaderParamType(), api.getHeaderFiexdParam(), apiCase.getHeaderHandleParam()));
        //webform
        data.setWebformParam(this.OAddOs(api.getWebformParamType(),api.getWebformFiexdParam(),apiCase.getWebformHandleParam()));
        //token
        data.setAuthorization("bearer acfae59f-333f-4f1c-a024-075ff02c5ae9");
        return data;
    }

    @Override
    public String getToken(ResponseData data) throws Throwable {
        if(data.getStatus().equals("200")){
            JSONObject response = new JSONObject(data.getResponse());
            String token  = "bearer "+ response.get("access_token").toString();
            return token;
        }else {
            throw new Throwable(data.getResponse());
        }

    }

    /**
     * 忍法 数据合体之术
     */
    public JSONArray OAddO(JSONArray a1, JSONArray a2) {
        JSONArray headerParam = new JSONArray("[]");
        for (Object handleParams : a2) {
            JSONObject handleParam = new JSONObject(handleParams.toString());
            String name = handleParam.get("name").toString();
            for (Object fiexdParams : a1) {
                JSONObject fiexdParam = new JSONObject(fiexdParams.toString());
                if (name.equals(fiexdParam.get("name").toString())) {
                    headerParam.put(fiexdParam);
                } else {
                    headerParam.put(handleParam);
                }
            }
        }
        return headerParam;
    }

    /**
     * 道法 多重数据合体之术
     */
    public JSONArray OAddOs(String type, String fiexdParam, String handleParm) {
        JSONArray param = new JSONArray("[]");
        JSONArray paramType = new JSONArray(type);
        if (paramType.length() >= 1) {
            param = new JSONArray(fiexdParam);
            if (paramType.length() > 1) {
                for (Object pt : paramType) {
                    switch (pt.toString()) {
                        case "2":
                            break;
                        case "3":
                            JSONArray handleParam = new JSONArray(handleParm);
                            param = this.OAddO(param, handleParam);
                            break;
                    }
                }
            }
        }
        return param;
    }


}
