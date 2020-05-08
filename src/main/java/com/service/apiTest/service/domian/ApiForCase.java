package com.service.apiTest.service.domian;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;


@Data
public class ApiForCase {
    private Integer apiId;
    private String apiPath;
    private String apiMark;
    private String apiParamType= "0";
    private Boolean isDepend = false;
    private Boolean hasRely = false;



    private JSONArray headerParamType= JSONArray.parseArray("['0']");
    private JSONArray headerFiexdParam = JSONArray.parseArray("[]");
    private JSONArray headerHandleParam= JSONArray.parseArray("[]");

    private JSONArray webformParamType= JSONArray.parseArray("['0']");
    private JSONArray webformFiexdParam= JSONArray.parseArray("[]");
    private JSONArray webformHandleParam= JSONArray.parseArray("[]");


    private JSONArray bodyParamType= JSONArray.parseArray("['0']");
    private String bodyFiexdParam;
    private JSONArray bodyHandleParam= JSONArray.parseArray("[]");
    private JSONArray deviceTypeList= JSONArray.parseArray("[]");
    private String device ;

    private JSONArray selectRelyCase = JSONArray.parseArray("[]");

    private JSONArray apiRelyToHandle = JSONArray.parseArray("[]");
    private JSONArray headerRelyToHandle = JSONArray.parseArray("[]");
    private JSONArray webformRelyToHandle = JSONArray.parseArray("[]");
    private JSONArray bodyRelyToHandle = JSONArray.parseArray("[]");
}
