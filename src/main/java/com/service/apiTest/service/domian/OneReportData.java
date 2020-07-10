package com.service.apiTest.service.domian;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class OneReportData {
    private Integer testId;
    private String apiPath;
    private JSONArray headerParam= JSONArray.parseArray("[]");
    private JSONArray webformParam = JSONArray.parseArray("[]");
    private Object bodyParam;
    private String apiMethod;

    private JSONObject response;
    private String relyValue;

    private  String expectStatus;
    private  String actStatus;
    private  Integer resultStatus;
    private Integer resultMain;
    private JSONArray responseValueExpectResult = JSONArray.parseArray("[]");
}
