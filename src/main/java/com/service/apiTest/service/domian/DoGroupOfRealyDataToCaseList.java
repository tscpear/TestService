package com.service.apiTest.service.domian;

import lombok.Data;

import java.util.List;

@Data
public class DoGroupOfRealyDataToCaseList {
    private String teamName;
    private List<DataForReadyGroup> dataForReadyGroups;
    private Integer step = 0;
}
