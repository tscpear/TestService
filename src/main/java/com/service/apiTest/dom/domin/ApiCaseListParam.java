package com.service.apiTest.dom.domin;

import lombok.Data;

@Data
public class ApiCaseListParam {
    private int pageBegin;
    private int PageEnd;
    private String apiPath;
    private String apiCaseMark;
    private String device;
    private Integer apiId;
}
