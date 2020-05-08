package com.service.apiTest.dom.entity;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import java.util.Date;

@Data
public class ApiCase {
    private Integer id;
    private Integer apiId;
    private String apiCaseMark;
    private String apiCaseLv;
    private String apiCaseType;
    private String apiHandleParam;
    private String headerHandleParam;
    private String webformHandleParam;
    private String bodyHandleParam;
    private String relyCaseId;
    private Integer isDepend;
    private String deviceType;
    private String statusAssertion;
    private String otherAssertionType;
//    private String responseAssertion;
//    private String sqlAssertion;


    private Integer isDel;
    private Integer delUserId;
    private Date createTime;
    private Integer createUserId;
    private Date updateTime;
    private Integer updateUserId;

    private String headerRelyToHandle ;
    private String webformRelyToHandle ;
    private String bodyRelyToHandle ;

}
