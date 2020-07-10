package com.service.utils.test.dom;

import com.mysql.cj.xdevapi.JsonArray;
import lombok.Data;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.PathVariable;

@Data
public class DoTestData {
    private String apiPath;
    private Integer apiMethod;
    private String apiParam;
    private String host;
    private JSONArray headerParam = null;
    private String authorization;
    private JSONArray webformParam = null;
    private String bodyParam = null;
    private String cookie;
}
