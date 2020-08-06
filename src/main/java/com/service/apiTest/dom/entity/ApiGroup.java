package com.service.apiTest.dom.entity;

import lombok.Data;

@Data
public class ApiGroup {
    private Integer id;
    private String caseList;
    private String groupMark;
    private Integer isDel;
    private Integer projectId;
}
