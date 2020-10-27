package com.service.apiTest.dom.mapper;

import com.service.apiTest.dom.domin.ApiListParam;
import com.service.apiTest.dom.entity.Api;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
    Integer countApi(Integer device,String apiPath,Integer projectId);

    /**
     * 删除api
     */
    void del(Integer id);

    /**
     * 查询device 与 接口id
     */
    List<Integer> getApiIdForCaseList(Integer device,String apiPath,Integer projectId);

    /**
     * 获取查找的id path
     * @param path
     * @return
     */
    List<Api> getApiForPath(String path,Integer projectId);

    /**
     * 获取接口路径byid
     * @param id
     * @return
     */
    String getPathById(Integer id);

    /**
     * 查询依赖数据的name
     * @param id
     * @return
     */
    String searchRelyName(Integer id);

    /**
     * 查询id
     * @param path
     * @return
     */
    Integer getApiIdByPath(String path);

    /**
     * 获取设备列表
     * @param apiIdList
     * @return
     */
    List<String> getDeviceList(@Param("apiIdList") List<Integer> apiIdList);

    /**
     * 查询有没有重复的数据
     * @param device
     * @param apiPath
     * @return
     */
    Integer getCountReData(Integer device,String apiPath,Integer projectId,Integer apiMethod);

}
