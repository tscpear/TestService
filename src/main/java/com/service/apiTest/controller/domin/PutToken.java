package com.service.apiTest.controller.domin;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

@Data
public class PutToken {
    JSONArray testList = JSONArray.parseArray("[]");
    Integer groupId  = 0;
    String environment;
}
