package com.service.guiTest.controller.domin;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

@Data
public class ElementData {
    private Integer id;
    private String name;
    private String element;
    private Integer device;
    private Integer elementType;
    private Integer active;
    private Integer projectId;
    private String keyValue;
    private JSONArray assertExpectValue = JSONArray.parseArray("[]");
}
