package com.service.apiTest.dom.domin;

import lombok.Data;

@Data
public class ApiListParam {
    private int pageBegin;
    private int PageEnd;
    private String apiPath;
    private String apiMark;
    private String device;
    private Integer projectId;
    private Integer createUserId;
}
