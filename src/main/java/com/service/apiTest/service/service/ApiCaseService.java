package com.service.apiTest.service.service;

import com.alibaba.fastjson.JSONArray;
import com.service.apiTest.dom.domin.ApiCaseListParam;
import com.service.apiTest.controller.domin.ApiCaseData;
import com.service.apiTest.service.domian.ApiCaseUpdateData;
import com.service.apiTest.service.domian.ApiForCase;
import org.springframework.stereotype.Service;

@Service
public interface ApiCaseService {
    /**
     * 获取新建测试用例的接口信息
     * @param apiId
     * @return
     */
    ApiForCase getApiDataForAddCase(Integer apiId);

    /**
     * 新增测试用例
     * @param apiCaseData
     */
    void  addApiCaseData(ApiCaseData apiCaseData);

    /**
     * 获取对应的API的测试数量
     * @param apiId
     * @return
     */
    Integer getCountApiCase(Integer apiId);

    /**
     * 所有的测试数据的数量
     * @return
     */
    Integer getCountApiCase();

    /**
     * 获取测试用例列表
     * @param apiCaseListParam
     * @return
     */
    JSONArray getApiCaseList(ApiCaseListParam apiCaseListParam);

    ApiCaseUpdateData getApiCaseData(int id);
}
