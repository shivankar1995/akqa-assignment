package com.crm.qa.base;

import com.crm.qa.util.TestUtil;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class TestBase {

  public static WebDriver driver;
  public static Properties prop;

  public TestBase() {
    try {
      prop = new Properties();
      FileInputStream ip =
          new FileInputStream(
              System.getProperty("user.dir")
                  + "/src/main/java/com/crm"
                  + "/qa/config/config.properties");
      prop.load(ip);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @BeforeMethod
  public static void initialization() {
    String browserName = prop.getProperty("browser");

    if (browserName.equalsIgnoreCase("chrome")) {
      WebDriverManager.chromedriver().setup();
      driver = new ChromeDriver();
    } else if (browserName.equalsIgnoreCase("FF") || browserName.equalsIgnoreCase("firefox")) {
      WebDriverManager.firefoxdriver().setup();
      driver = new FirefoxDriver();
    } else {
      throw new IllegalArgumentException("Browser not supported: " + browserName);
    }

    driver.manage().window().maximize();
    driver.manage().deleteAllCookies();
    driver.manage().timeouts().pageLoadTimeout(TestUtil.PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
    driver.manage().timeouts().implicitlyWait(TestUtil.IMPLICIT_WAIT, TimeUnit.SECONDS);

    driver.get(prop.getProperty("url"));
  }

  @AfterMethod
  public void tearDown() {
    driver.quit();
  }
}
