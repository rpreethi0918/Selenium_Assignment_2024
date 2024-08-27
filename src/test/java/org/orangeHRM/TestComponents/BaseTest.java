package org.orangeHRM.TestComponents;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.orangeHRM.AbstractComponents.AbstractComponents;
import org.orangeHRM.pages.DirectoryPage;
import org.orangeHRM.pages.LoginPage;
import org.orangeHRM.pages.PIMPage;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    public WebDriver driver;
    public LoginPage loginPage;
    public AbstractComponents abstractComponents;
    public PIMPage pimPage;
    public DirectoryPage dirPage;
    public Properties env;
    public Properties staticData;
    public Properties selectors;

    @BeforeMethod
    public void setup() throws IOException {
        env = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "//src//main//resources//env.properties");
        env.load(fis);

        staticData = new Properties();
        FileInputStream staticFis = new FileInputStream(System.getProperty("user.dir") + "//src//main//resources//staticData.properties");
        staticData.load(staticFis);

        selectors = new Properties();
        FileInputStream selectorsFis = new FileInputStream(System.getProperty("user.dir") + "//src//test//resources//selectors.properties");
        selectors.load(selectorsFis);

        driver = initializeDriver();
        loginPage = new LoginPage(driver);
        abstractComponents = new AbstractComponents(driver);
        pimPage = new PIMPage(driver);
        dirPage = new DirectoryPage(driver);
        loginPage.goTo();
    }

    public WebDriver initializeDriver() throws IOException {
        String browserName = env.getProperty("browser");
        if (browserName.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-extensions");
            // Set up the preferences to disable password save prompts
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("credentials_enable_service", false);
            prefs.put("profile.password_manager_enabled", false);
            options.setExperimentalOption("prefs", prefs);
            options.setExperimentalOption("useAutomationExtension", false);
            options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        }
        return driver;
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public By getSelector(String key) {
        String selector = selectors.getProperty(key);
        if (selector == null) {
            throw new IllegalArgumentException("Selector not found for key: " + key);
        }
        if (selector.contains("(") || selector.contains("@")) {
            return By.xpath(selector);
        }
        return By.cssSelector(selector);
    }
}
