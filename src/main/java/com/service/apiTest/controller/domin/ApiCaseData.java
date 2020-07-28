package com.service.apiTest.controller.domin;


import com.alibaba.fastjson.JSONArray;
import lombok.Data;

import java.util.List;

@Data
public class ApiCaseData {
    private Integer id;
    private Integer apiId;
    private String apiCaseMark;
    private Integer apiCaseLv;
    private Integer apiCaseType;
    private String apiHandleParam;
    private Boolean isDepend;
    private JSONArray headerHandleParam = JSONArray.parseArray("[]");
    private JSONArray webformHandleParam = JSONArray.parseArray("[]");
    private JSONArray bodyHandleParam = JSONArray.parseArray("[]");
    private String statusAssertion;
    private JSONArray otherAssertionType = JSONArray.parseArray("[]");
    private JSONArray responseValueExpect = JSONArray.parseArray("[]");
    private Integer userId;
    private Integer deviceType;
    private String device;
    private List<String> deviceTypeList;
    private JSONArray selectRelyCase = JSONArray.parseArray("[]");
    private JSONArray apiRelyToHandle = JSONArray.parseArray("[]");
    private JSONArray headerRelyToHandle = JSONArray.parseArray("[]");
    private JSONArray webformRelyToHandle = JSONArray.parseArray("[]");
    private JSONArray bodyRelyToHandle = JSONArray.parseArray("[]");
}
