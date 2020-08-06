package com.service.apiTest.dom.mapper;

import com.alibaba.fastjson.JSONArray;
import com.service.apiTest.dom.domin.ApiGroupParamList;
import com.service.apiTest.dom.entity.Api;
import com.service.apiTest.dom.entity.ApiGroup;
import com.service.apiTest.service.domian.DataForReadyGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ApiGroupMapper {
    /**
     * 获取用户类型信息
     * @param id
     * @return
     */
    String getUserType(Integer id);

    List<ApiGroup> getList(ApiGroupParamList param);

    String getTestList(Integer id);

    void insertGroup(ApiGroup apiGroup);

    ApiGroup getGroup(Integer projectId,Integer id);

    void updateGroup(ApiGroup apiGroup);

    void delGroup(Integer id);

    List<DataForReadyGroup> getDataForReadyGroup(@Param("testIdList") JSONArray testIdList);

}
