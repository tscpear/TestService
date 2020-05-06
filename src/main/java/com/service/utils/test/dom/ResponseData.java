package com.service.utils.test.dom;

import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class ResponseData {
    private String apiMethod;
    private String path;
    private String bodyParam;
    private String headerParam;
    private String status;
    private String response;
    private String cookie;
}
