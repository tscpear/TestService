package com.service.user.service.domain;

import com.service.user.dao.entity.DUser;
import lombok.Data;

@Data
public class UserBaseRe {
    private int code;
    private String msg;
    private DUser user;
    private Object data;
}
