package com.service.guiTest.base.dom;

import io.appium.java_client.android.AndroidDriver;
import lombok.Data;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@Data
public class MyDriver {
    private WebDriver webDriver;
    private AndroidDriver<WebElement> appDriver;
    private Integer type;
    private String packageName;
    private String activity;
}
