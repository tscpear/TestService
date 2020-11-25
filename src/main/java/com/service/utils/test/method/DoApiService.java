package com.service.utils.test.method;

import com.service.apiTest.dom.entity.ApiCase;
import com.service.apiTest.dom.entity.Token;
import com.service.apiTest.service.domian.ApiReportCache;
import com.service.utils.test.dom.DoTestData;
import com.service.utils.test.dom.GetToken;
import com.service.utils.test.dom.MyHost;
import com.service.utils.test.dom.ResponseData;
import org.json.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Map;

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

//    DoTestData getLoginData(Integer environment, String device, String userType);

    /**
     * 拼接测试用例
     * @param environment
     * @param testId
     * @return
     */
    DoTestData getTestData(Integer environment, Integer testId, Map<String,String> tokenList, Map<String,String> newDataList, long reportId, List<String> accountValue, Integer projectId, ApiReportCache apiReportCache) throws Throwable;


    DoTestData getTestData(Integer environment, Integer testId, Map<String,String> tokenList, Map<String,String> newDataList, long reportId, List<String> accountValue, Integer projectId) throws Throwable;


    /**
     * 分析返回值
     */
    String getToken(ResponseData data) throws Throwable;

    /**
     * 通过device 获取需要的账号
     */

    void doSmsCode(String smsHost, String smsUri, String newSmsParam, JSONArray newSmsHeader);

    /**
     * 登入接口
     * @param host
     * @param uri
     * @param param
     * @param header
     */
    ResponseData doLogin(String host,String uri,JSONArray param,JSONArray header,String authorization);



    String getDoorCookie(String host,String uri);




}
