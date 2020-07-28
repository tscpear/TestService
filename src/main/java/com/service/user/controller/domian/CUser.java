package com.service.user.controller.domian;


import lombok.Data;

@Data
public class CUser {
    private String username;
    private String password;
    private Integer projectId;
    private String token;

}
