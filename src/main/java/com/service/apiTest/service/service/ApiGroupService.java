package com.service.apiTest.service.service;

import com.alibaba.fastjson.JSONArray;
import com.service.apiTest.controller.domin.DoGroupRequest;
import com.service.apiTest.controller.domin.RequestApiGroup;
import com.service.apiTest.dom.domin.ApiGroupParamList;
import com.service.apiTest.dom.entity.DoGroupReadyData;
import com.service.apiTest.service.domian.DoGroupOfRealyData;
import org.springframework.stereotype.Service;

@Service
public interface ApiGroupService {
    /**
     * 获取例表信息
     */
    JSONArray getList(ApiGroupParamList param);

    void addGroup(RequestApiGroup requestApiGroup);

    RequestApiGroup getRequestApiGroup(Integer id,Integer projectId);

    void updateGroup(RequestApiGroup requestApiGroup);

    void delGroup(Integer id);

    DoGroupOfRealyData getDoGroupReadyData(Integer id, Integer projectId);


    DoGroupOfRealyData doOne(DoGroupRequest doGroupRequest) throws Throwable;
}
