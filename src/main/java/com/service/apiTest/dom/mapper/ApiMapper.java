package com.service.apiTest.dom.mapper;

import com.service.apiTest.dom.domin.ApiListParam;
import com.service.apiTest.dom.entity.Api;
import com.service.apiTest.service.domian.ApiData;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ApiMapper {
    /**
     * 获取接口列表信息
     * @param params
     * @return
     */
    List<Api> getApiList(ApiListParam params);

    /**
     * 获取接口数量
     * @param params
     * @return
     */

    Integer getApiCount(ApiListParam params);

    /**
     * 获取接口详情信息
     * @param id
     * @return
     */
    Api getApiData(int id);


    /**
     * 更新接口详情信息
     * @param api
     */

    void updateApi(Api api);

    /**
     * 新增api
     */
    void addApi(Api api);

    /**
     * 查看apiPath method device 的接口数量
     */
    Integer countApi(String device,String apiPath);

    /**
     * 删除api
     */

    /**
     * 查询device 与 接口id
     */
    List<Integer> getApiIdForCaseList(String device,String apiPath);


}
