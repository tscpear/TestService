package com.service.user.service.domain;

import com.service.user.dao.entity.DUser;
import com.service.utils.test.dom.project.Project;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class UserBaseRe {
    private int code;
    private String msg;
    private DUser user;
    private Object data;
    private Integer projectId;
    private String projectName;
    private List<String> environment;
    private List<String> device;
}
