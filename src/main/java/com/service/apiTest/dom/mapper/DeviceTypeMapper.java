package com.service.apiTest.dom.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface DeviceTypeMapper {
    List<Integer> getDeviceTypeList(String device,Integer userId);
}
