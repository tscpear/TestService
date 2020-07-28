package com.service.utils.test.impl;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.mysql.cj.xdevapi.JsonArray;
import com.service.apiTest.dom.entity.Api;
import com.service.apiTest.dom.entity.ApiCase;
import com.service.apiTest.dom.entity.ApiReportMain;
import com.service.apiTest.dom.entity.Token;
import com.service.apiTest.dom.mapper.ApiCaseMapper;
import com.service.apiTest.dom.mapper.ApiMapper;
import com.service.apiTest.dom.mapper.ApiReportMapper;
import com.service.apiTest.service.service.ApiReportService;
import com.service.utils.MyBaseChange;
import com.service.utils.test.dom.*;
import com.service.utils.test.dom.project.Device;
import com.service.utils.test.dom.project.Project;
import com.service.utils.test.dom.project.Project1;
import com.service.utils.test.method.DoApiService;
import com.service.utils.test.method.HttpClientService;
import org.apache.catalina.Host;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.validation.groups.Default;
import java.lang.invoke.SwitchPoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DoApiImpl implements DoApiService {
    @Autowired
    private StoreHost storeHost;
    @Autowired
    private HttpClientService httpClientService;
    @Autowired
    private TWebHost tWebHost;
    @Autowired
    private PdaHost pdaHost;
    @Autowired
    private DriverAppHost driverAppHost;
    @Autowired
    private ApiMapper apiMapper;
    @Autowired
    private ApiCaseMapper apiCaseMapper;
    @Autowired
    private MyBaseChange b;
    @Autowired
    private ApiReportMapper apiReportMapper;
    @Autowired
    private ApiReportService apiReportService;
    @Autowired
    private Project project1;

    public String getAccount(String device, Integer environment, String deviceType) {
        MyHost myHost = this.selectHost(device);
        Integer i = Integer.parseInt(deviceType);
        if (environment.equals("uat") || environment.equals("prod")) {
            return myHost.getAccount().get(i);
        } else {
            return myHost.getAccounts().get(i);
        }
    }


    public String getHost(MyHost myHost, Integer environment, boolean isLogin, String device) {
        String host = null;
        switch (environment) {
            case 1:
                if (isLogin && (device.equals("2") || device.equals("4") || device.equals("3"))) {
                    host = myHost.getUatl();
                } else {
                    host = myHost.getUat();
                }
                break;
            case 2:
                if (isLogin && (device.equals("2") || device.equals("4") || device.equals("3"))) {

                } else {
                    host = myHost.getProd();
                }
                break;
            case 3:
                if (isLogin && (device.equals("2") || device.equals("4") || device.equals("3"))) {
                    host = myHost.getTestl();
                } else {
                    host = myHost.getTest();
                }
                break;
            case 4:
                if (isLogin && (device.equals("2") || device.equals("4") || device.equals("3"))) {
                    host = myHost.getTestsl();
                } else {
                    host = myHost.getTests();
                }

                break;
        }
        return host;


    }


    public String getBasic(String device, Integer environment) {
        MyHost myHost = this.selectHost(device);
        if (environment.equals("uat") || environment.equals("prod")) {
            return myHost.getBasic();
        } else {
            return myHost.getBasics();
        }

    }

//    public String getToken(MyHost host, String environment, String basic) {
//        DoTestData data = new DoTestData();
//        data.setApiMethod("2");
//        if (environment.equals("uat")) {
//            data.setHost("https://auth.t.zhilunkeji.com");
//        }
//        data.setApiPath("/sms/code");
//        data.setBodyParam("{\"deviceId\":\"719910247738029\",\"mobile\":\"12900000001\"}");
//
//        httpClientService.getResponse(data).toString();
//        data.setApiPath("/oauth/token");
//        data.setAuthorization("Basic TU9CSUxFX1NFUlZJQ0VfQ0FSOjU4NjgzZmU4ZWU2MDRmN2I5MTlhYzM0YTFmMjVkOGUy");
//        data.setBodyParam(null);
//        JSONArray objects = new JSONArray("[{\"name\":\"smsCode\",\"value\":\"cf79ae6addba60ad018347359bd144d2\"},{\"name\":\"deviceId\",\"value\":\"719910247738029\"},{\"name\":\"mobile\",\"value\":\"12900000001\"},{\"name\":\"version\",\"value\":\"2.7.7\"},{\"name\":\"grant_type\",\"value\":\"sms_code\"}]");
//        data.setWebformParam(objects);
//        return httpClientService.getResponse(data).toString();
//
//
//    }

    public MyHost selectHost(String device) {
        switch (device) {
            case "1":
                return tWebHost;
            case "2":
                return storeHost;
            case "3":
                return driverAppHost;
            case "4":
                return driverAppHost;
            case "5":
                return pdaHost;
            default:
                return null;

        }


    }

    /**
     * 获取一个登入的dotestdata
     *
     * @param environment
     * @param device
     * @param userType
     * @return
     */
    @Override
    public DoTestData getLoginData(Integer environment, String device, String deviceType) {
        DoTestData data = new DoTestData();
        MyHost myHostData = selectHost(device);
        data.setHost(getHost(myHostData, environment, true, device));
//        data.setApiMethod("2");
        JSONObject webformParam = new JSONObject();
        if (device.equals("5")) {
            data.setApiPath("/scan/login.do");
            webformParam.put("username", myHostData.getName());
            webformParam.put("password", myHostData.getPassword());
            webformParam.put("scanVersion", "20200303");
            webformParam.put("x", "113.236565");
            webformParam.put("y", "35.250118");
            webformParam.put("deviceNum", "ZX1G42CPJD");
        } else if (device.equals("2") || device.equals("4") || device.equals("3")) {
            data.setApiPath("/oauth/token");
            List<String> mobileList = new ArrayList<>();
            if (environment.equals("uat")) {
                mobileList = myHostData.getAccount();
            } else {
                mobileList = myHostData.getAccounts();
            }
            String mobileValue = mobileList.get(Integer.parseInt(deviceType.split("\\.")[1]) - 1);
            String grantValue;
            if (mobileValue.equals("13588096710")) {
                grantValue = "store_password";
                webformParam.put("password", "21218cca77804d2ba1922c33e0151105");
                webformParam.put("application", "app_zhilun");

            } else {
                ResponseData data1 = httpClientService.getResponse(this.getSmCodeData(environment, device, deviceType));
                webformParam.put("smsCode", "cf79ae6addba60ad018347359bd144d2");
                grantValue = "sms_code";
            }
            webformParam.put("mobile", mobileValue);
            webformParam.put("deviceId", "719910247738029");
            webformParam.put("grant_type", grantValue);
            if (device.equals("4") || device.equals("3")) {
                webformParam.put("application", "app_driver");
                webformParam.put("appVersionCode", "132");
            } else {
                webformParam.put("version", "2.7.7");
            }

        } else {
            data.setApiPath("/oauth/token");
            webformParam.put("username", myHostData.getName());
            webformParam.put("password", myHostData.getPassword());
            webformParam.put("grant_type", "password");
        }
        data.setWebformParam(b.oToA(webformParam, "name", "value"));
        data.setAuthorization("Basic " + getBasic(device, environment));
        return data;
    }


    public DoTestData getSmCodeData(Integer environment, String device, String deviceType) {

        DoTestData data = new DoTestData();
//        data.setApiMethod("2");
        MyHost myHostData = selectHost(device);
        data.setHost(getHost(myHostData, environment, true, device));
        data.setApiPath("/sms/code");
        data.setAuthorization("Basic " + getBasic(device, environment));
        List<String> mobileList = new ArrayList<>();
        if (environment.equals("uat")) {
            mobileList = myHostData.getAccount();
        } else {
            mobileList = myHostData.getAccounts();
        }
        JSONObject json = new JSONObject();
        json.put("deviceId", "719910247738029");
        json.put("mobile", mobileList.get(Integer.parseInt(deviceType.split("\\.")[1]) - 1));
        if (device.equals("4")) {
            json.put("application", "app_driver");
            json.put("appVersionCode", "132");
        }
        JSONArray header = new JSONArray();
        JSONObject o = new JSONObject();
        o.put("name", "Content-Type");
        o.put("value", "application/json");
        header.put(o);
        data.setHeaderParam(header);
        data.setBodyParam(json.toString());
        return data;
    }

    @Override
    public DoTestData getTestData(Integer environment, Integer testId, Map<String, String> tokenList, Map<String, String> newDataList, long reportId, List<String> accountValue, Integer projectId) {
        Project project = new Project();
        switch (projectId) {
            case 1:
                project = project1;
                break;
        }
        DoTestData data = new DoTestData();
        ApiCase apiCase = apiCaseMapper.getApiCaseData(testId);
        Integer apiId = apiCase.getApiId();
        Api api = apiMapper.getApiData(apiId);
        Integer deviceId = api.getDevice();
        Integer deviceType = apiCase.getDeviceType();
        String key = deviceId + "." + deviceType;
        Map<String, String> paths = project.getDevice().get(deviceId - 1).getLoginRely();
        String loginRely = newDataList.get(key);
        String tokenValue = tokenList.get(key);




        /**
         * 判断被依赖的测试用例是否执行了
         */
        if (apiCase.getIsDepend() == 1) {
            JSONArray testList = new JSONArray(apiCase.getRelyCaseId());
            List<Integer> nowDoTestList = apiReportMapper.getNowDoTestId(reportId);
            JSONArray relyTestListId = new JSONArray();
            for (Object o : testList) {
                JSONObject os = new JSONObject(o.toString());
                Integer relyTestId = Integer.parseInt(os.get("apiCaseId").toString());
                if (relyTestId > 0) {
                    System.out.println(!nowDoTestList.contains(relyTestId));
                    if (!nowDoTestList.contains(relyTestId)) {
                        relyTestListId.put(relyTestId);
                    }
                }

            }
            com.alibaba.fastjson.JSONArray array = b.StringToArray(relyTestListId.toString());
            if (array.size() > 0) {
                apiReportService.doTest(b.StringToArray(relyTestListId.toString()), environment, reportId, accountValue, projectId);

            }


        }


        /**
         * 合成依赖数据
         */
        Map<Integer, Map<String, String>> newRely = new HashMap<>();
        if (apiCase.getIsDepend() == 1) {
            newRely = this.getRelyValue(paths, apiCase, loginRely, reportId);
        }


        //请求方法
        data.setApiMethod(api.getApiMethod());
        //uri
        data.setApiPath(api.getApiPath());
        //获取host基础数据
//        MyHost myHostData = selectHost(api.getDevice());
        //host
//        data.setHost(getHost(myHostData, environment, false, api.getDevice()));
        //apiParam


        /**
         * host
         */


        String host = project.getDevice().get(deviceId - 1).getEnvironment().get(environment - 1).getHost();
        data.setHost(host);
        String apiParamType = api.getApiParamType();
        switch (apiParamType) {
            case "1":
                data.setApiParam(api.getApiFiexdParam());
                break;
            case "3":
                data.setApiParam(apiCase.getApiHandleParam());
                break;
            case "2":
                if (apiCase.getIsDepend() == 1) {
                    JSONObject apiRelyParam = new JSONObject(api.getApiRelyParam());
                    Integer relyTestId = 0;
                    JSONArray array = new JSONArray(apiCase.getRelyCaseId());
                    if (apiRelyParam.get("name").equals("0")) {
                        data.setApiParam(newRely.get(0).get(apiRelyParam.get("value")));

                    } else {
                        for (Object as : array) {
                            JSONObject a = new JSONObject(as.toString());
                            if (a.get("apiPath").toString().equals(apiRelyParam.get("name").toString())) {
                                relyTestId = Integer.parseInt(a.get("apiCaseId").toString());
                                break;
                            }
                        }
                        try {
                            JSONObject relyValue = new JSONObject(apiReportMapper.getRelyValue(reportId, relyTestId));
                            data.setApiParam(relyValue.get(apiRelyParam.get("value").toString()).toString());
                        } catch (Exception e) {

                        }
                    }

                } else {
                    data.setApiParam(apiCase.getApiHandleParam());
                }
                break;
        }
        //header
        data.setHeaderParam(this.OAddOs(api.getHeaderParamType(), api.getHeaderFiexdParam(), apiCase.getHeaderHandleParam(), api.getHeaderRelyParam(), newRely));
        //webform
        data.setWebformParam(this.OAddOs(api.getWebformParamType(), api.getWebformFiexdParam(), apiCase.getWebformHandleParam(), api.getWebformRelyParam(), newRely));
        //bodParam
        JSONArray bodyParamType = new JSONArray(api.getBodyParamType());
        if (bodyParamType.length() > 0) {
            JSONObject o = new JSONObject();
            o.put("name", "Content-Type");
            o.put("value", "application/json; charset=utf-8");
            JSONArray headers = data.getHeaderParam();
            data.setHeaderParam(headers.put(o));
            String bodyString = b.getJSONString(api.getBodyFiexdParam());
            data.setBodyParam(bodyString);
        }
        data.setAuthorization(tokenValue);

        /**
         * 登入接口的数据依赖
         */


        return data;
    }

    @Override
    public String getToken(ResponseData data) throws Throwable {
        if (data.getStatus().equals("200")) {
            JSONObject response = new JSONObject(data.getResponse());
            String cookie = data.getCookie();
            response.put("cookie", cookie);
            return response.toString();
        } else {
            throw new Throwable(data.getResponse());
        }

    }

    @Override
    public void doSmsCode(String smsHost, String smsUri, String newSmsParam, JSONArray newSmsHeader) {
        DoTestData doTestData = new DoTestData();
        doTestData.setHost(smsHost);
        doTestData.setBodyParam(newSmsParam);
        doTestData.setHeaderParam(newSmsHeader);
        doTestData.setApiPath(smsUri);
        doTestData.setApiMethod(2);
        doTestData.setAuthorization("null");
        ResponseData responseData = httpClientService.getResponse(doTestData);
        System.out.println(responseData.toString());
    }

    @Override
    public ResponseData doLogin(String host, String uri, JSONArray param, JSONArray header) {
        DoTestData doTestData = new DoTestData();
        doTestData.setHost(host);
        doTestData.setWebformParam(param);
        doTestData.setHeaderParam(header);
        doTestData.setApiPath(uri);
        doTestData.setApiMethod(2);
        doTestData.setAuthorization("null");
        ResponseData responseData = httpClientService.getResponse(doTestData);
        System.out.println(responseData.toString());
        return responseData;
    }

    /**
     * 忍法 数据合体之术
     */
    public JSONArray OAddO(JSONArray a1, JSONArray a2) {
        JSONArray headerParam = new JSONArray("[]");
        for (Object fixedParams : a1) {
            JSONObject fiexdParam = new JSONObject(fixedParams.toString());
            //自动参数name
            String fiexdName = fiexdParam.get("name").toString();
            Integer a1Size = a2.length();
            int i = 0;
            for (Object handleParams : a2) {
                i++;
                JSONObject handleParam = new JSONObject(handleParams.toString());
                String handleName = handleParam.get("name").toString();
                if (fiexdName.equals(handleName)) {
                    headerParam.put(handleParam);
                    break;
                }
                if (i == a1Size) {
                    headerParam.put(fiexdParam);
                }
            }
        }
        return headerParam;
    }

    /**
     * 道法 多重数据合体之术
     */
    public JSONArray OAddOs(String type, String fiexdParam, String handleParm, String relyParam, Map<Integer, Map<String, String>> newRely) {
        JSONArray param = new JSONArray("[]");
        JSONArray paramType = new JSONArray(type);
        if (paramType.length() >= 1) {
            param = new JSONArray(fiexdParam);
            if (paramType.length() > 1) {
                for (Object pt : paramType) {
                    switch (pt.toString()) {
                        case "2":
                            JSONArray array = new JSONArray(relyParam);
                            JSONArray relyParams = new JSONArray();
                            for (Object a : array) {
                                JSONObject object = new JSONObject();
                                JSONObject o = new JSONObject(a.toString());
                                String name = o.get("name").toString();
                                String valueName = o.get("value").toString();
                                Integer apiPath = Integer.parseInt(o.get("apiPath").toString());
                                if(apiPath==0){

                                }
                                String newValue = newRely.get(apiPath).get(valueName);
                                object.put("name", name);
                                object.put("value", newValue);
                                relyParams.put(object);
                            }
                            param = this.OAddO(param, relyParams);
                            break;
                        case "3":
                            JSONArray handleParam = new JSONArray(handleParm);
                            param = this.OAddO(param, handleParam);
                            break;
                    }
                }
            }
        }
        return param;
    }

    /**
     * 增加aplication
     */
    public JSONArray addAplication(JSONArray header, Integer deviceType) {
        String type = "";
        if (deviceType == 1) {
            type = "management";
        } else if (deviceType == 2) {
            type = "app_zhilun";
        } else {

        }
        JSONObject o = new JSONObject();
        o.put("name", "application");
        o.put("value", type);
        header.put(o);
        return header;
    }

    /**
     * 合成apiId 对应的 Map<name,value>
     */
    public Map<Integer, Map<String, String>> getRelyValue(Map<String, String> paths, ApiCase apiCase, String loginRely, long reportId) {
        JSONArray relyCaseList = new JSONArray(apiCase.getRelyCaseId());
        Map<String, String> loginRelyParam = new HashMap<>();
        Map<Integer, Map<String, String>> newRely = new HashMap<>();
        for (Object relyCase : relyCaseList) {
            JSONObject relyCaseValue = new JSONObject(relyCase.toString());
            Integer relyTestId = Integer.parseInt(relyCaseValue.get("apiCaseId").toString());
            if (relyTestId > 0) {
                String relyValues = apiReportMapper.getRelyValue(reportId, relyTestId);
                Gson gson = new Gson();
                Map<String, String> rely = new HashMap<>();
                rely = gson.fromJson(relyValues, rely.getClass());
                Integer apiId = apiCaseMapper.getApiIdByApiCaseId(relyTestId);
                newRely.put(apiId, rely);
            } else {
                for (String key : paths.keySet()) {
                    String value = paths.get(key);
                    String newValue = b.getValueFormJsonByPath(loginRely, value).toString();
                    loginRelyParam.put(key, newValue);
                }
                newRely.put(0, loginRelyParam);
            }
        }
        return newRely;
    }

}
