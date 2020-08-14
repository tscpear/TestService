package com.service.apiTest.controller.domin;

import lombok.Data;

import java.util.List;

@Data
public class DoGroupRequest {
    private Integer groupId;
    private Integer teamId;
    private Integer projectId;
    private Integer environment;
    List<String> accountValue;
    private long reportId;
}
