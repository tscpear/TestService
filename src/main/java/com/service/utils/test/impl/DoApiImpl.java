package com.service.utils.test.impl;

import com.mysql.cj.xdevapi.JsonArray;
import com.service.apiTest.dom.entity.Api;
import com.service.apiTest.dom.entity.ApiCase;
import com.service.apiTest.dom.entity.Token;
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
import javax.validation.groups.Default;
import java.lang.invoke.SwitchPoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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


    public String getHost(MyHost myHost, String environment,boolean isLogin,String device) {
        String host = null;
        switch (environment) {
            case "uat":
                if(isLogin && device.equals("2")){
                    host = myHost.getUatl();
                }else {
                    host = myHost.getUat();
                }
                break;
            case "prod":
                if(isLogin && device.equals("2")){

                }else{
                    host = myHost.getProd();
                }
                break;
            case "test":
                if(isLogin && device.equals("2")){
                    host = myHost.getTestl();
                }else {
                    host = myHost.getTest();
                }
                break;
            case "tests":
                if(isLogin && device.equals("2")){
                    host= myHost.getTestsl();
                }else{
                    host = myHost.getTests();
                }

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
            case "5":
                return pdaHost;
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
        data.setHost(getHost(myHostData, environment,true,device));
        data.setApiMethod("2");
        JSONArray webform = new JSONArray();
        if (device.equals("5")) {
            data.setApiPath("/scan/login.do");
            JSONObject username = new JSONObject();
            JSONObject password = new JSONObject();
            JSONObject scanVersion = new JSONObject();
            JSONObject y = new JSONObject();
            JSONObject x = new JSONObject();
            JSONObject deviceNum = new JSONObject();
            username.put("name", "username");
            username.put("value", myHostData.getName());
            password.put("name","password");
            password.put("value", myHostData.getPassword());
            scanVersion.put("name", "scanVersion");
            scanVersion.put("value", "20200303");
            x.put("name", "x");
            x.put("value", "113.236565");
            y.put("name", "y");
            y.put("value", "35.250118");
            deviceNum.put("name", "deviceNum");
            deviceNum.put("value", "ZX1G42CPJD");
            webform.put(username);
            webform.put(password);
            webform.put(scanVersion);
            webform.put(x);
            webform.put(y);
            webform.put(deviceNum);
        } else if (device.equals("2")) {
           ResponseData data1 =  httpClientService.getResponse(this.getSmCodeData(environment, device, userType));
            data.setApiPath("/oauth/token");
            JSONObject mobile = new JSONObject();
            JSONObject deviceId = new JSONObject();
            JSONObject smsCode = new JSONObject();
            JSONObject version = new JSONObject();
            JSONObject grant = new JSONObject();
            List<String> mobileList = new ArrayList<>();
            if (environment.equals("uat")) {
                mobileList = myHostData.getAccount();
            } else {
                mobileList = myHostData.getAccounts();
            }
            mobile.put("name", "mobile");
            mobile.put("value", mobileList.get(0));
            deviceId.put("name", "deviceId");
            deviceId.put("value", "719910247738029");
            smsCode.put("name", "smsCode");
            smsCode.put("value", "cf79ae6addba60ad018347359bd144d2");
            version.put("name", "version");
            version.put("value", "2.7.7");
            grant.put("name", "grant_type");
            grant.put("value", "sms_code");
            webform.put(mobile);
            webform.put(deviceId);
            webform.put(smsCode);
            webform.put(version);
            webform.put(grant);

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
        data.setApiMethod("2");
        MyHost myHostData = selectHost(device);
        data.setHost(getHost(myHostData, environment,true,device));
        data.setApiPath("/sms/code");
        data.setAuthorization("Basic " + getBasic(device, environment));
        List<String> mobileList = new ArrayList<>();
        if (environment.equals("uat")) {
            mobileList = myHostData.getAccount();
        } else {
            mobileList = myHostData.getAccounts();
        }
        JSONObject json = new JSONObject();
        json.put("deviceId", "719910247738029");
        json.put("mobile", mobileList.get(0));
        JSONArray header = new JSONArray();
        JSONObject o = new JSONObject();
        o.put("name", "Content-Type");
        o.put("value", "application/json");
        header.put(o);
        data.setHeaderParam(header);
        data.setBodyParam(json.toString());
        return data;
    }

    @Override
    public DoTestData getTestData(String environment, Integer testId, Token token) {
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
        data.setHost(getHost(myHostData, environment,false, api.getDevice()));
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
        data.setWebformParam(this.OAddOs(api.getWebformParamType(), api.getWebformFiexdParam(), apiCase.getWebformHandleParam()));
        //token
        String authorization ="";
        JSONObject tokenData;
        switch (api.getDevice()){
            case "1":
                tokenData = new JSONObject(token.getTireWebToken());
                authorization = tokenData.get("token_type").toString()+" " + tokenData.get("access_token").toString();
                break;
            case "2":
                tokenData = new JSONObject(token.getStore1());
                authorization = tokenData.get("token_type").toString()+" " + tokenData.get("access_token").toString();
                break;
            case "5":
                tokenData = new JSONObject(token.getPdaCookie());
                authorization = tokenData.get("cookie").toString();
                break;

        }
        data.setAuthorization(authorization);

        /**
         * 登入接口的数据依赖
         */


        return data;
    }

    @Override
    public String getToken(ResponseData data) throws Throwable {
        if (data.getStatus().equals("200")) {
            JSONObject response = new JSONObject(data.getResponse());
            String cookie = data.getCookie();
            response.put("cookie",cookie);
            return response.toString();
        } else {
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
            Integer a1Size = a1.length();
            int i =0;
            for (Object fiexdParams : a1) {
                i++;
                JSONObject fiexdParam = new JSONObject(fiexdParams.toString());
                if (name.equals(fiexdParam.get("name").toString())) {
                    headerParam.put(handleParam);
                    break;
                }
                if(i==a1Size){
                    headerParam.put(fiexdParam);
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
