package com.service.user.service.impl;

import com.service.user.dao.mapper.UserMapper;
import com.service.user.controller.domian.CUser;
import com.service.user.dao.entity.DUser;
import com.service.user.service.domain.UserBaseRe;
import com.service.user.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class UserServiceImpl implements UserService {
    @Autowired
   private UserMapper userMapper;



    @Override
    public UserBaseRe login(CUser cUser) {

        UserBaseRe baseRe = new UserBaseRe();
        DUser dUser = userMapper.getUserByName(cUser.getUsername());
        if(dUser.getPassword()==null){
            baseRe.setCode(0);
            baseRe.setMsg("账号不存在");
        }else {
            if (dUser.getPassword().equals(cUser.getPassword())) {
                baseRe.setCode(1);
                baseRe.setUser(dUser);

            } else {
                baseRe.setCode(0);
                baseRe.setMsg("密码错误");
            }
        }


        return baseRe;
    }

}
