package com.service.guiTest.service.service;

import com.service.guiTest.controller.domin.ElementData;
import com.service.guiTest.dom.entity.GuiData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GuiService {
    /**
     * 增加数据
     * @param data
     */
    void add(ElementData data);

    /**
     * 查询数据列表
     * @param projectId
     * @return
     */
    List<GuiData> getData(Integer projectId);

    /**
     * 通过id获取数据
     * @param id
     * @return
     */
    ElementData getOneData(Integer id);


    void update(ElementData data);
}
