package com.service.utils.test.dom;

import com.service.config.testBase.DeviceType;
import lombok.Data;

@Data
public class DoApiTestData {
    //用例表
    private Integer testId;
    //接口表
    private Integer apiId;
    //device_type表
    private Integer deviceTypeId;
    //记录表
    private Integer reportId;

}
