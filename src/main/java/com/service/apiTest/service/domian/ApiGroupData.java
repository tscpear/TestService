package com.service.apiTest.service.domian;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

@Data
public class ApiGroupData {
    private Integer id;
    private JSONArray testList = JSONArray.parseArray("[]");
    private String userType;
    private String groupMark;
    private String groupType;
}
