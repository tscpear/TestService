package com.service.apiTest.service.domian;

import lombok.Data;

@Data
public class ApiListData {
    private Integer id;
    private String apiPath;
    private String apiMark;
    private Integer testNum;
    private String apiMethod;
    private  String device;
}
