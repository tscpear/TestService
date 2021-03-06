package com.service.user.service.impl;

import com.service.user.dao.entity.ProjectData;
import com.service.user.dao.mapper.UserMapper;
import com.service.user.controller.domian.CUser;
import com.service.user.dao.entity.DUser;
import com.service.user.service.domain.UserBaseRe;
import com.service.user.service.service.UserService;
import com.service.utils.test.dom.project.Device;
import com.service.utils.test.dom.project.Project;
import com.service.utils.test.dom.project.Project1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
   private UserMapper userMapper;
    @Autowired
    private Project project1;



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
                baseRe.setProjectId(cUser.getProjectId());
                Project project = new Project();
                if(cUser.getProjectId() == 1){
                    project = project1;
                }
                baseRe.setEnvironment(project1.getEnvironment());
                baseRe.setProjectName(project1.getName());
                List<Device> devices = project1.getDevice();
                List<String> device = new ArrayList<>();
                for(Device d :devices){
                    device.add(d.getName());
                }
                baseRe.setDevice(device);

                /**
                 * 生成token
                 */
                String token = UUID.randomUUID().toString().replace("-", "");
                long tokenTime = System.currentTimeMillis();
                userMapper.updateToken(token,cUser.getUsername(),tokenTime);
                baseRe.setToken(token);

                /**
                 * 存入玩家列表
                 */
                baseRe.setUserList(userMapper.getUserList());

            } else {
                baseRe.setCode(0);
                baseRe.setMsg("密码错误");

            }
        }


        return baseRe;
    }

    @Override
    public List<ProjectData> getProject() {
       return userMapper.getProject();
    }

}
