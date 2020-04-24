package com.service.apiTest.controller.domin;


import com.alibaba.fastjson.JSONArray;
import lombok.Data;

@Data
public class ApiCaseData {
    private Integer apiId;
    private String apiCaseMark;
    private String apiCaseLV;
    private String apiCaseType;
    private String apiHandleParam;
    private Boolean isDepend;
    private JSONArray headerHandleParam = JSONArray.parseArray("[]");
    private JSONArray webformHandleParam = JSONArray.parseArray("[]");
    private JSONArray bodyHandleParam = JSONArray.parseArray("[]");
    private String status;
    private JSONArray otherAssertionType = JSONArray.parseArray("[]");
    private Integer UserId;

}
