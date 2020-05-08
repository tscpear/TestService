package com.service;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.xdevapi.JsonArray;
import com.service.apiTest.dom.mapper.ApiCaseMapper;
import com.service.apiTest.service.impl.ApiServicelmpl;
import com.service.apiTest.service.service.ApiReportService;
import com.service.utils.test.dom.DoTestData;
import com.service.utils.test.dom.StoreHost;
import com.service.utils.test.method.DoApiService;
import com.service.utils.test.method.HttpClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ServiceApplicationTests {

    @Autowired
    private StoreHost host;

    @Autowired
    private DoApiService doApiService;
    @Autowired
    private ApiCaseMapper apiCaseMapper;

    @Autowired
    private ApiServicelmpl apiServicelmpl;
    @Autowired
    private HttpClientService httpClientService;
    @Autowired
    private ApiReportService apiReportService;

    @Test
    void contextLoads() throws Throwable {
        apiReportService.addReportMain(11,11);
    }

}
