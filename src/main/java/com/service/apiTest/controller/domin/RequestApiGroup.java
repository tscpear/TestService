package com.service.apiTest.controller.domin;

import com.alibaba.fastjson.JSONArray;
import com.service.apiTest.service.domian.DeviceAndType;
import lombok.Data;

import java.util.List;

@Data
public class RequestApiGroup {
    private JSONArray caseList;
    private String groupMark;
    private Integer projectId;
    private Integer id;

}
