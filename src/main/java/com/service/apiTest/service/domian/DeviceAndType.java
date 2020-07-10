package com.service.apiTest.service.domian;

import lombok.Data;

import java.util.List;

@Data
public class DeviceAndType {
    private Integer device;
    private Integer deviceType;
    private List<String> accountList;
    private String deviceTypeName;
    private String deviceName;
}
