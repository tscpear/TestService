package com.service.apiTest.dom.mapper;

import com.alibaba.fastjson.JSONArray;
import com.service.apiTest.dom.domin.ApiCaseForReport;
import com.service.apiTest.dom.domin.ApiCaseListParam;
import com.service.apiTest.dom.domin.NewApiListCaseParam;
import com.service.apiTest.dom.entity.ApiCase;
import com.service.apiTest.service.domian.ApiReportList;
import com.service.apiTest.service.domian.DeviceAndType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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
    Integer getCountApiCase(NewApiListCaseParam param);

    /**
     * 获取测试用例列表
     * @return
     */
    List<ApiCase> getApiCaseList(NewApiListCaseParam param);
    List<ApiCase> getApiCaseListByApiId(NewApiListCaseParam param);

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

    /**
     * 删除用例数据
     * @param id
     */
    void delApiCase(Integer id,Integer userId);

    /**
     * 获取等级
     * @param id
     * @return
     */
    String findApiCaseOfLv(Integer id);

    /**
     * 获取类型
     * @param id
     * @return
     */
    String findApiCaseOfType(Integer id);

    /**
     * 商务
     * @param testIdList
     * @return
     */
    List<ApiReportList> getApiCaseListForReport(@Param("testIdList") JSONArray testIdList);

    /**
     * 获取依赖的用例
     * @param id
     * @return
     */
    List<ApiCase> getTestForRely(Integer id);

    /**
     * 获取对应用例列表的接口id列表
     * @param testIdList
     * @return
     */
    List<Integer> getApiIdFromApiCase(@Param("testIdList") JSONArray testIdList);


    List<String> getDeviceType(@Param("testIdList") JSONArray testIdList);


    /**
     * 通过testIdList  可以获取到设备Id 和账户类型的Id
     * @param testIdList
     * @return
     */
    List<DeviceAndType> getDeviceTypeList(@Param("testIdList") JSONArray testIdList);

    /**
     * 通过测试用例id获取 获取apiId
     * @param id
     * @return
     */
    Integer getApiIdByApiCaseId(Integer id);



}
