package com.service.apiTest.service.domian;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class ApiReportList {
    private Integer testId;
    private Integer device;
    private String apiPath;
    private String apiCaseMark;
    private String resultStatus;
    private long reportId;
    private Integer id;
    private Integer deviceType;
}
