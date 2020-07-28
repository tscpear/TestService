package com.service.apiTest.dom.entity;

import lombok.Data;

@Data
public class NewToken {
    private Integer id;
    private Integer projectId;
    private Integer deviceId;
    private Integer deviceType;
    private Integer environment;
    private String data;
    private String token;
    private Integer accountId;
    private String cookie;
}
