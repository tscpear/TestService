package com.service.apiTest.service.domian;

import lombok.Data;
import org.springframework.web.bind.annotation.PathVariable;

@Data
public class ApiCaseList {
    private Integer id;
    private int device;
    private  String apiPath;
    private String apiId;
    private String apiCaseMark;
    private String apiCaseLv;
    private String apiCaseType;

}
