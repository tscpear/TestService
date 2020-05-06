package com.service.utils.test.impl;

import com.mysql.cj.xdevapi.JsonArray;
import com.service.utils.test.dom.DoTestData;
import com.service.utils.test.dom.GetToken;
import com.service.utils.test.dom.MyHost;
import com.service.utils.test.dom.StoreHost;
import com.service.utils.test.method.DoApiService;
import com.service.utils.test.method.HttpClientService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;

@Component
public class DoApiImpl implements DoApiService {
    @Autowired
    private StoreHost storeHost;
    @Autowired
    private HttpClientService httpClientService;




    @Override
    public String getAccount(String device, String environment, String deviceType) {
        MyHost myHost = this.selectHost(device);
        Integer i = Integer.parseInt(deviceType);
        if (environment.equals("uat") || environment.equals("prod")) {
            return myHost.getAccount().get(i);
        } else {
            return myHost.getAccounts().get(i);
        }
    }

    @Override
    public String getHost(String device, String environment) {
        String host = null;
        MyHost myHost = this.selectHost(device);
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

    @Override
    public String getBasic(String device, String environment) {
        MyHost myHost = this.selectHost(device);
        if (environment.equals("uat") || environment.equals("prod")) {
            return myHost.getBasic();
        } else {
            return myHost.getBasics();
        }

    }

    @Override
    public String getToken(String account, String environment, String basic) {
        DoTestData data = new DoTestData();
        data.setApiMethod("2");
        if(environment.equals("uat")){
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
        MyHost myHost = new MyHost();
        switch (device) {
            case "2":
                myHost = storeHost;
                break;
        }
        return myHost;
    }


}
