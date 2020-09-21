package com.service.guiTest.base.service;

import com.service.guiTest.dom.entity.GuiData;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DriverBaseService {
    /**
     * 获取浏览器驱动
     * @return
     */
    WebDriver getWebDriver();

    /**
     * 获取安卓手机驱动
     * @return
     */
    AndroidDriver<WebElement> getAndroidDriver();

    /**
     * 执行一个测试用例
     * @param driver
     * @param data
     * @return
     */
    boolean doGui(WebDriver driver, GuiData data) throws InterruptedException;

    /**
     * 获取用例基础数据list
     * @param ids
     * @return
     */
    List<GuiData> getGuiData(List<Integer> ids);

    /**
     * 批量执行
     * @param ids
     * @return
     * @throws InterruptedException
     */
    boolean doGuis(List<Integer> ids) throws InterruptedException;

    /**
     * 执行一个测试用例
     * @param driver
     * @param data
     * @return
     */
    boolean doGui( AndroidDriver<WebElement> driver, GuiData data) throws InterruptedException;

    boolean doAndroidGuis(List<Integer> ids);




}
