package com.service.user.service.service;

import com.service.user.controller.domian.CUser;
import com.service.user.dao.entity.ProjectData;
import com.service.user.service.domain.UserBaseRe;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    /**
     * 校验密码
     * @param cUser
     * @return
     */
    UserBaseRe login(CUser cUser);

    /**
     * 获取项目列表
     * @return
     */
    List<ProjectData> getProject();
}
