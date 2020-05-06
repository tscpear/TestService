package com.service.utils.test.method;

import com.service.apiTest.dom.entity.ApiCase;
import com.service.utils.test.dom.GetToken;
import org.springframework.stereotype.Service;

@Service
public interface DoApiService {

    /**
     * 根据环境、device、获取
     */
    String getAccount(String device,String enviroment,String deviceType);

    /**
     * 获取host
     * @param device
     * @param environment
     * @return
     */

    String getHost(String device,String environment);


    /**
     * 获取basic
     * @param device
     * @param environment
     * @return
     */
    String getBasic(String device,String environment);



    String getToken(String account,String environment,String basic);
}
