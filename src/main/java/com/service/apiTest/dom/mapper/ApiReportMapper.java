package com.service.apiTest.dom.mapper;

import com.service.apiTest.dom.entity.ApiReport;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ApiReportMapper {
    /**
     * 存入测试报告信息
     */
    void putData(ApiReport report);

    /**
     * 查询列表
     * @param reportId
     * @return
     */
    List<ApiReport> getList(long reportId);

    /**
     * 获取报告信息
     * @param testId
     * @param reportId
     * @return
     */
    ApiReport getData(Integer testId,long reportId);

    /**
     * 获取成功的数量
     * @param reportId
     * @return
     */
   Integer countOfSuccess(long reportId);
}
