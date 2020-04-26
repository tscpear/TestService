package com.service.apiTest.dom.domin;

import lombok.Data;

import java.util.List;

@Data
public class NewApiListCaseParam {
    private int pageBegin;
    private int PageEnd;

    private String apiCaseMark;
    private List<Integer> caseIdList;
}
