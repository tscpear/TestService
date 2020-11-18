package com.service.guiTest.service.service;

import com.service.guiTest.controller.domin.GuiGroupData;
import com.service.guiTest.dom.entity.GuiGroupDatas;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GuiGroupService  {
    /**
     * 新增GUI测试用例组
     * @param data
     */
    String add(GuiGroupData data,Integer userId);

    /**
     * 查询GUI用例组列表信息
     * @param project
     * @return
     */
    List<GuiGroupDatas> list(Integer project);

    /**
     * 获取用例组的详细信息
     * @param id
     * @param projectId
     * @return
     */
    GuiGroupData one(Integer id);

    /**
     * 编辑数据
     * @param data
     */
    String update(GuiGroupData data,Integer userId);


    void doTest(Integer id,Integer projectId,List<String> deviceId) throws InterruptedException;
}
