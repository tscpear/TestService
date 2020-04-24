package com.service.apiTest.service.service;

import com.alibaba.fastjson.JSONArray;
import com.service.apiTest.dom.domin.ApiListParam;
import com.service.apiTest.controller.domin.ApiBaseRe;
import com.service.apiTest.service.domian.ApiData;
import com.service.apiTest.service.domian.ApiDataAU;
import com.service.apiTest.service.domian.ApiDataDel;
import org.springframework.stereotype.Service;

@Service
public interface ApiService{
    /**
     * 获取接口的列表信息
     * @param params
     * @return
     */
    JSONArray getApiList(ApiListParam params);


    /**
     * 获取接口数量
     */

    Integer getApiCount();
    /**
     * 获取接口详细信息
     * @param id
     * @return
     */
    ApiData getApi(int id);

    /**
     * 更新api的数据集
     */
    void updateApi(ApiDataAU apiData) throws Throwable;

    /**
     * 新增api
     */
    void addApi(ApiDataAU apiData) throws Throwable;

    /**
     * 删除API
     */
    ApiBaseRe delApi(ApiDataDel apiDataDel);
}
