package com.service.apiTest.service.domian;

import com.alibaba.fastjson.JSONArray;
import com.service.apiTest.controller.domin.ApiCaseData;
import lombok.Data;

@Data
public class ApiCaseUpdateData extends ApiCaseData {
    private Integer id;
    private String apiPath;
    private String apiMark;
    private String apiParamType= "0";
    private Boolean isDepend = false;
    private Boolean hasRely = false;


    private JSONArray headerParamType= JSONArray.parseArray("['0']");
    private JSONArray headerFiexdParam = JSONArray.parseArray("[]");



    private JSONArray webformParamType= JSONArray.parseArray("['0']");
    private JSONArray webformFiexdParam= JSONArray.parseArray("[]");


    private JSONArray bodyParamType= JSONArray.parseArray("['0']");
    private String bodyFiexdParam;

    private String deviceType;

    private JSONArray selectRelyCase = JSONArray.parseArray("[]");
    private JSONArray relyCaseId = JSONArray.parseArray("[]");

}
