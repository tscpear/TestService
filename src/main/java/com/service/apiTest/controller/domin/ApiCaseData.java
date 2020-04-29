package com.service.apiTest.controller.domin;


import com.alibaba.fastjson.JSONArray;
import lombok.Data;

@Data
public class ApiCaseData {
    private Integer id;
    private Integer apiId;
    private String apiCaseMark;
    private String apiCaseLv;
    private String apiCaseType;
    private String apiHandleParam;
    private Boolean isDepend;
    private JSONArray headerHandleParam = JSONArray.parseArray("[]");
    private JSONArray webformHandleParam = JSONArray.parseArray("[]");
    private JSONArray bodyHandleParam = JSONArray.parseArray("[]");
    private String statusAssertion;
    private JSONArray otherAssertionType = JSONArray.parseArray("[]");
    private Integer userId;
    private String deviceType;
    private String device;
    private JSONArray deviceTypeList = JSONArray.parseArray("[]");
}
