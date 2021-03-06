package com.service.apiTest.dom.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ApiReport {
    private Integer id;
    private long reportId;
    private Integer testId;
    private String apiPath;
    private String headerParam;
    private String webformParam;
    private String bodyParam;
    private String apiMethod;
    private String response;
    private Integer responseBaseExpectResult;
    private String responseValueExpectResult;

    private String relyValue;

    private String expectStatus;
    private String actStatus;
    private Integer resultStatus;
    private Integer resultMain;
    private Integer device;
    private Integer deviceType;
    private String apiCaseMark;
    private  String relyValueLook;

}
