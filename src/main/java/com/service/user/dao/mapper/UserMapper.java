package com.service.user.dao.mapper;

import com.service.user.controller.domian.UserList;
import com.service.user.dao.entity.DUser;
import com.service.user.dao.entity.ProjectData;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserMapper {
    /**
     * 获取用户名与密码
     * @param username
     * @return
     */
    DUser getUserByName(String username);


    List<ProjectData> getProject();


    void updateToken(String token,String username,long tokenTime);


    DUser getUserBy(String token);

    /**
     * 获取用户列表
     * @return
     */
    List<UserList> getUserList();
}
