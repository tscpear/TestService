package com.service.apiTest.dom.entity;

import com.alibaba.fastjson.JSONArray;
import com.service.apiTest.service.domian.NewCaseList;
import lombok.Data;

import java.nio.channels.Pipe;

@Data
public class DoGroupReadyData {
    private  Integer id;
    private JSONArray caseList;
    private NewCaseList newCaseList;
    private String groupMark;
}
