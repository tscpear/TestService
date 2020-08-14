package com.service.apiTest.service.domian;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DoGroupOfRealyData {
    private Integer groupId;
    private String groupMark;
    private List<DoGroupOfRealyDataToCaseList> doGroupOfRealyDataToCaseLists;
    private List<DeviceAndType> deviceAndTypes;
    private List<Integer> testList;
    private long reportId = 0;
    private Integer teamResult = 1;
}
