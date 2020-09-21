package com.service.guiTest.service.impl;

import com.service.guiTest.controller.domin.ElementData;
import com.service.guiTest.dom.entity.GuiData;
import com.service.guiTest.dom.mapper.GuiMapper;
import com.service.guiTest.service.service.GuiService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuiImpl implements GuiService {
    @Autowired
    private GuiMapper guiMapper;

    @Override
    public void add(ElementData data) {
        GuiData guiData = new GuiData();
        BeanUtils.copyProperties(data, guiData);
        guiMapper.add(guiData);
    }

    @Override
    public List<GuiData> getData(Integer projectId) {
        return guiMapper.getData(projectId);
    }

    @Override
    public GuiData getOneData(Integer id) {
        return guiMapper.getOneData(id);
    }

    @Override
    public void update(GuiData data) {
        guiMapper.update(data);
    }
}
