package com.service.apiTest.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.service.apiTest.controller.domin.ApiCaseData;
import com.service.apiTest.dom.domin.ApiCaseListParam;
import com.service.apiTest.dom.domin.NewApiListCaseParam;
import com.service.apiTest.dom.entity.ApiCase;
import com.service.apiTest.dom.mapper.ApiCaseMapper;
import com.service.apiTest.dom.mapper.ApiMapper;
import com.service.apiTest.dom.mapper.DeviceTypeMapper;
import com.service.apiTest.service.domian.ApiCaseList;
import com.service.apiTest.service.domian.ApiCaseUpdateData;
import com.service.apiTest.service.domian.ApiData;
import com.service.apiTest.service.domian.ApiForCase;
import com.service.apiTest.service.service.ApiCaseService;
import com.service.apiTest.service.service.ApiService;
import com.service.utils.MyBaseChange;
import com.service.utils.test.dom.DriverAppHost;
import com.service.utils.test.dom.MyHost;
import com.service.utils.test.dom.StoreHost;
import com.service.utils.test.dom.project.Project;
import com.service.utils.test.dom.project.Project1;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApiCaseServicelmpl implements ApiCaseService {


    @Autowired
    private ApiService apiService;

    @Autowired
    private ApiMapper apiMapper;

    @Autowired
    private MyBaseChange b;

    @Autowired
    private ApiCaseMapper apiCaseMapper;

    @Autowired
    private DeviceTypeMapper deviceTypeMapper;
    @Autowired
    private StoreHost storeHost;
    @Autowired
    private DriverAppHost driverAppHost;
    @Autowired
    private Project1 project1;


    @Override
    public ApiForCase getApiDataForAddCase(Integer apiId, Integer userId, Integer projectId) {
        ApiData apiData = apiService.getApi(apiId);
        ApiForCase apiForCase = new ApiForCase();
        apiForCase.setDeviceTypeList(this.getDeviceType(projectId, apiData.getDevice()));
        BeanUtils.copyProperties(apiData, apiForCase);
        apiForCase.setApiId(apiData.getId());


        /**
         * 获取依赖属性
         */
        String headerRelyParam = apiData.getHeaderRelyParam().toString();
        String webformRelyParam = apiData.getWebformRelyParam().toString();
        String bodyRelyParam = apiData.getBodyRelyParam().toString();

        /**
         * 判断是否存在依赖
         */

        JSONArray relyTestIdList = new JSONArray();
        if (apiData.getApiParamType().equals("2")) {
            apiForCase.setHasRely(true);
            apiForCase.setIsDepend(true);
            relyTestIdList.add(this.getSelectRelyValue(apiData.getApiRelyParamName()));
        }

        for (Object type : apiData.getHeaderParamType()) {
            if (type.toString().equals("2")) {
                apiForCase.setHasRely(true);
                this.addrely(headerRelyParam, relyTestIdList);
                apiForCase.setHeaderRelyToHandle(this.relyTHandle(headerRelyParam));
                break;
            }
        }
        for (Object type : apiData.getWebformParamType()) {
            if (type.toString().equals("2")) {
                apiForCase.setHasRely(true);
                this.addrely(webformRelyParam, relyTestIdList);
                apiForCase.setWebformRelyToHandle(this.relyTHandle(webformRelyParam));
                break;
            }
        }
        for (Object type : apiData.getBodyParamType()   ) {
            if (type.toString().equals("2")) {
                apiForCase.setHasRely(true);
                this.addrely(bodyRelyParam, relyTestIdList);
                apiForCase.setBodyRelyToHandle(this.relyTHandle(bodyRelyParam));
                break;
            }
        }

        apiForCase.setSelectRelyCase(b.removeSame(relyTestIdList));


        return apiForCase;
    }

    @Override
    public void addApiCaseData(ApiCaseData apiCaseData, Integer projectId,Integer userId) {
        ApiCase apiCase = this.updateAndAdd(apiCaseData, projectId);
        apiCase.setCreateUserId(userId);
        apiCaseMapper.addApiCaseData(apiCase);
    }

    @Override
    public Integer getCountApiCase(Integer apiId) {
        return null;
    }

    @Override
    public Integer getCountApiCase() {
        return null;
    }

    @Override
    public Map<String, Object> getApiCaseList(ApiCaseListParam apiCaseListParam) {
        Map<String, Object> map = new HashMap<>();
        NewApiListCaseParam param = new NewApiListCaseParam();
        BeanUtils.copyProperties(apiCaseListParam, param);
        List<Integer> caseIdList;
        Integer device = apiCaseListParam.getDevice();
        String apiPath = apiCaseListParam.getApiPath();
        Integer projectId = apiCaseListParam.getProjectId();
        List<ApiCase> apiCaseLists;
        if (StringUtils.isEmpty(apiCaseListParam.getApiId())) {
            if (StringUtils.isEmpty(apiPath) && StringUtils.isEmpty(device)) {
                caseIdList = null;
            } else {
                caseIdList = apiMapper.getApiIdForCaseList(device, apiPath, projectId);
            }
            param.setCaseIdList(caseIdList);
        }
        apiCaseLists = apiCaseMapper.getApiCaseList(param);
        Integer count = apiCaseMapper.getCountApiCase(param);
        JSONArray caseList = new JSONArray();
        for (ApiCase apiCase : apiCaseLists) {
            ApiCaseList apiCaseList = new ApiCaseList();
            BeanUtils.copyProperties(apiCase, apiCaseList);
            Integer apiId = apiCase.getApiId();
            ApiData apiData = apiService.getApi(apiId);
            apiCaseList.setApiPath(apiData.getApiPath());
            apiCaseList.setDevice(apiData.getDevice());
            caseList.add(apiCaseList);
        }
        map.put("list", caseList);
        map.put("count", count);

        return map;
    }

    /**
     * 编辑页面
     *
     * @param id
     * @param userId
     * @return
     */
    @Override
    public ApiCaseUpdateData getApiCaseData(int id, Integer userId, Integer projectId) {
        ApiCaseUpdateData apiCaseUpdateData = new ApiCaseUpdateData();
        ApiCase apiCase = apiCaseMapper.getApiCaseData(id);
        ApiForCase apiForCase = this.getApiDataForAddCase(apiCase.getApiId(), userId, projectId);
        BeanUtils.copyProperties(apiForCase, apiCaseUpdateData);
        BeanUtils.copyProperties(apiCase, apiCaseUpdateData);
        apiCaseUpdateData.setWebformHandleParam(b.StringToAO(apiCase.getWebformHandleParam()));
        apiCaseUpdateData.setPreCase(b.StringToArray(apiCase.getPreCase()));
        JSONArray otherAssertionType = b.StringToArray(apiCase.getOtherAssertionType());
        apiCaseUpdateData.setOtherAssertionType(otherAssertionType);
        Integer deviceId = apiForCase.getDevice();
        for (Object o : otherAssertionType) {
            switch (o.toString()) {
                case "2":
                    apiCaseUpdateData.setResponseValueExpect(b.StringToAO(apiCase.getResponseValueExpect()));
                    break;
            }
        }


        /**
         * 依赖转换
         */
        if (apiCaseUpdateData.getHasRely()) {
            if (apiCase.getIsDepend() == 0) {
                apiCaseUpdateData.setIsDepend(false);
                apiCaseUpdateData.setHeaderRelyToHandle(b.StringToAO(apiCase.getHeaderRelyToHandle()));
                apiCaseUpdateData.setWebformRelyToHandle(b.StringToAO(apiCase.getWebformRelyToHandle()));
                apiCaseUpdateData.setBodyRelyToHandle(b.StringToAO(apiCase.getBodyRelyToHandle()));
            } else {
                apiCaseUpdateData.setIsDepend(true);
                /**
                 * 来自接口
                 */
                JSONArray selectRelyCase = b.StringToAO(apiForCase.getSelectRelyCase().toString());
                /**
                 * 来自用例
                 */

                JSONArray relyCaseId = b.StringToAO(apiCase.getRelyCaseId());

                JSONArray result = new JSONArray();
                for (Object relys : selectRelyCase) {

                    JSONObject rely = b.StringToJson(relys.toString());
                    for (Object relyIds : relyCaseId) {
                        JSONObject relyId = b.StringToJson(relyIds.toString());
                        String path = relyId.get("apiPath").toString();
                        if (!path.startsWith("/") && !path.equals("login")) {
                            path = apiMapper.getPathById(Integer.parseInt(path));
                        }
                        if (rely.get("apiPath").toString().equals(path)) {
                            rely.put("apiCaseMark", relyId.get("apiCaseId").toString());
                            result.add(rely);
                            break;
                        }
                    }
                }
                apiCaseUpdateData.setSelectRelyCase(result);

            }
        }


        return apiCaseUpdateData;
    }

    @Override
    public void updateApiCaseData(ApiCaseData apiCaseData, Integer projectId,Integer userId) {
        ApiCase apiCase = this.updateAndAdd(apiCaseData, projectId);
        apiCase.setUpdateUserId(userId);
        apiCaseMapper.updateApiCaseData(apiCase);
    }

    @Override
    public void delApiCase(Integer id, Integer userId) throws Throwable {
        String apiCaseLv = apiCaseMapper.findApiCaseOfLv(id);
        String apiCaseType = apiCaseMapper.findApiCaseOfType(id);
        if (apiCaseLv.equals("3")) {
            throw new Throwable("该等级的用例不可删除");
        }
        if (apiCaseType.equals("3")) {
            throw new Throwable("该类型的用例不可删除");
        }
        apiCaseMapper.delApiCase(id, userId);
    }


    public ApiCase updateAndAdd(ApiCaseData apiCaseData, Integer projectId) {
        ApiCase apiCase = new ApiCase();
        BeanUtils.copyProperties(apiCaseData, apiCase);

        apiCase.setApiHandleParam(apiCaseData.getApiHandleParam());
        apiCase.setHeaderHandleParam(apiCaseData.getHeaderHandleParam().toString());
        apiCase.setWebformHandleParam(apiCaseData.getWebformHandleParam().toString());
        apiCase.setBodyHandleParam(apiCaseData.getBodyHandleParam().toString());
        apiCase.setHeaderRelyToHandle(apiCaseData.getHeaderRelyToHandle().toString());
        apiCase.setWebformRelyToHandle(apiCaseData.getWebformRelyToHandle().toString());
        apiCase.setBodyRelyToHandle(apiCaseData.getBodyRelyToHandle().toString());
        apiCase.setOtherAssertionType(apiCaseData.getOtherAssertionType().toString());
        apiCase.setResponseValueExpect(apiCaseData.getResponseValueExpect().toString());
        apiCase.setPreCase(apiCaseData.getPreCase().toJSONString());
        apiCase.setProjectId(projectId);

        JSONArray relySelect = b.StringToAO(apiCaseData.getSelectRelyCase().toString());
        JSONArray array = new JSONArray();
        if (apiCaseData.getIsDepend()) {
            apiCase.setIsDepend(1);
            for (Object select : relySelect) {
                JSONObject object = b.StringToJson(select.toString());
                JSONObject o = new JSONObject();
                String path = object.get("apiPath").toString();
                if (path.startsWith("/")) {
                    path = apiMapper.getApiIdByPath(path).toString();
                }
                o.put("apiPath", path);
                o.put("apiCaseId", object.get("apiCaseMark").toString());
                array.add(o);
            }
            apiCase.setRelyCaseId(array.toString());
        } else {
            apiCase.setIsDepend(0);
        }

        return apiCase;

    }

    /**
     * 依赖依赖接口对应的用例id
     *
     * @param s
     * @return
     */
    public JSONObject getSelectRelyValue(String name) {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        if (name.equals("login")) {
            JSONObject a = new JSONObject();
            a.put("apiCaseId", "0");
            a.put("apiCaseMark", "登入");
            object.put("apiPath", "login");
            array.add(a);
        } else {
            String ids = apiMapper.getApiIdByPath(name).toString();

            Integer id = Integer.parseInt(ids);
            String apiPath = apiMapper.getPathById(id);
            object.put("apiPath", apiPath);
            List<ApiCase> relyTest = apiCaseMapper.getTestForRely(id);

            for (ApiCase relys : relyTest) {
                JSONObject a = new JSONObject();
                a.put("apiCaseId", relys.getId().toString());
                a.put("apiCaseMark", relys.getApiCaseMark());
                array.add(a);
            }
        }
        object.put("relyCase", array);
        return object;
    }


    public void addrely(String s, JSONArray relyTestIdList) {
        JSONArray relyValue = b.StringToAO(s);
        for (Object rely : relyValue) {
            JSONObject value = b.StringToJson(rely.toString());
            relyTestIdList.add(this.getSelectRelyValue(value.get("apiPath").toString()));

        }
    }

    public JSONArray relyTHandle(String s) {
        JSONArray array = new JSONArray();
        JSONArray relys = b.StringToAO(s);
        for (Object rely : relys) {
            JSONObject relyO = b.StringToJson(rely.toString());
            JSONObject o = new JSONObject();
            o.put("name", relyO.get("name").toString());
            o.put("value", "");
            array.add(o);
        }

        return array;
    }

    /**
     * 账户类型
     *
     * @param device
     * @param e
     * @return
     */
    public List<String> getDeviceType(Integer projectId, Integer device) {

        Project project = new Project();
        switch (projectId) {
            case 1:
                project = project1;
                break;

        }
        return project.getDevice().get(device - 1).getDeviceTypeList();

    }
}
