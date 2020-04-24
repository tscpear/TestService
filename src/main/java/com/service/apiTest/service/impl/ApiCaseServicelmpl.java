package com.service.apiTest.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.service.apiTest.dom.domin.ApiCaseListParam;
import com.service.apiTest.dom.entity.ApiCase;
import com.service.apiTest.dom.mapper.ApiCaseMapper;
import com.service.apiTest.dom.mapper.ApiMapper;
import com.service.apiTest.controller.domin.ApiCaseData;
import com.service.apiTest.service.domian.ApiCaseList;
import com.service.apiTest.service.domian.ApiCaseUpdateData;
import com.service.apiTest.service.domian.ApiData;
import com.service.apiTest.service.domian.ApiForCase;
import com.service.apiTest.service.service.ApiCaseService;
import com.service.apiTest.service.service.ApiService;
import com.service.utils.MyBaseChange;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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


    @Override
    public ApiForCase getApiDataForAddCase(Integer apiId) {
        ApiData apiData = apiService.getApi(apiId);
        ApiForCase apiForCase = new ApiForCase();
        BeanUtils.copyProperties(apiData, apiForCase);
        apiForCase.setApiId(apiData.getId());


        /**
         * 判断是否存在依赖
         */
        if (apiData.getApiParamType() == "2") {
            apiForCase.setIsDepend(true);
            return apiForCase;
        }
        for (Object type : apiData.getHeaderParamType()) {
            if (type.toString().equals("2")) {
                apiForCase.setIsDepend(true);
                return apiForCase;
            }
        }
        for (Object type : apiData.getWebformParamType()) {
            if (type.toString().equals("2")) {
                apiForCase.setIsDepend(true);
                return apiForCase;
            }
        }
        for (Object type : apiData.getBodyParamType()) {
            if (type.toString().equals("2")) {
                apiForCase.setIsDepend(true);
                return apiForCase;
            }
        }
        return apiForCase;
    }

    @Override
    public void addApiCaseData(ApiCaseData apiCaseData) {
        ApiCase apiCase = new ApiCase();
        BeanUtils.copyProperties(apiCaseData, apiCase);
        apiCase.setApiHandleParam(apiCaseData.getApiHandleParam());
        apiCase.setHeaderHandleParam(apiCaseData.getHeaderHandleParam().toString());
        apiCase.setWebformHandleParam(apiCaseData.getWebformHandleParam().toString());
        apiCase.setBodyHandleParam(apiCaseData.getBodyHandleParam().toString());
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
    public JSONArray getApiCaseList(ApiCaseListParam apiCaseListParam) {
        List<ApiCase> apiCaseLists = apiCaseMapper.getApiCaseList(apiCaseListParam);
        JSONArray caseList = new JSONArray();
        for (ApiCase apiCase : apiCaseLists) {
            ApiCaseList apiCaseList = new ApiCaseList();
            BeanUtils.copyProperties(apiCase,apiCaseList);





            caseList.add(apiCaseList);
        }
        return caseList;
    }

    @Override
    public ApiCaseUpdateData getApiCaseData(int id) {
        ApiCaseUpdateData apiCaseUpdateData = new ApiCaseUpdateData();
        ApiCase apiCase = apiCaseMapper.getApiCaseData(id);
        ApiForCase apiForCase = this.getApiDataForAddCase(apiCase.getApiId());
        BeanUtils.copyProperties(apiForCase,apiCaseUpdateData);
        BeanUtils.copyProperties(apiCase,apiCaseUpdateData);
        return  apiCaseUpdateData;
    }


}
