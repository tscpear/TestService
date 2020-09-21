package com.service.mapper;

import com.service.user.dao.entity.DUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface TestMapper {
    /**
     *
     * @return
     */
    DUser a();


}
