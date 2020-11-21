package com.service.apiTest.service.domian;

import com.service.apiTest.dom.entity.ApiReport;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ApiReportCache {

    /**
     * 已执行的用例列表
     */
    private List<Integer>  testIdDoneList = new ArrayList<>();
    /**
     * shuju
     */

    private Map<Integer, List<ApiReport>> apiReportList = new HashMap<>();


    private long reportId;

}
