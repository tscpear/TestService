package com.service.utils.test.dom;

import lombok.Data;
import org.springframework.stereotype.Service;

@Data
public class GetToken {
    private String environment;
    private String device;
    private String deviceType;
    private Integer userId;
}
