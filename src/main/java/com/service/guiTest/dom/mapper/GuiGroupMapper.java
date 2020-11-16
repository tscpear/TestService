package com.service.guiTest.dom.mapper;

import com.service.guiTest.dom.entity.GuiGroupDatas;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
@Repository
public interface GuiGroupMapper {
    /**
     * 插入基础数据
     * @param datas
     */
    void insertData(GuiGroupDatas datas);

    /**
     * 获取基础数据列表
     * @param projectId
     * @return
     */
    List<GuiGroupDatas> list(Integer projectId);

    /**
     * 获取一个信息
     * @param id
     * @return
     */
    GuiGroupDatas one(Integer id);

    /**
     * 改
     */
    void update(GuiGroupDatas datas);
}
