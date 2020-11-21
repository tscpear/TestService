package com.service.apiTest.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.service.apiTest.controller.domin.PutToken;
import com.service.apiTest.dom.domin.ApiCaseForReport;
import com.service.apiTest.dom.entity.*;
import com.service.apiTest.dom.mapper.*;
import com.service.apiTest.service.domian.ApiReportCache;
import com.service.apiTest.service.domian.ApiReportList;
import com.service.apiTest.service.domian.DeviceAndType;
import com.service.apiTest.service.domian.OneReportData;
import com.service.apiTest.service.service.ApiReportService;
import com.service.utils.MyBaseChange;
import com.service.utils.MyObject.MyJsonPath;
import com.service.utils.MyVerification;
import com.service.utils.test.dom.DoTestData;
import com.service.utils.test.dom.ResponseData;
import com.service.utils.test.dom.project.Device;
import com.service.utils.test.dom.project.EData;
import com.service.utils.test.dom.project.Project;
import com.service.utils.test.dom.project.Project1;
import com.service.utils.test.method.DoApiService;
import com.service.utils.test.method.HttpClientService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.Data;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.jayway.jsonpath.JsonPath;
import org.springframework.util.StringUtils;

import javax.sound.midi.Soundbank;
import javax.xml.transform.Source;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ApiReportIlmpl implements ApiReportService {
    @Autowired
    private ApiCaseMapper apiCaseMapper;
    @Autowired
    private DoApiService doApiService;
    @Autowired
    private HttpClientService httpClientService;
    @Autowired
    private TokenMapper tokenMapper;
    @Autowired
    private ApiReportMainMapper apiReportMainMapper;
    @Autowired
    private ApiMapper apiMapper;
    @Autowired
    private ApiReportMapper apiReportMapper;
    @Autowired
    private MyBaseChange b;
    @Autowired
    private MyVerification v;
    @Autowired
    private Project1 project1;
    @Autowired
    private NewTokenMapper newTokenMapper;


    @Override
    public List<ApiReportList> getReportList(JSONArray testIdList) {
        List<ApiReportList> apiCaseList = apiCaseMapper.getApiCaseListForReport(testIdList);


        return apiCaseList;
    }

    @Override
    public ApiReportCache doTest(JSONArray testId,
                       Integer environment,
                       long reportId,
                       List<String> accountValue,
                       Integer projectId,
                       Integer b,
                       ApiReportCache apiReportCache) {
        /**
         * 已执行的数据缓存
         */
        Map<String, String> newTokenList = new HashMap<>();
        Map<String, String> newDataList = new HashMap<>();
        for (String accounts : accountValue) {
            NewToken newToken = new NewToken();
            Integer deviceId = Integer.parseInt(accounts.split("\\.")[0]);
            Integer deviceType = Integer.parseInt(accounts.split("\\.")[1]);
            Integer acountId = Integer.parseInt(accounts.split("\\.")[2]);
            newToken.setEnvironment(environment);
            newToken.setDeviceId(deviceId);
            newToken.setDeviceType(deviceType);
            newToken.setProjectId(projectId);
            newToken.setAccountId(acountId);
            String newName = deviceId + "." + deviceType;
            String tokenValue = newTokenMapper.getToken(newToken);
            if (StringUtils.isEmpty(tokenValue)) {
                tokenValue = newTokenMapper.getCookie(newToken);
            }
            String data = newTokenMapper.getData(newToken);
            newTokenList.put(newName, tokenValue);
            newDataList.put(newName, data);
        }
        /**
         * testId 自动排序
         * @1获取testId isdepend relyCaseId
         *
         */
        for (Object ids : testId) {
            Integer id = Integer.parseInt(ids.toString());
            DoTestData doTestData = doApiService.getTestData(environment, id, newTokenList, newDataList, reportId, accountValue, projectId, apiReportCache);
            ResponseData responseData = httpClientService.getResponse(doTestData);
            apiReportCache = addReport(responseData, reportId, id, b, apiReportCache);
        }
        /**
         * 存入成功率
         */
        Integer all = testId.size();
        Integer success = apiReportMapper.countOfSuccess(reportId);
        String value = success * 100 / all + "%";
        apiReportMainMapper.updateSuccess(value, reportId);



        return apiReportCache;
    }

    @Override
    public ApiReportCache doTest(JSONArray testId, Integer environment, long reportId, List<String> accountValue, Integer projectId, Integer b) {
        ApiReportCache apiReportCache = new ApiReportCache();
        apiReportCache.setReportId(reportId);
        return this.doTest(testId, environment, reportId, accountValue, projectId, b, apiReportCache);



    }


    @Override
    public void addReportMain(long reportId) {
        apiReportMainMapper.add(reportId);
    }

    @Override
    public ApiReportCache addReport(ResponseData data, long reportId, Integer testId, Integer a, ApiReportCache apiReportCache) {
        List<Integer> testIdDoneList = apiReportCache.getTestIdDoneList();
        testIdDoneList.add(testId);
        apiReportCache.setTestIdDoneList(testIdDoneList);

        ApiReport report = new ApiReport();
        report.setReportId(reportId);
        report.setTestId(testId);
        BeanUtils.copyProperties(data, report);


        ApiCase apiCase = apiCaseMapper.getApiCaseData(testId);
        Api api = apiMapper.getApiData(apiCase.getApiId());


        report.setExpectStatus(apiCase.getStatusAssertion());
        report.setActStatus(data.getStatus());
        report.setDeviceType(apiCase.getDeviceType());

        /**
         * 返回状态值断言
         */
        if (apiCase.getStatusAssertion().equals(data.getStatus())) {
            report.setResultMain(0);
            report.setResultStatus(1);
        } else {
            report.setResultStatus(0);
            report.setResultMain(1);
        }
        /**
         * 其他断言
         */
        if (report.getResultStatus() == 1) {
            JSONArray otherAssertionType = b.StringToArray(apiCase.getOtherAssertionType());
            for (Object o : otherAssertionType) {
                if (report.getResultMain() != 0) {
                    break;
                }
                switch (Integer.parseInt(o.toString())) {
                    case 1:
                        if (v.isSameJSONObject(api.getResponseBase(), report.getResponse())) {
                            report.setResponseBaseExpectResult(1);
                        } else {
                            report.setResponseBaseExpectResult(0);
                            report.setResultMain(2);
                        }
                        break;
                    case 2:
                        JSONArray responseValueExpect = b.StringToAO(apiCase.getResponseValueExpect());
                        JSONArray responseValueExpectRelust = new JSONArray();
                        for (Object expect : responseValueExpect) {
                            JSONObject expectValueOld = b.StringToJson(expect.toString());
                            JSONObject expectValueNew = new JSONObject();
                            String path = expectValueOld.get("name").toString();
                            String expectValue = expectValueOld.get("value").toString();
                            Object actValue = b.getValueFormJsonByPath(report.getResponse(), path);
                            expectValueNew.put("path", path);
                            expectValueNew.put("expectValue", expectValue);
                            expectValueNew.put("actValue", actValue);
                            responseValueExpectRelust.add(expectValueNew);
//                            if(actValue.getType() ==1){
//                                JSONArray newExpectValue = b.StringToArray(expectValue);
//                                JSONArray newActValue = b.StringToArray(actValue.getValue().toString());
//                                if(newExpectValue == newActValue){
//                                    report.setResultMain(3);
//                                }
//                            }else {
//                                if (!actValue.getValue().equals(expectValue)) {
//                                    report.setResultMain(3);
//                                }
//                            }

                            if (v.isJSONArray(actValue.toString())) {
                                JSONArray newExpectValue = b.StringToArray(expectValue);
                                JSONArray newActValue = b.StringToArray(actValue.toString());
                                if (newExpectValue == newActValue) {
                                    report.setResultMain(3);
                                }
                            } else {
                                if (!actValue.equals(expectValue)) {
                                    report.setResultMain(3);
                                }
                            }
                        }
                        report.setResponseValueExpectResult(responseValueExpectRelust.toString());
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                }
            }
        }


        /**
         * 存储依赖值
         */
        if (report.getResultMain() == 0) {
            Integer isRely = api.getIsRely();
            if (isRely == 1) {
                JSONArray relyValueLook = new JSONArray();
                JSONObject value = new JSONObject();
                JSONArray relyParams = b.StringToAO(api.getRelyValue());
                Object values = null;
                for (Object relyParam : relyParams) {
                    JSONObject map = new JSONObject();
                    JSONObject param = b.StringToJson(relyParam.toString());
                    String path = param.get("value").toString();
                    try {
                        values = JsonPath.read(data.getResponse(), path);
                        value.put(param.get("name").toString(), values);
                    } catch (Exception e) {
                        report.setResultMain(3);
                    }
                    map.put("name", param.get("name").toString());
                    map.put("path", path);
                    map.put("value", values);
                    relyValueLook.add(map);
                }
                report.setRelyValue(value.toString());
                report.setRelyValueLook(relyValueLook.toString());

            }
        }
        if (apiReportMapper.findReportIdAndTestId(report) > 0 && a == 1) {
            apiReportMapper.updateByReportId(report);
        } else {
            apiReportMapper.putData(report);
        }

        Map<Integer, List<ApiReport>> apiReportList = apiReportCache.getApiReportList();
        List<ApiReport> apiReportList1 = new ArrayList<>();
        if (apiReportList.containsKey(testId)) {
            apiReportList1 = apiReportList.get(testId);
        }
        apiReportList1.add(report);
        apiReportList.put(testId, apiReportList1);
        apiReportCache.setApiReportList(apiReportList);
        return apiReportCache;
    }

    @Override
    public List<ApiReport> getReportDataList(long reportId) {
        return apiReportMapper.getList(reportId);
    }

    @Override
    public OneReportData getOneReport(Integer id) {
        OneReportData oneReportData = new OneReportData();
        ApiReport apiReport = apiReportMapper.getData(id);
        BeanUtils.copyProperties(apiReport, oneReportData);
        oneReportData.setHeaderParam(b.StringToAO(apiReport.getHeaderParam()));
        oneReportData.setWebformParam(b.StringToAO(apiReport.getWebformParam()));
        oneReportData.setResponseValueExpectResult(b.StringToAO(apiReport.getResponseValueExpectResult()));
        oneReportData.setRelyValueLook(b.StringToAO(apiReport.getRelyValueLook()));
        try {
            oneReportData.setBodyParam(b.StringToAO(apiReport.getBodyParam()));
        } catch (Exception e) {
            oneReportData.setBodyParam(b.StringToJson(apiReport.getBodyParam()));
        }
        if (v.isJsonObject(apiReport.getResponse())) {
            oneReportData.setResponse(b.StringToJson(apiReport.getResponse()));
        } else {
            JSONObject o = new JSONObject();
            o.put("非json返回值", apiReport.getResponse());
            oneReportData.setResponse(o);
        }

        return oneReportData;
    }

    @Override
    public List<DeviceAndType> getDeviceTypeAndAccountList(JSONArray testList, Integer projectId, Integer environment) {
        /**
         * 增加前置用例的账户体系
         */
        List<DeviceAndType> deviceAndTypeList = apiCaseMapper.getDeviceTypeList(testList);
        Project project = new Project();
        if (projectId == 1) {
            project = project1;
        }
        List<DeviceAndType> newDeviceAndTypeList = new ArrayList<>();
        for (DeviceAndType deviceAndType : deviceAndTypeList) {
            deviceAndType.setDeviceName(project.getDevice().get(deviceAndType.getDevice() - 1).getName());
            deviceAndType.setDeviceTypeName(project.getDevice().get(deviceAndType.getDevice() - 1).getDeviceTypeList().get(deviceAndType.getDeviceType() - 1));
            deviceAndType.setAccountList(project.getDevice().get(deviceAndType.getDevice() - 1).getEnvironment().get(environment - 1).getAccount().get(deviceAndType.getDeviceType() - 1));
            newDeviceAndTypeList.add(deviceAndType);
        }
        return newDeviceAndTypeList;
    }

    @Override
    public String accountLogin(PutToken putToken, Integer projectId) {
        String msg = "";
        ResponseData responseData = new ResponseData();
        Project project = new Project();
        switch (projectId) {
            case 1:
                BeanUtils.copyProperties(project1, project);
                break;
        }

        List<String> wxCode = putToken.getWxCode();
        Integer environment = putToken.getEnvironment();
        List<String> accountValueList = putToken.getAccountValue();


        for (String values : accountValueList) {
            Integer deviceId = Integer.parseInt(values.split("\\.")[0]);
            Integer deviceTypeId = Integer.parseInt(values.split("\\.")[1]);
            Integer accountId = Integer.parseInt(values.split("\\.")[2]);
            Device device = project.getDevice().get(deviceId - 1);
            Integer loginType = device.getLoginType();
            EData eData = device.getEnvironment().get(environment - 1);
            String account = eData.getAccount().get(deviceTypeId - 1).get(accountId - 1);
            String loginHost = eData.getLoginHost();
            String loginUri = device.getLoginUri();
            String tokenPath = device.getTokenPath();
            Map<String, Object> loginHeader = eData.getLoginHeader();
            org.json.JSONArray newLoginHeader = new org.json.JSONArray();
            if (!StringUtils.isEmpty(loginHeader)) {
                newLoginHeader = b.mapToArray(loginHeader);
            }
            if (loginType == 0) {

                /**
                 * 获取验证码的接口
                 */
                Map<String, Object> smsParam = device.getSmsParam();
                String deviceName = device.getName();
                String smsUri = device.getSmsUri();
                Map<String, Object> smsHeader = eData.getSmsHeader();
                org.json.JSONArray newSmsHeader = new org.json.JSONArray();
                if (!StringUtils.isEmpty(smsHeader)) {
                    newSmsHeader = b.mapToArray(smsHeader);
                }
                smsParam = b.replaceValueOfMap(smsParam, "username", account);
                if (deviceName.contains("小程序")) {
                    try {
                        String wxCodeValue = wxCode.get(0);
                        wxCode.remove(wxCodeValue);
                        smsParam = b.replaceValueOfMap(smsParam, "wxCode", wxCodeValue);
                    } catch (Exception e) {
                        return "wxCode不足";
                    }
                }
                String newSmsParam = JSONObject.toJSONString(smsParam);

                doApiService.doSmsCode(loginHost, smsUri, newSmsParam, newSmsHeader);

                /**
                 * 使用验证码登入的接口
                 */
                org.json.JSONArray newSmsLoginParam = new org.json.JSONArray();
                Map<String, Object> smsLoginParam = device.getSmsLoginParam();
                if (deviceName.contains("小程序")) {
                    try {
                        String wxCodeValue = wxCode.get(0);
                        wxCode.remove(wxCodeValue);
                        smsLoginParam = b.replaceValueOfMap(smsLoginParam, "wxCode", wxCodeValue);
                    } catch (Exception e) {
                        return "wxCode不足";
                    }
                }
                smsLoginParam = b.replaceValueOfMap(smsLoginParam, "username", account);
                if (!StringUtils.isEmpty(smsLoginParam)) {
                    newSmsLoginParam = b.mapToArray(smsLoginParam);

                }

                responseData = doApiService.doLogin(loginHost, loginUri, newSmsLoginParam, newLoginHeader, "null");
            } else {
                /**
                 * 直接密码账号登入
                 *
                 */
                String authorization = "null";
                if (loginType == 3) {
                    /**
                     * 先获取原始cookie
                     */

                    String cookieUri = device.getSmsUri();
                    String doorCookie = doApiService.getDoorCookie(loginHost, cookieUri);
                    authorization = doorCookie;
                }
                Map<String, Object> loginParam = device.getLoginParam();
                loginParam = b.replaceValueOfMap(loginParam, "username", account);
                org.json.JSONArray newLoginParam = new org.json.JSONArray();
                if (!StringUtils.isEmpty(loginParam)) {
                    newLoginParam = b.mapToArray(loginParam);
                }
                responseData = doApiService.doLogin(loginHost, loginUri, newLoginParam, newLoginHeader, authorization);

            }
            String sucess = this.depositToken(responseData, projectId, deviceId, environment, deviceTypeId, accountId, tokenPath);
            if (!sucess.equals("success")) {
                msg = "账号  " + account + "登入失败：" + sucess + "!";
            }

        }
        return msg;
    }

    public void login(String loginHost) {

    }

    /**
     * 存入token
     */
    public String depositToken(ResponseData data, Integer projectId, Integer device, Integer environment, Integer deviceType, Integer accountId, String tokenPath) {
        NewToken newToken = new NewToken();
        newToken.setAccountId(accountId);
        newToken.setDeviceId(device);
        newToken.setProjectId(projectId);
        newToken.setDeviceType(deviceType);
        newToken.setEnvironment(environment);

        if (data.getStatus().equals("200")) {
            newToken.setData(data.getResponse());
            try {
                String token = b.getValueFormJsonByPath(data.getResponse(), tokenPath).toString();
                token = "bearer " + token;
                newToken.setToken(token);
            } catch (Exception e) {
                newToken.setCookie(data.getCookie());
            }
        } else {
            return data.getResponse();
        }

        /**
         * 查看账号是否在数据库是否存在
         */
        Integer hasAccount = newTokenMapper.countOfAccount(newToken);
        if (hasAccount > 0) {
            /**
             * 更新数据
             */
            newTokenMapper.updateToken(newToken);

        } else {
            /**
             * 插入新数据
             */
            newTokenMapper.insertAccount(newToken);
        }
        return "success";
    }
}
