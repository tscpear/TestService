package com.service.guiTest.controller.domin;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

@Data
public class GuiGroupData {
    private Integer id;
    private JSONArray deviceList = JSONArray.parseArray("[]");
    private String groupMark;
    private Integer projectId;
}
