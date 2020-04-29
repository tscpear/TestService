package com.service;

import com.service.utils.test.dom.StoreHost;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ServiceApplicationTests {

    @Autowired
    private StoreHost host;

    @Test
    void contextLoads() {
        System.out.println(host.getTests());
    }

}
