package com.service.apiTest.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.xdevapi.JsonArray;
import com.service.apiTest.controller.domin.ApiBaseRe;
import com.service.apiTest.dom.domin.ApiListParam;
import com.service.apiTest.dom.entity.Api;
import com.service.apiTest.dom.mapper.ApiCaseMapper;
import com.service.apiTest.dom.mapper.ApiMapper;
import com.service.apiTest.service.domian.*;
import com.service.apiTest.service.service.ApiService;
import com.service.utils.MyBaseChange;
import com.service.utils.MyVerification;
import com.service.utils.test.dom.project.Project;
import com.service.utils.test.dom.project.Project1;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApiServicelmpl implements ApiService {

    @Autowired
    private ApiMapper apiMapper;

    @Autowired
    private MyBaseChange b;

    @Autowired
    private MyVerification v;

    @Autowired
    private ApiCaseMapper apiCaseMapper;
    @Autowired
    private Project1 project1;

    @Override
    public Map<String, Object> getApiList(ApiListParam params) {
        Map<String, Object> map = new HashMap<>();
        List<Api> apiList = apiMapper.getApiList(params);
        JSONArray apiListDataList = new JSONArray();
        for (Api api : apiList) {
            ApiListData apiListData = new ApiListData();
            BeanUtils.copyProperties(api, apiListData);
            apiListDataList.add(apiListData);
            apiListData.setTestNum(apiCaseMapper.getCountApiCaseByApiId(api.getId()));
        }
        map.put("list", apiListDataList);
        map.put("count", apiMapper.getApiCount(params));
        return map;
    }

    @Override
    public Integer getApiCount() {
        return null;
    }

    @Override
    public ApiData getApi(int id) {
        Api api = apiMapper.getApiData(id);
        ApiData apiData = new ApiData();
        BeanUtils.copyProperties(api, apiData);
        if(api.getMore() == 1){
            apiData.setMore(true);
        }else {
            apiData.setMore(false);
        }
        apiData.setApiParamType(api.getApiParamType());
        /**
         * 存入apiParamType。。。
         */
        switch (api.getApiParamType()) {
            case "1":
                apiData.setApiFiexdParam(api.getApiFiexdParam());
                break;
            case "2":
                JSONObject apiRelyParam = b.StringToJson(api.getApiRelyParam());
                if (apiRelyParam.size() > 1) {
                    Integer apiId = Integer.parseInt(apiRelyParam.get("name").toString());
                    String apiPath = apiMapper.getPathById(apiId);
                    if(apiId == 0){
                        apiPath = "login";
                    }
                    apiData.setApiRelyParamName(apiPath);
                    apiData.setApiRelyParamValue(apiRelyParam.get("value").toString());
                }

        }


        /**
         * 存入headerParam....
         */

        JSONArray headerParamType = b.StringToArray(api.getHeaderParamType());
        apiData.setHeaderParamType(headerParamType);
        for (Object headerParam : headerParamType) {
            switch (headerParam.toString()) {
                case "1":
                    apiData.setHeaderFiexdParam(b.StringToAO(api.getHeaderFiexdParam()));
                    break;
                case "2":
                    apiData.setHeaderRelyParam(this.idToPath(api.getHeaderRelyParam()));
                    break;
                case "3":
                    apiData.setHeaderHandleParam(b.StringToAO(api.getHeaderHandleParam()));
                    break;
            }
        }

        /**
         * 存入webform....
         */
        JSONArray webformParamType = b.StringToArray(api.getWebformParamType());
        apiData.setWebformParamType(webformParamType);
        for (Object webformParam : webformParamType) {
            switch (webformParam.toString()) {
                case "1":
                    apiData.setWebformFiexdParam(b.StringToAO(api.getWebformFiexdParam()));
                    break;
                case "2":
                    apiData.setWebformRelyParam(this.idToPath(api.getWebformRelyParam()));
                    break;
                case "3":
                    apiData.setWebformHandleParam(b.StringToAO(api.getWebformHandleParam()));
                    break;
            }
        }

        /**
         * 存入body。。。
         */

        JSONArray bodyParamType = b.StringToArray(api.getBodyParamType());
        apiData.setBodyParamType(bodyParamType);
        for (Object bodyParam : bodyParamType) {
            switch (bodyParam.toString()) {
                case "1":
                    apiData.setBodyFiexdParam(api.getBodyFiexdParam());
                    break;
                case "2":
                    apiData.setBodyRelyParam(this.idToPath(api.getBodyRelyParam()));
                    break;
                case "3":
                    apiData.setBodyHandleParam(b.StringToAO(api.getBodyHandleParam()));
                    break;
            }
        }

        /**
         * 存入relyValue
         */

        if (StringUtils.isEmpty(api.getIsRely()) || (api.getIsRely() == 0)) {
            apiData.setIsRely(false);
        } else {
            apiData.setIsRely(true);
            apiData.setRelyValue(b.StringToArray(api.getRelyValue()));
        }
        return apiData;
    }

    @Override
    public void updateApi(ApiDataAU apiData,Integer userId) throws Throwable {
        Integer count = apiMapper.countApi(apiData.getDevice(), apiData.getApiPath(), apiData.getProjectId());
        if (count > 1) {
            throw new Throwable("接口存在重复类型");
        } else {
            Api api = new Api();
            this.au(apiData, api);
            api.setUpdateUserId(userId);
            apiMapper.updateApi(api);
        }

    }

    @Override
    public void addApi(ApiDataAU apiData) throws Throwable {
        Api api = new Api();
        Integer count = apiMapper.countApi(apiData.getDevice(), apiData.getApiPath(), api.getProjectId());
        if (count > 0) {
            throw new Throwable("存在相同类型的接口");
        } else {

            this.au(apiData, api);
            api.setCreateUserId(apiData.getUserId());
            apiMapper.addApi(api);
        }
    }

    @Override
    public ApiBaseRe delApi(ApiDataDel apiDataDel) {
        return null;
    }

    /**
     * 获取依赖选择项
     *
     * @param path
     * @return
     */
    @Override
    public JSONArray searchTest(String path, Integer project) {
        List<Api> apiList = apiMapper.getApiForPath(path, project);
        JSONArray apiArray = new JSONArray();
        for (Api api : apiList) {
            JSONObject object = new JSONObject();
            object.put("value", api.getId());
            object.put("text", api.getApiPath());
            apiArray.add(object);
        }
        if ("login".contains(path)) {
            JSONObject object = new JSONObject();
            object.put("value", 0);
            object.put("text", "login");
            apiArray.add(object);
        }
        return apiArray;
    }

    @Override
    public JSONArray searchRelyName(String path,Integer device,Integer projectId) {
        JSONArray array = new JSONArray();
        if (path.equals("0")) {
            Project project = new Project();
            switch (projectId){
                case 1:
                    project = project1;
            }
            Map<String,String> loginRely = project.getDevice().get(device-1).getLoginRely();
            for(String key : loginRely.keySet()){
                array.add(key);
            }


        } else {
            String relyName = apiMapper.searchRelyName(Integer.parseInt(path));
            JSONArray namePath = b.StringToArray(relyName);

            for (Object object : namePath) {
                JSONObject names = b.StringToJson(object.toString());
                array.add(names.get("name").toString());

            }
        }
        return array;
    }


    /**
     * 存入APIDATA 的数据
     *
     * @param apiData
     * @param api
     */

    public void au(ApiDataAU apiData, Api api) throws Throwable {

        BeanUtils.copyProperties(apiData, api);
        if(apiData.getMore()){
            api.setMore(1);
        }else {
            api.setMore(0);
        }
        /**
         * 存入apiParam.....
         */
        api.setApiParamType(apiData.getApiParamType());

        switch (apiData.getApiParamType()) {
            case "1":
                api.setApiFiexdParam(apiData.getApiFiexdParam());
                break;
            case "2":
                JSONObject apiRelyParam = new JSONObject();
                String name = apiData.getApiRelyParamName();
                if (name.startsWith("/")) {
                    name = apiMapper.getApiIdByPath(name).toString();
                }
                if(name.equals("login")){
                    name = "0";
                }
                apiRelyParam.put("name", name);
                apiRelyParam.put("value", apiData.getApiRelyParamValue());
                api.setApiRelyParam(apiRelyParam.toString());
                break;
        }


        /**
         * 存入headerparam....
         */
        api.setHeaderParamType(apiData.getHeaderParamType().toString());
        for (Object headerParam : apiData.getHeaderParamType()) {
            switch (headerParam.toString()) {
                case "1":
                    api.setHeaderFiexdParam(apiData.getHeaderFiexdParam().toString());
                    break;
                case "2":
                    api.setHeaderRelyParam(this.pathToId(apiData.getHeaderRelyParam().toString()));
                    break;
                case "3":
                    api.setHeaderHandleParam(apiData.getHeaderHandleParam().toString());
                    break;
            }
        }

        /**
         * 存入webform。。。。
         */

        api.setWebformParamType(apiData.getWebformParamType().toString());
        for (Object webformParam : apiData.getWebformParamType()) {
            switch (webformParam.toString()) {
                case "1":
                    api.setWebformFiexdParam(apiData.getWebformFiexdParam().toString());
                    break;
                case "2":
                    api.setWebformRelyParam(this.pathToId(apiData.getWebformRelyParam().toString()));
                    break;
                case "3":
                    api.setWebformHandleParam(apiData.getWebformHandleParam().toString());
                    break;

            }
        }

        /**
         * 存入body....
         */
        api.setBodyParamType(apiData.getBodyParamType().toString());
        for (Object bodyParam : apiData.getBodyParamType()) {
            switch (bodyParam.toString()) {
                case "1":
                    api.setBodyFiexdParam(apiData.getBodyFiexdParam());
                    break;
                case "2":

                    /**
                     * 验证路径值是否存在
                     */

                        v.havePathInJson(apiData.getBodyFiexdParam(),apiData.getBodyRelyParam());
                    api.setBodyRelyParam(this.pathToId(apiData.getBodyRelyParam().toString()));
                    break;
                case "3":
                    api.setBodyHandleParam(apiData.getBodyHandleParam().toString());
                    break;
            }
        }

        /**
         * 存入isrely
         */
        if (StringUtils.isEmpty(apiData.getIsRely()) || !apiData.getIsRely()) {
            api.setIsRely(0);
        } else {
            api.setIsRely(1);
            api.setRelyValue(apiData.getRelyValue().toString());
        }



    }

    /**
     * apiId 转 apiPath
     */
    public JSONArray idToPath(String s) {
        JSONArray result = new JSONArray();
        JSONArray params = b.StringToAO(s);
        for (Object param : params) {
            JSONObject object = b.StringToJson(
                    param.toString());
            JSONObject a = object;
            Integer apiId = Integer.parseInt(object.get("apiPath").toString());
            String path = apiMapper.getPathById(apiId);
            if(apiId==0){
                path = "login";
            }
            a.put("apiPath", path);
            result.add(a);

        }
        return result;
    }

    /**
     * apiPath 转 apiId
     */
    public String pathToId(String s) {
        JSONArray result = new JSONArray();
        JSONArray params = b.StringToAO(s);
        for (Object p : params) {
            JSONObject object = b.StringToJson(p.toString());
            String name = object.get("apiPath").toString();
            if (name.startsWith("/")) {
                object.put("apiPath", apiMapper.getApiIdByPath(name));
            }
            if(name.equals("login")){
                object.put("apiPath", "0");
            }
            result.add(object);
        }
        return result.toString();
    }
}
