package com.service.guiTest.base.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.service.guiTest.base.dom.MyDriver;
import com.service.guiTest.base.service.DriverBaseService;
import com.service.guiTest.dom.entity.GuiData;
import com.service.guiTest.dom.mapper.GuiMapper;
import com.service.utils.MyBaseChange;
import com.service.utils.MyObject.MyJsonPath;
import com.service.utils.test.dom.project.Project;
import com.service.utils.test.dom.project.Project1;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.PointOption;
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
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class DriverBaseImpl implements DriverBaseService {

    @Autowired
    private GuiMapper guiMapper;
    @Autowired
    private Project1 project1;
    @Autowired
    private MyBaseChange b;


    public WebDriver getWebDriver(String loginPageUri) {
        System.setProperty("webdriver.chrome.driver", "E:\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.navigate().to(loginPageUri);
        return driver;
    }


    public AndroidDriver<WebElement> getAndroidDriver(String appPackage, String appActivity, String ip, String id) {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
//        String ip = null;
//        String id = null;
//        appPackage = "com.kakebang.movingrescue";
//        appActivity = "com.kakebang.movingrescue.activity.GuideActivity";
//        ip = "4723";
//        id = "79UNW19307001406  79UNW19307001406";
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
    public boolean doGui(MyDriver driver, GuiData data) throws InterruptedException {
        WebElement webElement = this.getWebElement(data.getElementType(), driver, data.getElement());
        switch (data.getActive()) {
            case 1:
                webElement.click();
                break;
            case 2:
                Actions actions;
                if (driver.getType() == 1) {
                    actions = new Actions(driver.getWebDriver());
                } else {
                    actions = new Actions(driver.getAppDriver());
                }

                actions.doubleClick(webElement).build().perform();
                break;
            case 3:
                webElement.sendKeys(data.getKeyValue());
                break;
            case 4:
                webElement.clear();
                break;
            case 5:
                this.slideDown(driver.getAppDriver());
                break;
            case 6:
                this.slideUp(driver.getAppDriver());
                break;


        }
        if (!StringUtils.isEmpty(data.getAssertExpectValue())) {
            JSONArray assertValue = b.StringToAO(data.getAssertExpectValue());
            String assertElement;
            Integer assertElementType;
            String assertExpect;
            for (Object values : assertValue) {
                JSONObject value = b.StringToJson(values.toString());
                assertElement = value.get("assertElement").toString();
                assertElementType = Integer.parseInt(value.get("assertElementType").toString());
                assertExpect = value.get("assertExpect").toString();
                WebElement assertWebElement = this.getWebElement(assertElementType, driver, assertElement);
                String test = assertWebElement.getText();
                if (!test.equals(assertExpect)) {
                    return false;
                }
            }
            System.err.println("成功啦");
        }
        return true;
    }

    @Override
    public List<GuiData> getGuiData(List<Integer> ids) {
        List<GuiData> datas = guiMapper.getGuiData(ids);
        return datas;
    }

    @Override
    public boolean doGuis(List<Integer> ids, MyDriver myDriver) throws InterruptedException {
        boolean result = true;
        List<GuiData> datas = this.getGuiData(ids);
        for (int i = 0; i < datas.size(); i++) {
            GuiData data = datas.get(i);
            result = this.doGui(myDriver, data);
            if (!result) {
                break;
            }
        }


        return result;
    }

    @Override
    public void doGuiMoudle() {

    }


    @Override
    public MyDriver getDriver(Integer projectId, Integer device, Integer environment, String ip, String id) {
        MyDriver myDriver = new MyDriver();
        Project project = new Project();
        switch (projectId) {
            case 1:
                project = project1;
                break;
        }
        if (project.getDevice().get(device - 1).getLoginPage().equals("web")) {
            myDriver.setType(1);
            String loginPageUri = project.getDevice().get(device - 1).getEnvironment().get(environment - 1).getHost();
            myDriver.setWebDriver(this.getWebDriver(loginPageUri));
        } else {
            myDriver.setType(2);
            String loginPage = project.getDevice().get(device - 1).getLoginPage();
            String appPackage = loginPage.split("/")[0];
            String appActivity = loginPage.split("/")[1];
            myDriver.setAppDriver(this.getAndroidDriver(appPackage, appActivity, ip, id));
            myDriver.setActivity(appActivity);
            myDriver.setPackageName(appPackage);
        }
        return myDriver;
    }

    @Override
    public void quitDriver(Map<String, MyDriver> drviers) {
        for (String key : drviers.keySet()) {
            MyDriver myDriver = drviers.get(key);
            if (myDriver.getType() == 1) {
                myDriver.getWebDriver().quit();
            } else {
                myDriver.getAppDriver().quit();
            }
        }

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
            case 4:

                break;
        }
        return webElement;
    }

    public WebElement getWebElement(Integer type, AndroidDriver<WebElement> driver, String element) {
        WebElement webElement = null;

        switch (type == null ? 0:type) {
            case 0:
                break;
            case 1:
                webElement = driver.findElement(By.id(element));
                break;
            case 2:
                webElement = driver.findElement(By.xpath(element));
                break;
            case 3:
                webElement = driver.findElement(By.cssSelector(element));
                break;
            default:
                break;

        }
        return webElement;
    }


    public WebElement getWebElement(Integer type, MyDriver driver, String element) {
        if (driver.getType() == 1) {
            return getWebElement(type, driver.getWebDriver(), element);
        } else {
            return getWebElement(type, driver.getAppDriver(), element);
        }

    }

    /**
     * 向下滑屏的方法
     */
    public void slideDown(AndroidDriver<WebElement> driver) {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        new TouchAction(driver).press(PointOption.point(width / 2, height / 4))
                .moveTo(PointOption.point(width / 2, height * 3 / 4)).release().perform();
    }

    /**
     * 向上花屏 的方法
     */
    public void slideUp(AndroidDriver<WebElement> driver) {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        new TouchAction(driver).press(PointOption.point(width / 2, height * 3 / 4))
                .moveTo(PointOption.point(width / 2, height / 4)).release().perform();
    }


}
