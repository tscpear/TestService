package com.service;

import com.service.apiTest.controller.domin.PutToken;
import com.service.apiTest.dom.mapper.ApiCaseMapper;
import com.service.apiTest.dom.mapper.TokenMapper;
import com.service.apiTest.service.impl.ApiReportIlmpl;
import com.service.apiTest.service.impl.ApiServicelmpl;
import com.service.apiTest.service.service.ApiReportService;
import com.service.apiTest.service.service.CreateTireDataService;
import com.service.guiTest.base.impl.DriverBaseImpl;
import com.service.guiTest.dom.entity.GuiData;
import com.service.utils.MyBaseChange;
import com.service.utils.test.dom.DoTestData;
import com.service.utils.test.dom.ResponseData;
import com.service.utils.test.dom.StoreHost;
import com.service.utils.test.dom.project.Project1;
import com.service.utils.test.method.DoApiService;
import com.service.utils.test.method.HttpClientService;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    private CreateTireDataService createTireDataService;
    @Autowired
    private TokenMapper tokenMapper;
    @Autowired
    private MyBaseChange m;

    private Project1 project1 = new Project1();
    @Autowired
    private ApiReportIlmpl apiReportIlmpl;

    @Autowired
    private DriverBaseImpl driverBaseService;



    @Test
    void contextLoads() throws Throwable {
        DoTestData doTestData = doApiService.getLoginData(1, "1", "1");
        System.out.println(httpClientService.getResponse(doTestData));
    }

    @Test
    void test1() {
        DoTestData doTestData = doApiService.getLoginData(2, "5", "1");
        ResponseData data = httpClientService.getResponse(doTestData);
        apiReportService.addReport(data, 11, 11,1);
    }

    //    @Test
//    void test2(){
//       tireDataService.doTest("uat","sss");
//    }
//
    @Test
    void test3() {
        System.out.println(project1.getDevice().get(0).getLoginParam());
    }

    //    @Test
//    void test4(){
//        List<Integer> value = new ArrayList<>();
//        value.add(1);
//        value.add(2);
//        value.add(3);
//        value.add(4);
//
//        System.out.println(apiCaseMapper.getDeviceTypeList(value));
//    }
    @Test
    void test5() {
        String q = project1.getDevice().get(1).getEnvironment().get(1).getAccount().get(0).get(0);
        System.out.println(q);
    }

    @Test
    void test6(){
        PutToken putToken = new PutToken();
        putToken.setEnvironment(1);
        List<String> accounts = new ArrayList<>();
        accounts.add("2.1.1");
        putToken.setAccountValue(accounts);
        System.out.println("你妈妈");
        apiReportService.accountLogin(putToken,1);
    }
//
//    @Test
//    void test7(){
//        driverBaseService.getWebDriver(1,1);
//        System.out.println("你妈妈");
//    }
//    @Test
//    void test8() throws InterruptedException {
//        WebDriver driver =  driverBaseService.getWebDriver(1,1);
//        GuiData data = new GuiData();
//        data.setActive(3);
//        data.setElementType(1);
//        data.setElement("username");
//        data.setKeyValue("鸟妈妈");
//        driverBaseService.doGui(driver,data);
//    }
//
//    @Test
//    void test9() throws InterruptedException {
//        List<Integer> ids = new ArrayList<>();
//        ids.add(7);
//        ids.add(8);
//        ids.add(10);
//        driverBaseService.doGuis(ids);
//    }
//
//
//    @Test
//    void test10(){
//        driverBaseService.getAndroidDriver(1,1);
//    }
//
//    @Test
//    void test11(){
//        List<Integer> ids = new ArrayList<>();
//        ids.add(11);
//        ids.add(12);
//        ids.add(13);
//        ids.add(14);
//        driverBaseService.doAndroidGuis(ids);
//    }
}
