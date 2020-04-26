package com.service.apiTest.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.service.apiTest.dom.domin.ApiCaseListParam;
import com.service.apiTest.dom.domin.NewApiListCaseParam;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
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
        apiCaseMapper.addApiCaseData(this.updateAndAdd(apiCaseData));
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
        NewApiListCaseParam param = new NewApiListCaseParam();
        BeanUtils.copyProperties(apiCaseListParam, param);
        List<Integer> caseIdList = new ArrayList<>();
        String device = apiCaseListParam.getDevice();
        String apiPath  =  apiCaseListParam.getApiPath();
        if(StringUtils.isEmpty(apiPath) && StringUtils.isEmpty(device)){

            caseIdList = null;
        }else {

            caseIdList = apiMapper.getApiIdForCaseList(device,apiPath);
            System.out.println(caseIdList);
        }
        param.setCaseIdList(caseIdList);
        List<ApiCase> apiCaseLists = apiCaseMapper.getApiCaseList(param);
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
        return caseList;
    }

    @Override
    public ApiCaseUpdateData getApiCaseData(int id) {
        ApiCaseUpdateData apiCaseUpdateData = new ApiCaseUpdateData();
        ApiCase apiCase = apiCaseMapper.getApiCaseData(id);
        ApiForCase apiForCase = this.getApiDataForAddCase(apiCase.getApiId());
        BeanUtils.copyProperties(apiForCase, apiCaseUpdateData);
        BeanUtils.copyProperties(apiCase, apiCaseUpdateData);
        return apiCaseUpdateData;
    }

    @Override
    public void updateApiCaseData(ApiCaseData apiCaseData) {
        apiCaseMapper.updateApiCaseData(this.updateAndAdd(apiCaseData));
    }


    public ApiCase updateAndAdd(ApiCaseData apiCaseData) {
        ApiCase apiCase = new ApiCase();
        BeanUtils.copyProperties(apiCaseData, apiCase);
        apiCase.setApiHandleParam(apiCaseData.getApiHandleParam());
        apiCase.setHeaderHandleParam(apiCaseData.getHeaderHandleParam().toString());
        apiCase.setWebformHandleParam(apiCaseData.getWebformHandleParam().toString());
        apiCase.setBodyHandleParam(apiCaseData.getBodyHandleParam().toString());
        return apiCase;
    }


}
