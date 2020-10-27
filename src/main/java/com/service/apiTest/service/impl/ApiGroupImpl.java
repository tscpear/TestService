package com.service.apiTest.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.service.apiTest.controller.domin.DoGroupRequest;
import com.service.apiTest.controller.domin.RequestApiGroup;
import com.service.apiTest.dom.domin.ApiGroupParamList;
import com.service.apiTest.dom.entity.ApiGroup;
import com.service.apiTest.dom.entity.ApiReport;
import com.service.apiTest.dom.mapper.ApiGroupMapper;
import com.service.apiTest.dom.mapper.ApiReportMainMapper;
import com.service.apiTest.dom.mapper.ApiReportMapper;
import com.service.apiTest.service.domian.*;
import com.service.apiTest.dom.entity.DoGroupReadyData;
import com.service.apiTest.service.service.ApiGroupService;
import com.service.apiTest.service.service.ApiReportService;
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
    @Autowired
    private ApiReportService apiReportService;
    @Autowired
    private ApiReportMainMapper apiReportMainMapper;
    @Autowired
    private ApiReportMapper apiReportMapper;


    @Override
    public JSONArray getList(ApiGroupParamList param) {
        List<ApiGroup> apiGroupList = apiGroupMapper.getList(param);
        JSONArray data = new JSONArray();
        for (ApiGroup apiGroup : apiGroupList) {
            ApiGroupData apiGroupData = new ApiGroupData();
            BeanUtils.copyProperties(apiGroup, apiGroupData);
            apiGroupData.setCaseList(b.StringToArray(apiGroup.getCaseList()));
            data.add(apiGroupData);
        }
        return data;

    }

    @Override
    public void addGroup(RequestApiGroup requestApiGroup) {
        ApiGroup apiGroup = new ApiGroup();
        BeanUtils.copyProperties(requestApiGroup, apiGroup);
        apiGroup.setCaseList(requestApiGroup.getCaseList().toString());
        apiGroupMapper.insertGroup(apiGroup);
    }

    @Override
    public RequestApiGroup getRequestApiGroup(Integer id, Integer projectId) {
        ApiGroup apiGroup = apiGroupMapper.getGroup(projectId, id);
        RequestApiGroup requestApiGroup = new RequestApiGroup();
        BeanUtils.copyProperties(apiGroup, requestApiGroup);
        requestApiGroup.setCaseList(b.StringToAO(apiGroup.getCaseList()));
        return requestApiGroup;
    }

    @Override
    public void updateGroup(RequestApiGroup requestApiGroup) {
        ApiGroup apiGroup = new ApiGroup();
        BeanUtils.copyProperties(requestApiGroup, apiGroup);
        apiGroup.setCaseList(requestApiGroup.getCaseList().toString());
        apiGroupMapper.updateGroup(apiGroup);


    }

    @Override
    public void delGroup(Integer id) {
        apiGroupMapper.delGroup(id);
    }

    @Override
    public DoGroupOfRealyData getDoGroupReadyData(Integer id, Integer projectId) {
        DoGroupOfRealyData doGroupOfRealyData = new DoGroupOfRealyData();
        /**
         * 此处处理流程数据结构
         */
        ApiGroup apiGroup = apiGroupMapper.getGroup(projectId, id);
        doGroupOfRealyData.setGroupId(apiGroup.getId());
        doGroupOfRealyData.setGroupMark(apiGroup.getGroupMark());
        JSONArray a = b.StringToAO(apiGroup.getCaseList());
        List<DoGroupOfRealyDataToCaseList> doGroupOfRealyDataToCaseLists = new ArrayList<>();
        JSONArray testList = new JSONArray();
        for (Object o : a) {
            JSONObject o1 = b.StringToJson(o.toString());
            DoGroupOfRealyDataToCaseList doGroupOfRealyDataToCaseList = new DoGroupOfRealyDataToCaseList();
            doGroupOfRealyDataToCaseList.setTeamName(o1.get("groupMark").toString());
            JSONArray list = b.StringToArray(o1.get("caseList").toString());
            testList.addAll(list);
            List<DataForReadyGroup> dataForReadyGroups = apiGroupMapper.getDataForReadyGroup(list);
            doGroupOfRealyDataToCaseList.setDataForReadyGroups(dataForReadyGroups);
            doGroupOfRealyDataToCaseLists.add(doGroupOfRealyDataToCaseList);
        }
        doGroupOfRealyData.setDoGroupOfRealyDataToCaseLists(doGroupOfRealyDataToCaseLists);
        /**
         * 此处处理账号类型属性
         */
        Integer environment = 1;
        List<Integer> list = JSONObject.parseArray(JSONObject.toJSONString(testList), Integer.class);
        list = b.removeSame(list);
        testList = JSONArray.parseArray(list.toString());
        List<DeviceAndType> deviceAndTypeList = apiReportService.getDeviceTypeAndAccountList(testList, projectId, environment);
        doGroupOfRealyData.setDeviceAndTypes(deviceAndTypeList);
        doGroupOfRealyData.setTestList(list);
        return doGroupOfRealyData;
    }

    @Override
    public DoGroupOfRealyData doOne(DoGroupRequest doGroupRequest) {
        /**
         * 本层结果
         */
        Integer teamReslut = 1;

        /**
         * 判断是否需要创建reportId
         */
        long reportId = doGroupRequest.getReportId();
        if (doGroupRequest.getTeamId() <= 1 && doGroupRequest.getReportId() < 100000) {
            apiReportService.addReportMain(reportId);
        }

        /**
         * 获取数据结构
         */
        DoGroupOfRealyData doGroupOfRealyData = this.getDoGroupReadyData(doGroupRequest.getGroupId(), doGroupRequest.getProjectId());
        /**
         * 获取当前层的模块
         */
        List<DoGroupOfRealyDataToCaseList> doGroupOfRealyDataToCaseLists = doGroupOfRealyData.getDoGroupOfRealyDataToCaseLists();
        ApiGroup apiGroup = apiGroupMapper.getGroup(doGroupRequest.getProjectId(), doGroupRequest.getGroupId());
        JSONArray a = b.StringToAO(apiGroup.getCaseList());
//        Integer old = apiReportMainMapper.haveOldReport(doGroupRequest.getGroupId());
//
//        if (old < 1) {
//            apiReportService.addReportMain(doGroupRequest.getGroupId());
//        }
        /**
         * 执行用例
         */
        JSONObject o = b.StringToJson(a.get(doGroupRequest.getTeamId() - 1).toString());
        JSONArray list = b.StringToArray(o.get("caseList").toString());
        apiReportService.doTest(list, doGroupRequest.getEnvironment(), reportId, doGroupRequest.getAccountValue(), doGroupRequest.getProjectId(),2);

        /**
         * 获取报告
         */
        List<ApiReport> apiGroupOfReportResults = apiReportMapper.getApiGroupOfReportResult(reportId);
        int j = 0;
        for (int i = 0; i < doGroupRequest.getTeamId(); i++) {
            DoGroupOfRealyDataToCaseList doGroupOfRealyDataToCaseList = doGroupOfRealyDataToCaseLists.get(i);
            List<DataForReadyGroup> dataForReadyGroups = doGroupOfRealyDataToCaseList.getDataForReadyGroups();
            for (int k = 0;k<dataForReadyGroups.size();k++) {
                DataForReadyGroup dataForReadyGroup = dataForReadyGroups.get(k);
                Integer id = apiGroupOfReportResults.get(j).getId();
                Integer success = apiGroupOfReportResults.get(j).getResultMain();
                if(success == 0){
                    dataForReadyGroup.setSuccess(1);
                }else {
                    dataForReadyGroup.setSuccess(2);
                    teamReslut = 0;
                }
                dataForReadyGroup.setId(id);
                dataForReadyGroups.set(k,dataForReadyGroup);
                j++;
            }
            doGroupOfRealyDataToCaseList.setDataForReadyGroups(dataForReadyGroups);
            doGroupOfRealyDataToCaseLists.set(i,doGroupOfRealyDataToCaseList);

        }
        doGroupOfRealyData.setDoGroupOfRealyDataToCaseLists(doGroupOfRealyDataToCaseLists);
        doGroupOfRealyData.setReportId(reportId);
        doGroupOfRealyData.setTeamResult(teamReslut);
        return doGroupOfRealyData;
    }
}
