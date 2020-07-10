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
     * 重新执行的时候 更新数据
     */
    void updateByReportId(ApiReport report);
    /**
     * 查看该reportId 是否存在
     */
    Integer findReportIdAndTestId(ApiReport report);


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
    ApiReport getData(Integer id);

    /**
     * 获取成功的数量
     * @param reportId
     * @return
     */
   Integer countOfSuccess(long reportId);

    /**
     * 查最近的一个依赖用例的存储值
     * @param reportId
     * @param testId
     * @return
     */
   String getRelyValue(long reportId,Integer testId);

    /**
     * 获取当前已经执行的测试用例的Id
     * @param reportId
     * @return
     */
   List<Integer> getNowDoTestId(long reportId);


}
