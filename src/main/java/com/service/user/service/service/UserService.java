package com.service.user.service.service;

import com.service.user.controller.domian.CUser;
import com.service.user.service.domain.UserBaseRe;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserBaseRe login(CUser cUser);
}
