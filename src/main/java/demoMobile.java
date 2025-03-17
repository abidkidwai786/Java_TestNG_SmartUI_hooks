
import com.lambdatest.tunnel.Tunnel;
import cucumber.api.java.eo.Se;
import io.appium.java_client.AppiumDriver;
import org.apache.commons.lang3.time.StopWatch;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class demoMobile {

    public String username = "";
    public String accesskey = "";
    public RemoteWebDriver driver;
    public String gridURL = "mobile-hub.lambdatest.com";
    String status;
    String hub;
    SessionId sessionId;
    public String p="";
    public String platform_version="";
    public String device_name="";


    @org.testng.annotations.Parameters(value = {"browser", "platformVersion", "platform", "deviceName"})
    @BeforeTest
    public void setUp(String browser, String platformVersion, String platform, String deviceName) throws Exception {
        try {

            device_name=deviceName+" "; //for screenshot naming
            platform_version=platformVersion; //for screenshot naming


            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("name", "Mobile");
            capabilities.setCapability("build", "MobileDemo");
            capabilities.setCapability("deviceName", deviceName);
            capabilities.setCapability("platformVersion",platformVersion );
            capabilities.setCapability("platformName", platform);
            capabilities.setCapability("network", true);
            capabilities.setCapability("console", true);
            capabilities.setCapability("visual", true);
            capabilities.setCapability("smartUI.project", "Goldman Sachs VisualUI Mobile");
            capabilities.setCapability("smartUI.build", "Build1"); //UPDATE BUILD NAME IN EVERY RUN
            capabilities.setCapability("isRealMobile", true);

            StopWatch driverStart = new StopWatch();
            driverStart.start();

            hub = "https://" + username + ":" + accesskey + "@" + gridURL + "/wd/hub";
            driver = new RemoteWebDriver(new URL(hub), capabilities);

            sessionId = driver.getSessionId();
            System.out.println(sessionId);
            driverStart.stop();
            float timeElapsed = driverStart.getTime() / 1000f;
            System.out.println("Driver initiate time" + "   " + timeElapsed);
            ArrayList<Float> TotalTimeDriverSetup = new ArrayList<Float>();
            TotalTimeDriverSetup.add(timeElapsed);
            System.out.println(TotalTimeDriverSetup);


        } catch (
                MalformedURLException e) {
            System.out.println("Invalid grid URL");
        } catch (Exception f) {
            System.out.println(f);

        }

    }

    @Test
    public void DesktopScript() {
        try {
            driver.get("https://www.lambdatest.com/");
            Thread.sleep(5000);

            System.out.println("Taking FullPage Screenshot");
            //driver.executeScript("smartui.takeFullPageScreenshot=home-page-"+p+b+v);
            driver.executeScript("smartui.takeScreenshot=home-page-"+device_name+platform_version);
            Thread.sleep(1000);

            driver.get("https://www.lambdatest.com/pricing");
            Thread.sleep(5000);

            System.out.println("Taking Pricing Page Screenshot");
            driver.executeScript("smartui.takeScreenshot=pricing-page-"+device_name+platform_version);
            Thread.sleep(1000);

            driver.get("https://www.lambdatest.com/support/docs/");
            Thread.sleep(5000);

            System.out.println("Taking Docs Page Screenshot");
            driver.executeScript("smartui.takeScreenshot=docs-"+device_name+platform_version);

            System.out.println("TestFinished");
            status="passed";
        } catch (Exception e) {

            System.out.println(e);
            status = "failed";
        }
    }


    @AfterTest
    public void tearDown() throws Exception {
        if (driver != null) {
            ((JavascriptExecutor) driver).executeScript("lambda-status=" + status);
            driver.quit();
        }
    }
}

