package com.service.apiTest.dom.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ApiReportMainMapper {
    void add(Integer groupId,Integer id);
}
