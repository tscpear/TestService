package com.service.guiTest.controller.domin;

import lombok.Data;

@Data
public class ElementData {
    private String name;
    private String element;
    private Integer device;
    private Integer elementType;
    private Integer active;
    private String assertElement;
    private Integer assertElementType;
    private Integer projectId;
    private String keyValue;
    private String assertExpect;
}
