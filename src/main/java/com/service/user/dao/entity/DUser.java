package com.service.user.dao.entity;


import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
public class DUser implements Serializable {
    private int id;
    private String username;
    private String password;
    private String token;
    private long tokenTime;
}
