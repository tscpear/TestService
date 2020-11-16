package com.service.guiTest.dom.mapper;

import com.service.guiTest.controller.domin.ElementData;
import com.service.guiTest.dom.entity.GuiData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GuiMapper {
    /**
     * 插入gui测试数据
     * @param data
     */
    void add(GuiData data);

    /**
     * 查询数据列表
     * @param projectId
     * @return
     */
    List<GuiData> getData(Integer projectId);

    /**
     * 获取数据列表来自ids
     * @param ids
     * @return
     */
    List<GuiData> getGuiData(@Param("ids") List<Integer> ids);

    /**
     * 获取一条数据
     * @param id
     * @return
     */
    GuiData getOneData(Integer id);

    /**
     * 更新数据
     * @param data
     */
    void update(GuiData data);

}
