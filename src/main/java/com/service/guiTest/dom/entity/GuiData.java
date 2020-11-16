package com.service.guiTest.dom.entity;

import com.service.guiTest.controller.domin.ElementData;
import lombok.Data;

@Data
public class GuiData {
    private Integer id;
    private String name;
    private String element;
    private Integer device;
    private Integer elementType;
    private Integer active;
    private Integer projectId;
    private String keyValue;
    private String assertExpectValue = "[]";
}
