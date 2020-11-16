package com.service.guiTest.base.service;

import com.service.guiTest.base.dom.MyDriver;
import com.service.guiTest.dom.entity.GuiData;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface DriverBaseService {


    /**
     * 执行一个测试用例
     * @param driver
     * @param data
     * @return
     */
    boolean doGui(MyDriver driver, GuiData data) throws InterruptedException;

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
    boolean doGuis(List<Integer> ids,MyDriver myDriver) throws InterruptedException;

    void doGuiMoudle();

    MyDriver getDriver(Integer projectId,Integer device,Integer environment,String ip,String id);


    void quitDriver(Map<String,MyDriver> drviers);



}
