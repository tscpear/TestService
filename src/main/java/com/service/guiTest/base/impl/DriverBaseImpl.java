package com.service.guiTest.base.impl;

import com.service.guiTest.base.service.DriverBaseService;
import com.service.guiTest.dom.entity.GuiData;
import com.service.guiTest.dom.mapper.GuiMapper;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class DriverBaseImpl implements DriverBaseService {

    @Autowired
    private GuiMapper guiMapper;

    @Override
    public WebDriver getWebDriver() {
        System.setProperty("webdriver.chrome.driver", "E:\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.navigate().to("https://ra-t.zhilunkeji.com/login");
        return driver;
    }

    @Override
    public AndroidDriver<WebElement> getAndroidDriver() {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        String appPackage = null;
        String appActivity = null;
        String ip = null;
        String id = null;
        appPackage = "com.kakebang.movingrescue";
        appActivity = "com.kakebang.movingrescue.activity.GuideActivity";
        ip = "4723";
        id = "79UNW19307001406";
        desiredCapabilities.setCapability("platformName", "Android");
        desiredCapabilities.setCapability("deviceName", "127.0.0.1:7555");
        desiredCapabilities.setCapability("appPackage", appPackage);
        desiredCapabilities.setCapability("appActivity", appActivity);
        desiredCapabilities.setCapability("autoGrantPermissions", "true");
        desiredCapabilities.setCapability("noReset", true);
        desiredCapabilities.setCapability("udid", id);
        desiredCapabilities.setCapability("resetKeyboard", "true");
        try {
            return new AndroidDriver<WebElement>(new URL("http://0.0.0.0:" + ip + "/wd/hub"), desiredCapabilities);
        } catch (MalformedURLException e) {
            System.err.println("appium链接失败！");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean doGui(WebDriver driver, GuiData data) throws InterruptedException {
        WebElement webElement = this.getWebElement(data.getElementType(), driver, data.getElement());
        switch (data.getActive()) {
            case 1:
                webElement.click();
                break;
            case 2:
                Actions actions = new Actions(driver);
                actions.doubleClick(webElement).build().perform();
                break;
            case 3:
                webElement.sendKeys(data.getKeyValue());
                break;
            case 4:
                webElement.clear();
                break;
        }

        if (!StringUtils.isEmpty(data.getAssertElement())) {
            WebElement assertWebElement = this.getWebElement(data.getAssertElementType(), driver, data.getAssertElement());
            String test = assertWebElement.getText();
            if (!test.equals(data.getAssertExpect())) {
                return false;
            } else {
                System.err.println("成功啦");
            }
        }
        return true;
    }

    @Override
    public List<GuiData> getGuiData(List<Integer> ids) {
        List<GuiData> datas = guiMapper.getGuiData(ids);
        return datas;
    }

    @Override
    public boolean doGuis(List<Integer> ids) throws InterruptedException {
        boolean result = true;
        WebDriver webDriver = this.getWebDriver();
        List<GuiData> datas = this.getGuiData(ids);
        for (int i = 0; i < datas.size(); i++) {
            GuiData data = datas.get(i);
            result = this.doGui(webDriver, data);
            if (!result) {
                break;
            }
        }
        webDriver.quit();
        return result;
    }

    @Override
    public boolean doGui(AndroidDriver<WebElement> driver, GuiData data) throws InterruptedException {
        WebElement webElement = this.getWebElement(data.getElementType(), driver, data.getElement());
        switch (data.getActive()) {
            case 1:
                webElement.click();
                break;
            case 2:
                Actions actions = new Actions(driver);
                actions.doubleClick(webElement).build().perform();
                break;
            case 3:
                webElement.sendKeys(data.getKeyValue());
                break;
            case 4:
                webElement.clear();
                break;
        }

        if (!StringUtils.isEmpty(data.getAssertElement())) {
            WebElement assertWebElement = this.getWebElement(data.getAssertElementType(), driver, data.getAssertElement());
            String test = assertWebElement.getText();
            if (!test.equals(data.getAssertExpect())) {
                return false;
            } else {
                System.err.println("成功啦");
            }
        }
        return true;
    }

    @Override
    public boolean doAndroidGuis(List<Integer> ids) {

        boolean result = true;
        AndroidDriver<WebElement> driver = this.getAndroidDriver();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<GuiData> datas = this.getGuiData(ids);
        for (int i = 0; i < datas.size(); i++) {


            GuiData data = datas.get(i);
            try {
                result = this.doGui(driver, data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!result) {
                break;
            }
        }
        driver.quit();
        return result;
    }

    public WebElement getWebElement(Integer type, WebDriver driver, String element) {
        WebElement webElement = null;
        switch (type) {
            case 1:
                webElement = driver.findElement(By.id(element));
                break;
            case 2:
                webElement = driver.findElement(By.xpath(element));
                break;
            case 3:
                webElement = driver.findElement(By.cssSelector(element));
                break;
        }
        return webElement;
    }

    public WebElement getWebElement(Integer type, AndroidDriver<WebElement> driver, String element) {
        WebElement webElement = null;
        switch (type) {
            case 1:
                webElement = driver.findElement(By.id(element));
                break;
            case 2:
                webElement = driver.findElement(By.xpath(element));
                break;
            case 3:
                webElement = driver.findElement(By.cssSelector(element));
                break;
        }
        return webElement;
    }
}
