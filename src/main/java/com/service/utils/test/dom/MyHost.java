package com.service.utils.test.dom;

import lombok.Data;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class MyHost {
    private String tests;
    private String uat;
    private String test;
    private String prod;
    private String basic;
    private String basics;
    private List<String> account;
    private List<String> accounts;
    private String name;
    private String password;
    private String uatl;
    private String testl;
    private String testsl;
}
