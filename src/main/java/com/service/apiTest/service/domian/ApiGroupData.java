package com.service.apiTest.service.domian;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

@Data
public class ApiGroupData {
    private Integer id;
    private JSONArray caseList = JSONArray.parseArray("[]");
    private String groupMark;
}

