package com.service.apiTest.service.domian;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class ApiData {
    private int id;
    private String apiPath;
    private String device;
    private String apiMethod;
    private String apiMark;
    private String apiParamType = "0";
    private String apiFiexdParam;
    private String apiRelyParamName;
    private String apiRelyParamValue;
    private JSONArray headerParamType = JSONArray.parseArray("['0']");
    private JSONArray headerFiexdParam= JSONArray.parseArray("[]");
    private JSONArray headerRelyParam= JSONArray.parseArray("[]");
    private JSONArray headerHandleParam= JSONArray.parseArray("[]");
    private JSONArray webformParamType= JSONArray.parseArray("['1']");
    private JSONArray webformFiexdParam= JSONArray.parseArray("[]");
    private JSONArray webformRelyParam= JSONArray.parseArray("[]");
    private JSONArray webformHandleParam= JSONArray.parseArray("[]");
    private JSONArray bodyParamType= JSONArray.parseArray("['0']");
    private String bodyFiexdParam;
    private JSONArray bodyRelyParam= JSONArray.parseArray("[]");
    private JSONArray bodyHandleParam= JSONArray.parseArray("[]");
    private Boolean isRely;
    private JSONArray relyValue = JSONArray.parseArray("[]");
    private String responseBase;
}
