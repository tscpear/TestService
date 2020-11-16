package com.service.guiTest.service.impl;

import com.service.guiTest.controller.domin.ElementData;
import com.service.guiTest.dom.entity.GuiData;
import com.service.guiTest.dom.mapper.GuiMapper;
import com.service.guiTest.service.service.GuiService;
import com.service.utils.MyBaseChange;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuiImpl implements GuiService {
    @Autowired
    private GuiMapper guiMapper;
    @Autowired
    private MyBaseChange b;

    @Override
    public void add(ElementData data) {
        GuiData guiData = new GuiData();
        BeanUtils.copyProperties(data, guiData);
        guiData.setAssertExpectValue(data.getAssertExpectValue().toString());
        guiMapper.add(guiData);
    }

    @Override
    public List<GuiData> getData(Integer projectId) {
        return guiMapper.getData(projectId);
    }

    @Override
    public ElementData getOneData(Integer id) {
        ElementData elementData = new ElementData();
        GuiData data =  guiMapper.getOneData(id);
        BeanUtils.copyProperties(data, elementData);
        elementData.setAssertExpectValue(b.StringToAO(data.getAssertExpectValue()));
        return elementData;
    }

    @Override
    public void update(ElementData data) {
        GuiData guiData = new GuiData();
        BeanUtils.copyProperties(data, guiData);
        guiData.setAssertExpectValue(data.getAssertExpectValue().toString());
        guiMapper.update(guiData);
    }
}
