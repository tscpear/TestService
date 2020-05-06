package com.service.apiTest.dom.domin;

import lombok.Data;

@Data
public class ApiCaseForReport {
    private Integer testId;
    private String path;
    private String mark;
    private String device;
    private String deviceType;
}
