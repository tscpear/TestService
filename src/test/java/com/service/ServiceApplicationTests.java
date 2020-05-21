package com.service;

import com.service.apiTest.dom.entity.Token;
import com.service.apiTest.dom.mapper.ApiCaseMapper;
import com.service.apiTest.dom.mapper.TokenMapper;
import com.service.apiTest.service.impl.ApiServicelmpl;
import com.service.apiTest.service.service.ApiReportService;
import com.service.apiTest.service.service.CreateTireDataService;
import com.service.utils.MyBaseChange;
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
    @Autowired
    private CreateTireDataService createTireDataService;
    @Autowired
    private TokenMapper tokenMapper;
    @Autowired
    private MyBaseChange m;


    @Test
    void contextLoads() throws Throwable {
       DoTestData doTestData = doApiService.getLoginData("uat","1","1");
        System.out.println(httpClientService.getResponse(doTestData));
    }
    @Test
    void test1(){
        DoTestData doTestData = doApiService.getLoginData("test","5","1");
        ResponseData data = httpClientService.getResponse(doTestData);
        apiReportService.addReport(data,11,11);
    }
//    @Test
//    void test2(){
//       tireDataService.doTest("uat","sss");
//    }
    @Test
    void test2(){
        System.out.println( m.removetest( "[  \"A0D1F14150\",\"B0H5F12744\",\"B0H5F12757\",\"A0C6C20896\",\"D0D6C50340\",\"D0D6B50410\",\"D0D6B50409\",\"D0D6C50330\",\"K9A6D20838\",\"K9A7A20975\",\"K9H9F13050\",\"C0J8C22933\",\"L9C1A51070\",\"L9C1A51032\",\"K9H9F12956\",\"K9H0F13497\",\"K9A6C20781\",\"K9A6C20712\",\"K9A7A20982\",\"K9A6C20714\",\"K9A7B20760\",\"K9A7D20876\",\"K9A4D20488\",\"K9A6A20906\",\"K9A6B20855\",\"K9A6A20931\",\"K9A6A20882\",\"K9A8D20726\",\"K9A7A20967\",\"K9A6D20811\",\"D0J8A20100\",\"K9A7D20877\",\"C0J8B23719\",\"K9A7C20864\",\"K9A6A20943\",\"K9A7A20976\",\"D0H5A20384\",\"D0I7D20268\",\"D0H5B20361\",\"D0H5D20109\",\"D0H5A20332\",\"D0H5B20316\",\"D0H5A20271\",\"A0C6B20911\",\"L9A1B22160\",\"L9A1B22173\",\"L9A1C22229\",\"L9A1C22205\",\"A0C6A20884\",\"I9I2D20471\",\"I9I2B20396\",\"I9I2A20634\",\"I9I1C20295\",\"I9I2A20597\",\"I9I2C20423\",\"A0C8D20405\",\"A0C8D20404\",\"D0I6B20932\",\"D0J6A20906\",\"A0A6C20798\",\"C0G5F14649\",\"C0G5F14708\",\"C0G5F14664\",\"C0G5F14637\",\"C0G5F14601\",\"C0G5F14610\",\"C0G5F14791\",\"C0G5F14841\",\"C0G5F14714\",\"C0G5F14858\",\"C0G5F14718\",\"D0I1C20843\",\"D0J6A20922\",\"D0E8D51270\",\"D0E8C51359\",\"D0G7C51006\",\"D0E4B51274\",\"L9H8B20683\",\"L9J3C21126\",\"D0H5D21622\",\"D0H5C21174\",\"D0H5D21346\",\"K9A7B51498\",\"K9D6C51831\",\"B0J7D21379\",\"B0H5D21305\",\"B0I1D21173\",\"B0H5D21296\",\"B0J7B21256\",\"B0I1D21254\",\"D0H5B20431\",\"D0H5A20341\",\"D0J6B20780\",\"D0J6B20858\",\"D0I6A21100\",\"D0I6A21088\",\"D0I6A21025\",\"K9A3D21258\",\"I9X4F12160\",\"I9X4F12208\",\"J9W6F17711\",\"I9X4F12174\",\"J9W6F17696\",\"I9X4F12088\",\"I9X4F12113\",\"J9W6F17706\",\"J9W5F11365\",\"I9X4F12157\",\"C0Q8B21885\",\"C0Q1B20186\",\"C0Q8B21804\",\"C0Q2B22368\",\"J9H0F14053\",\"J9D5F12357\",\"J9D5F12036\",\"L9C3D21037\",\"H9A6B21006\",\"H9A6B20800\",\"H9A6B20824\",\"L9A5D20804\",\"L9A6B21012\",\"K9A6C21202\",\"K9A6A20437\",\"K9A6A20454\",\"K9A5D20303\",\"L9A5C20985\",\"L9A6C20851\",\"K9A6D20279\",\"L9A6C21071\",\"L9A6B21160\",\"L9A5A20666\",\"L9A5D20968\",\"K9A6B20362\",\"L9A5D20961\",\"L9A5C21009\",\"K9A5C20260\",\"K9A5C20963\",\"K9A5D20262\",\"K9A5A21147\",\"K9A5C20979\",\"K9A5C21000\",\"K9A5A21128\",\"L9A5D20999\",\"L9A6C20852\",\"K9A5A21235\",\"L9A6C21073\",\"L9A6C21084\",\"L9A6C21083\",\"K9A5B21120\",\"K9A5B21087\",\"K9A5B21089\",\"K9B1B21525\",\"K9B1B21546\",\"K9B1B21543\",\"K9B1B21573\",\"K9A8B21432\",\"H9J5B20330\",\"H9J5B20292\",\"H9J5C20529\",\"L9X6F14119\",\"J9B8D21590\",\"C0D5F12073\",\"C0A5F13088\",\"C0A5F12241\",\"D0H5A20314\",\"C0H4D22326\",\"I9C2A21340\",\"J9C2B21943\",\"I9C2B21586\",\"L9C8B20944\",\"L9C8A20693\",\"L9C8D20849\",\"C0J7D23552\",\"C0J7C23374\",\"H9G2F10848\",\"H9G5F10900\",\"H9G5F10916\",\"K9G1F11797\",\"L9F3C51107\",\"L9F9D51527\",\"L9A6B22037\",\"L9A4C22048\",\"L9A6B22042\",\"A0A6C20799\",\"A0A6C20787\",\"A0B2A50021\",\"L9C1C51451\",\"B0H8B20058\",\"A0C2A20170\",\"A0C2C20377\",\"L9A7A20089\",\"L9A8D20065\",\"L9A7B20263\",\"L9J2D21300\",\"K9H3A20385\",\"K9H7A20249\",\"C0H6D21030\",\"C0H6D21052\",\"C0H6B20015\",\"C0J3C21603\"]"));
    }


}
