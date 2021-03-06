package com.service.utils.test.dom.project;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Device {
    private Integer id;
    private String name;
    private List<EData> environment;
    private Map<String,Object> loginParam;
    private String loginUri;
    private List<String> deviceTypeList;
    private Integer loginType;
    private Map<String,Object> smsParam;
    private String smsUri;
    private Map<String,Object> smsLoginParam;
    private String tokenPath;
    private Map<String,String> loginRely;
    private String loginPage;
}
