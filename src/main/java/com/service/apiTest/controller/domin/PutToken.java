package com.service.apiTest.controller.domin;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

import java.util.List;

@Data
public class PutToken {
    JSONArray testList = JSONArray.parseArray("[]");
    Integer groupId  = 0;
    Integer environment;
    long  reportId =0;
    List<String> accountValue;
}
