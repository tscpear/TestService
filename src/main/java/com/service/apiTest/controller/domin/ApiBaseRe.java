package com.service.apiTest.controller.domin;

import lombok.Data;

@Data
public class ApiBaseRe {
    private int code;
    private String msg;
    private Object data;
    private Integer count;
}
