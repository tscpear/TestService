package com.service.apiTest.dom.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Api {
    private int id;
    private String apiPath;
    private Integer device;
    private Integer apiMethod;
    private String apiMark;
    private String apiParamType;
    private String apiFiexdParam;
    private String apiRelyParam;
    private String headerParamType;
    private String headerFiexdParam;
    private String headerRelyParam;
    private String headerHandleParam;
    private String webformParamType;
    private String webformFiexdParam;
    private String webformRelyParam;
    private String webformHandleParam;
    private String bodyParamType;
    private String bodyFiexdParam;
    private String bodyRelyParam;
    private String bodyHandleParam;
    private Integer isRely;
    private String relyValue;
    private Integer isDel;
    private Integer delUserId;
    private Date updateTime;
    private Integer updateUserId;
    private Date createTime;
    private Integer createUserId;
    private String responseBase;
    private Integer projectId;
    private  Integer more;
}
