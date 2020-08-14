package com.service.apiTest.dom.mapper;

import com.service.apiTest.dom.domin.ApiListParam;
import com.service.apiTest.dom.entity.ApiReportMain;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ApiReportMainMapper {
    /**
     * 新增主报告核心
     * @param id
     */
    void add(long id);

    /**
     * 存入成功率
     * @param success
     * @param id
     */
    void updateSuccess(String success,long id);

    /**
     * 获取列表信息
     * @param param
     * @return
     */
    List<ApiReportMain> getList(ApiListParam param);

    /**
     * 获取总量
     * @return
     */
    Integer getCount(Integer projectId);


    /**
     * 判断流程有没有保存到mian
     */
    Integer haveOldReport(Integer id);

}
