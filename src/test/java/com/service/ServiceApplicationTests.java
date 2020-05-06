package com.service;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.xdevapi.JsonArray;
import com.service.apiTest.dom.mapper.ApiCaseMapper;
import com.service.apiTest.service.impl.ApiServicelmpl;
import com.service.utils.test.dom.DoTestData;
import com.service.utils.test.dom.StoreHost;
import com.service.utils.test.method.DoApiService;
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

    @Test
    void contextLoads() {

        JSONArray array = apiServicelmpl.searchRelyName("/lynx-goods/api/good/index");
        System.out.println(array.toArray());
    }

}
