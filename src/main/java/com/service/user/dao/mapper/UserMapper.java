package com.service.user.dao.mapper;

import com.service.user.controller.domian.CUser;
import com.service.user.dao.entity.DUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {
    DUser getUserByName(String username);
}
