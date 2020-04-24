package com.service.apiTest.service.domian;

import lombok.Data;
import org.springframework.web.bind.annotation.PathVariable;

@Data
public class ApiCaseList {
    private Integer id;
    private String device;
    private  String apiPath;
    private String apiId;
    private String apiCaseMark;
    private Integer apiCaseLv;
    private Integer apiCaseType;

}
