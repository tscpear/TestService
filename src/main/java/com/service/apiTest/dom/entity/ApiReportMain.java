package com.service.apiTest.dom.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ApiReportMain {
    private long id;
    private String testList;
    private Date createTime;
    private String success;
}
