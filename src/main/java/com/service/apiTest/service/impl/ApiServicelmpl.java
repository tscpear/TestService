package com.service.apiTest.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.service.apiTest.controller.domin.ApiBaseRe;
import com.service.apiTest.dom.domin.ApiListParam;
import com.service.apiTest.dom.entity.Api;
import com.service.apiTest.dom.mapper.ApiCaseMapper;
import com.service.apiTest.dom.mapper.ApiMapper;
import com.service.apiTest.service.domian.*;
import com.service.apiTest.service.service.ApiService;
import com.service.utils.MyBaseChange;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ApiServicelmpl implements ApiService {

    @Autowired
    private ApiMapper apiMapper;

    @Autowired
    private MyBaseChange b;

    @Autowired
    private ApiCaseMapper apiCaseMapper;

    @Override
    public JSONArray getApiList(ApiListParam params) {
        List<Api> apiList = apiMapper.getApiList(params);
        JSONArray apiListDataList = new JSONArray();
        for (Api api : apiList) {
            ApiListData apiListData = new ApiListData();
            BeanUtils.copyProperties(api, apiListData);
            apiListDataList.add(apiListData);
            apiListData.setTestNum(apiCaseMapper.getCountApiCaseByApiId(api.getId()));
        }
        return apiListDataList;
    }

    @Override
    public Integer getApiCount() {
        return null;
    }

    @Override
    public ApiData getApi(int id) {
        ApiBaseRe baseRe = new ApiBaseRe();
        Api api = apiMapper.getApiData(id);
        ApiData apiData = new ApiData();
        BeanUtils.copyProperties(api, apiData);
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
                    apiData.setApiRelyParamName(apiRelyParam.get("name").toString());
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
                    apiData.setHeaderRelyParam(b.StringToAO(api.getHeaderRelyParam()));
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
                    apiData.setWebformRelyParam(b.StringToAO(api.getWebformRelyParam()));
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
                    apiData.setBodyRelyParam(b.StringToAO(api.getBodyRelyParam()));
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
    public void updateApi(ApiDataAU apiData) throws Throwable {
        Integer count = apiMapper.countApi(apiData.getDevice(), apiData.getApiPath());
        if (count > 1) {
            throw new Throwable("接口存在重复类型");
        } else {
            Api api = new Api();
            this.au(apiData, api);
            api.setUpdateUserId(apiData.getUserId());
            apiMapper.updateApi(api);
        }

    }

    @Override
    public void addApi(ApiDataAU apiData) throws Throwable {
        Api api = new Api();
        Integer count = apiMapper.countApi(apiData.getDevice(), apiData.getApiPath());
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
     * 存入APIDATA 的数据
     *
     * @param apiData
     * @param api
     */

    public void au(ApiDataAU apiData, Api api) {

        BeanUtils.copyProperties(apiData, api);

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
                apiRelyParam.put("name", apiData.getApiRelyParamName());
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
                    api.setHeaderRelyParam(apiData.getHeaderRelyParam().toString());
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
                    api.setWebformRelyParam(apiData.getWebformRelyParam().toString());
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
                    api.setBodyRelyParam(apiData.getBodyRelyParam().toString());
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
}
