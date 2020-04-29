package com.service.utils.test.impl;

import com.service.utils.test.dom.GetToken;
import com.service.utils.test.dom.MyHost;
import com.service.utils.test.dom.StoreHost;
import com.service.utils.test.method.DoApiService;
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
    public String getToken(String account, String host, String basic) {
        return null;
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
