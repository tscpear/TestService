package com.service;

import com.service.apiTest.dom.mapper.ApiCaseMapper;
import com.service.apiTest.service.impl.ApiServicelmpl;
import com.service.apiTest.service.service.ApiReportService;
import com.service.utils.test.dom.DoTestData;
import com.service.utils.test.dom.ResponseData;
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
       DoTestData doTestData = doApiService.getLoginData("uat","1","1");
        System.out.println(httpClientService.getResponse(doTestData));
    }
    @Test
    void test1(){
        DoTestData doTestData = doApiService.getLoginData("uat","1","1");
        ResponseData data = httpClientService.getResponse(doTestData);
        apiReportService.addReport(data,11,11);
    }
//    @Test
//    void test2(){
//       tireDataService.doTest("uat","sss");
//    }

}
