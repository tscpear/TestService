package com.service.apiTest.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.service.apiTest.controller.domin.RequestApiGroup;
import com.service.apiTest.dom.domin.ApiGroupParamList;
import com.service.apiTest.dom.entity.ApiGroup;
import com.service.apiTest.dom.mapper.ApiGroupMapper;
import com.service.apiTest.service.domian.ApiGroupData;
import com.service.apiTest.dom.entity.DoGroupReadyData;
import com.service.apiTest.service.domian.DataForReadyGroup;
import com.service.apiTest.service.domian.NewCaseList;
import com.service.apiTest.service.service.ApiGroupService;
import com.service.utils.MyBaseChange;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            apiGroupData.setCaseList(b.StringToArray(apiGroup.getCaseList()));
            data.add(apiGroupData);
        }
        return data;

    }

    @Override
    public void addGroup(RequestApiGroup requestApiGroup) {
        ApiGroup apiGroup = new ApiGroup();
        BeanUtils.copyProperties(requestApiGroup,apiGroup);
        apiGroup.setCaseList(requestApiGroup.getTeamList().toString());
        apiGroupMapper.insertGroup(apiGroup);
    }

    @Override
    public RequestApiGroup getRequestApiGroup(Integer id, Integer projectId) {
        ApiGroup apiGroup = apiGroupMapper.getGroup(projectId,id);
        RequestApiGroup requestApiGroup = new RequestApiGroup();
        BeanUtils.copyProperties(apiGroup,requestApiGroup);
        requestApiGroup.setTeamList(b.StringToAO(apiGroup.getCaseList()));
        return requestApiGroup;
    }

    @Override
    public void updateGroup(RequestApiGroup requestApiGroup) {
        ApiGroup apiGroup = new ApiGroup();
        BeanUtils.copyProperties(requestApiGroup,apiGroup);
        apiGroup.setCaseList(requestApiGroup.getTeamList().toString());
        apiGroupMapper.updateGroup(apiGroup);


    }

    @Override
    public void delGroup(Integer id) {
        apiGroupMapper.delGroup(id);
    }

    @Override
    public DoGroupReadyData getDoGroupReadyData(Integer id,Integer projectId) {
        DoGroupReadyData doGroupReadyData = new DoGroupReadyData();
        ApiGroup apiGroup = apiGroupMapper.getGroup(projectId,id);
        doGroupReadyData.setGroupMark(apiGroup.getGroupMark());
        JSONArray caseList = b.StringToAO(apiGroup.getCaseList());
        for(Object o : caseList){
            JSONObject caseLists = b.StringToJson(o.toString());
            String cases = caseLists.get("teamList").toString();
            String groupMark = caseLists.get("teamMark").toString();
            JSONArray caseArray = b.StringToArray(cases);
            List<DataForReadyGroup> dataForReadyGroups = apiGroupMapper.getDataForReadyGroup(caseArray);
            NewCaseList newCaseList = new NewCaseList();

        }




        return null;
    }
}
