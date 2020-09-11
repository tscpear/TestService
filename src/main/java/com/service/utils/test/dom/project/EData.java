package com.service.utils.test.dom.project;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class EData {
    private String loginHost;
    private Map<String,Object> loginHeader;
    private List<List<String>> account;
    private String host;
    private Map<String,Object> smsHeader;
    private String smsHost;
}
