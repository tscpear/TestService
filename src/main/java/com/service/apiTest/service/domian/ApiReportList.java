package com.service.apiTest.service.domian;

import lombok.Data;

@Data
public class ApiReportList {
    private Integer id;
    private String device;
    private String deviceType;
    private String apiPath;
    private String testMark;
    private String result;
}
