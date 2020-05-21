package com.service.utils.test.method;

import com.service.apiTest.dom.entity.ApiCase;
import com.service.apiTest.dom.entity.Token;
import com.service.utils.test.dom.DoTestData;
import com.service.utils.test.dom.GetToken;
import com.service.utils.test.dom.MyHost;
import com.service.utils.test.dom.ResponseData;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.crypto.Data;

@Service
public interface DoApiService {
//
//    /**
//     * 根据环境、device、获取
//     */
//    String getAccount(String device,String enviroment,String deviceType);
//
//    /**
//     * 获取host
//     * @param device
//     * @param environment
//     * @return
//     */
//
//    String getHost(String device,String environment);
//
//
//    /**
//     * 获取basic
//     * @param device
//     * @param environment
//     * @return
//     */
//    String getBasic(String device,String environment);
//
//
//
//    String getToken(MyHost host, String environment, String basic);

    DoTestData getLoginData(String environment, String device, String userType);

    /**
     * 拼接测试用例
     * @param environment
     * @param testId
     * @return
     */
    DoTestData getTestData(String environment, Integer testId, Token token,long reportId);


    /**
     * 分析返回值
     */
    String getToken(ResponseData data) throws Throwable;

    /**
     * 通过device 获取需要的账号
     */

}
