package com.service.apiTest.dom.mapper;

import com.service.apiTest.dom.domin.ApiCaseListParam;
import com.service.apiTest.dom.domin.NewApiListCaseParam;
import com.service.apiTest.dom.entity.ApiCase;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ApiCaseMapper {
    void addApiCaseData(ApiCase apiCase);
    /**
     * 获取对应的API的测试数量
     * @param apiId
     * @return
     */
    Integer getCountApiCaseByApiId(Integer apiId);

    /**
     * 所有的测试数据的数量
     * @return
     */
    Integer getCountApiCase();

    /**
     * 获取测试用例列表
     * @return
     */
    List<ApiCase> getApiCaseList(NewApiListCaseParam param);

    /**
     * 通过id获取商品信息
     * @param id
     * @return
     */
    ApiCase getApiCaseData(int id);

    /**
     * 更新用例数据
     * @param apiCase
     */
    void updateApiCaseData(ApiCase apiCase);
}
