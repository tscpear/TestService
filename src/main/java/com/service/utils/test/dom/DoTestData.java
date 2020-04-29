package com.service.utils.test.dom;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.xdevapi.JsonArray;
import lombok.Data;
import org.springframework.web.bind.annotation.PathVariable;

@Data
public class DoTestData {
    private String apiPath;
    private String apiMethod;
    private String apiParam;
    private JSONArray headerParam = JSONArray.parseArray("[]");
    private String authorization;
    private JSONArray webformParam = JSONArray.parseArray("[]");
    private JSONArray bodyParam = JSONArray.parseArray("[]");
}
