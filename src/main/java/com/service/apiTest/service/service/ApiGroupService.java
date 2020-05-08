package com.service.apiTest.service.service;

import com.alibaba.fastjson.JSONArray;
import com.service.apiTest.dom.domin.ApiGroupParamList;
import com.service.apiTest.dom.entity.ApiGroup;
import com.service.apiTest.service.domian.ApiGroupData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ApiGroupService {
    /**
     * 获取例表信息
     */
    JSONArray getList(ApiGroupParamList param);


}
