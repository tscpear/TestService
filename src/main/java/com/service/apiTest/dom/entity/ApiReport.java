package com.service.apiTest.dom.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ApiReport {
   private Integer id;
   private Integer reportId;
   private Integer testId;
   private String apiPath;
   private String headerParam;
   private String webformParam;
   private String bodyParam;
   private  String expectStatus;
   private  String actStatus;
}
