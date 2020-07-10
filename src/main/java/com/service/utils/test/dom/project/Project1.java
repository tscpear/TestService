package com.service.utils.test.dom.project;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "project.project1")
public class Project1 extends Project {

}

