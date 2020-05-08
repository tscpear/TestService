package com.service.apiTest.dom.mapper;

import com.service.apiTest.dom.domin.ApiGroupParamList;
import com.service.apiTest.dom.entity.ApiGroup;
import org.apache.ibatis.annotations.Mapper;
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


    void insert(String list,String type);

    List<ApiGroup> getList(ApiGroupParamList param);
}
