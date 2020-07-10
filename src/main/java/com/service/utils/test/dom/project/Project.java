package com.service.utils.test.dom.project;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Project {
    private List<String> environment;
    private List<Device> device;
    private String name;
    private Integer id;
}
