package com.service.apiTest.service.domian;

import com.service.apiTest.dom.mapper.ApiCaseMapper;
import lombok.Data;

@Data
public class DataForReadyGroup {
    private Integer apiCaseId;
    private String apiCaseMark;
    private String apiPath;
    private Integer success = 0;
    private Integer id;
    private Integer device;
}
