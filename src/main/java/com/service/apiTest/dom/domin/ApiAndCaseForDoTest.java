package com.service.apiTest.dom.domin;

import lombok.Data;

@Data
public class ApiAndCaseForDoTest {
    private Integer testId;
    private String path;
    private String device;
    private String deviceType;
    private String apiParamType;
    private String apiFiexdParam;
    private String apiRelyParam;
    private String apiHandleParam;
    private String headerParamType;
    private String headerFiexdParam;
    private String headerRelyParam;
    private String headerHandleParam;
    private String webformParamType;
    private String webformFiexdParam;
    private String webformHandleParam;
    private String webformRelyParam;
    private String bodyParamType;
    private String bodyFiexdParam;
    private String bodyRelyParam;
    private String bodyHandleParam;
}
