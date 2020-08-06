package com.service.apiTest.controller.domin;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

@Data
public class RequestApiGroup {
    private JSONArray teamList;
    private String groupMark;
    private Integer projectId;
    private Integer id;
}
