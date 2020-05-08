package com.service.apiTest.dom.entity;

import lombok.Data;

@Data
public class Token {
    private int id;
    private String pdaCookie;
    private String tireWebToken;
}
