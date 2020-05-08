package com.service.apiTest.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.service.apiTest.dom.domin.ApiGroupParamList;
import com.service.apiTest.dom.entity.ApiGroup;
import com.service.apiTest.dom.mapper.ApiGroupMapper;
import com.service.apiTest.service.domian.ApiGroupData;
import com.service.apiTest.service.service.ApiGroupService;
import com.service.utils.MyBaseChange;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ApiGroupImpl implements ApiGroupService {
    @Autowired
    private ApiGroupMapper apiGroupMapper;
    @Autowired
    private MyBaseChange b;


    @Override
    public JSONArray getList(ApiGroupParamList param) {
       List<ApiGroup> apiGroupList = apiGroupMapper.getList(param);
        JSONArray data = new JSONArray();
        for(ApiGroup apiGroup : apiGroupList){
            ApiGroupData apiGroupData = new ApiGroupData();
            BeanUtils.copyProperties(apiGroup,apiGroupData);
            apiGroupData.setTestList(b.StringToArray(apiGroup.getTestList()));
            data.add(apiGroupData);
        }
        return data;

    }
}
