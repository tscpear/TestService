package com.service.utils.test.dom.project;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "project.project1")
public class Project {
    private List<String> environment;
    private List<Device> device;
    private String name;
    private Integer id;
}
