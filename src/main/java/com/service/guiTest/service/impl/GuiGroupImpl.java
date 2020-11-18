package com.service.guiTest.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.xdevapi.JsonArray;
import com.service.guiTest.base.dom.MyDriver;
import com.service.guiTest.base.service.DriverBaseService;
import com.service.guiTest.controller.domin.GuiGroupData;
import com.service.guiTest.dom.entity.GuiData;
import com.service.guiTest.dom.entity.GuiGroupDatas;
import com.service.guiTest.dom.mapper.GuiGroupMapper;
import com.service.guiTest.service.service.GuiGroupService;
import com.service.utils.MyBaseChange;
import com.service.utils.test.dom.project.Project;
import com.service.utils.test.dom.project.Project1;
import io.appium.java_client.android.Activity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.dc.pr.PRError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GuiGroupImpl implements GuiGroupService {

    @Autowired
    private GuiGroupMapper guiGroupMapper;
    @Autowired
    private MyBaseChange b;
    @Autowired
    private DriverBaseService driverBaseService;


    @Override
    public String add(GuiGroupData data,Integer userId) {
        String result = this.checkData(data.getDeviceList());
        if (!"success".equals(result)) {
            return result;
        }
        GuiGroupDatas datas = new GuiGroupDatas();
        BeanUtils.copyProperties(data, datas);
        datas.setDeviceList(data.getDeviceList().toString());
        datas.setCreateUserId(userId);
        guiGroupMapper.insertData(datas);
        return result;

    }

    @Override
    public List<GuiGroupDatas> list(Integer project) {
        return guiGroupMapper.list(project);
    }

    @Override
    public GuiGroupData one(Integer id) {
        GuiGroupDatas datas = guiGroupMapper.one(id);
        GuiGroupData data = new GuiGroupData();
        BeanUtils.copyProperties(datas, data);
        data.setDeviceList(b.StringToAO(datas.getDeviceList()));
        return data;
    }

    @Override
    public String update(GuiGroupData data,Integer userId) {
        String result = this.checkData(data.getDeviceList());
        if (!"success".equals(result)) {
            return result;
        }
        GuiGroupDatas datas = new GuiGroupDatas();
        BeanUtils.copyProperties(data, datas);
        datas.setDeviceList(data.getDeviceList().toString());
        datas.setUpdateUserId(userId);
        guiGroupMapper.update(datas);
        return result;
    }

    @Override
    public void doTest(Integer id, Integer projectId,List<String> deviceIds) throws InterruptedException {


        Integer ip = 4722;
        MyDriver driver;
        Integer index = -1;
        //获取GUI组的所有数据
        GuiGroupDatas datas = guiGroupMapper.one(id);
        //获取单个项目的list
        JSONArray deviceLists = b.StringToAO(datas.getDeviceList());
        String driverName;
        Integer deviceId;
        List<String> driverNames = new ArrayList<>();
        JSONObject value;
        Map<String, MyDriver> drivers = new HashMap<>();
        for (Object deviceList : deviceLists) {
            ip++;
            index++;
            System.out.println(ip);
            value = b.StringToJson(deviceList.toString());
            driverName = value.get("driverName").toString();
            if (!driverNames.contains(driverName)) {
                driverNames.add(driverName);
                deviceId = Integer.parseInt(value.get("deviceId").toString());
                driver = driverBaseService.getDriver(projectId, deviceId, 1, ip.toString(), deviceIds.get(index));
                Thread.sleep(5000);
            } else {
                    driver = drivers.get(driverName);

            }
            driverBaseService.doGuis(b.StringToListOfInt(value.get("list").toString()), driver);
            drivers.put(driverName,driver);
        }
        driverBaseService.quitDriver(drivers);
    }


    /**
     * 数据检查
     */
    public String checkData(JSONArray array) {
        Integer deviceId;
        List<Integer> ids = new ArrayList<>();
        array = b.StringToAO(array.toString());
        for (Object value : array) {
            JSONObject object = b.StringToJson(value.toString());
            deviceId = Integer.parseInt(object.get("deviceId").toString());
            ids = b.StringToListOfInt(object.get("list").toString());
            List<GuiData> datas = driverBaseService.getGuiData(ids);
            for (GuiData guiData : datas) {
                if (deviceId != guiData.getDevice()) {
                    return "用例" + guiData.getId() + "不属于编号" + object.get("driverName") + "对应的设备";
                }
            }
        }
        return "success";
    }


}
