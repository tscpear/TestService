package com.service.apiTest.controller.domin;


import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ApiCaseData {
    private Integer id;
    private Integer apiId;
    private String apiCaseMark;
    @NotNull(message = "用例等级不能为空")
    private Integer apiCaseLv;
    @NotNull(message = "用例类型不能为空")
    private Integer apiCaseType;
    private String apiHandleParam;
    private Boolean isDepend;
    private JSONArray headerHandleParam = JSONArray.parseArray("[]");
    private JSONArray webformHandleParam = JSONArray.parseArray("[]");
    private JSONArray bodyHandleParam = JSONArray.parseArray("[]");
    private String statusAssertion;
    private JSONArray otherAssertionType = JSONArray.parseArray("[]");
    private JSONArray responseValueExpect = JSONArray.parseArray("[]");
    private Integer userId;
    @NotNull(message = "账户类型不能为空")
    private Integer deviceType;
    private String device;
    private List<String> deviceTypeList;
    private JSONArray selectRelyCase = JSONArray.parseArray("[]");
    private JSONArray apiRelyToHandle = JSONArray.parseArray("[]");
    private JSONArray headerRelyToHandle = JSONArray.parseArray("[]");
    private JSONArray webformRelyToHandle = JSONArray.parseArray("[]");
    private JSONArray bodyRelyToHandle = JSONArray.parseArray("[]");
    private Integer closeCase;
    private JSONArray preCase = JSONArray.parseArray("[]");
}
