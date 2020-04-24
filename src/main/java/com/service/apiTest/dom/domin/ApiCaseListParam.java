package com.service.apiTest.dom.domin;

import lombok.Data;

@Data
public class ApiCaseListParam {
    private int pageBegin;
    private int PageEnd;
    private Integer apiId;
    private String apiCaseMark;
    private String device;
}
