package org.orangeHRM.pages;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.orangeHRM.AbstractComponents.AbstractComponents;
import org.testng.Assert;
import org.testng.Reporter;

public class LoginPage extends AbstractComponents {
    WebDriver driver;

    @FindBy(name = "username")
    WebElement userNameDOM;

    @FindBy(name = "password")
    WebElement passwordDOM;

    @FindBy(tagName = "button")
    WebElement loginButton;

    @FindBy(xpath = "(//p[@class='oxd-text oxd-text--p oxd-alert-content-text'])[1]")
    WebElement errorMessage;

    @FindBy(tagName = "span")
    WebElement requiredMessage;

    @FindBy(xpath = "//h6[normalize-space()='Dashboard']")
    WebElement dashboardTitle;

    public LoginPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void loginOrangeHRM(String username, String password) {
        userNameDOM.sendKeys(username);
        takeScreenshot("loginOrangeHRM_EnterUsername");
        passwordDOM.sendKeys(password);
        takeScreenshot("loginOrangeHRM_EnterPassword");
        Reporter.log("Username and Password entered successfully");
        loginButton.click();
        Reporter.log("Clicked Login button");
        takeScreenshot("loginOrangeHRM_ClickLoginButton");
    }

    public void goTo() {
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        Reporter.log("Opened browser and landed on Login Screen");
        takeScreenshot("goTo_LoginPage");
    }

    public String getErrorMessage() {
        takeScreenshot("getErrorMessage_BeforeReturn");
        Reporter.log("Error message Displayed Successfully");
        return errorMessage.getText().trim();
    }

    public String getRequiredMessage() {
        takeScreenshot("getRequiredMessage_BeforeReturn");
        Reporter.log("Required Field message displayed successfully");
        return requiredMessage.getText().trim();
    }

    public String getDashboardTitle() {
        takeScreenshot("getDashboardTitle_BeforeReturn");
        Reporter.log("Asserted Dashboard Screen by Title");
        return dashboardTitle.getText().trim();
    }

    public String getCurrentURL() {
        takeScreenshot("getCurrentURL_BeforeReturn");
        Reporter.log("Asserted Dashboard Screen by URL");
        return driver.getCurrentUrl();
    }
    
    public void loginAndAssertDashboard(String username, String password, String expectedDashboardUrl, String expectedDashboardTitle) {
        userNameDOM.sendKeys(username);
        passwordDOM.sendKeys(password);
        loginButton.click();
        takeScreenshot("loginAndAssertDashboard_AfterClickLogin");

        try {
            Assert.assertEquals(getCurrentURL(), expectedDashboardUrl, "URL does not match");
            Assert.assertEquals(getDashboardTitle(), expectedDashboardTitle, "Dashboard title does not match");
            takeScreenshot("loginAndAssertDashboard_Success");
            Reporter.log("Test case asserting Dashboard Passed");
            System.out.println("Test case asserting Dashboard Passed");
        } catch (AssertionError | Exception e) {
            takeScreenshot("loginAndAssertDashboard_Failure");
            Reporter.log("Test case asserting Dashboard Screen Failed");
            throw e;
        }
    }
    
    public void loginAndAssertErrorMessage(String username, String password, String expectedErrorMessage) {
        userNameDOM.sendKeys(username);
        passwordDOM.sendKeys(password);
        loginButton.click();
        takeScreenshot("loginAndAssertErrorMessage_AfterClickLogin");

        try {
            Assert.assertEquals(getErrorMessage(), expectedErrorMessage, "Error message does not match");
            takeScreenshot("loginAndAssertErrorMessage_Success");
            System.out.println("Test case asserting Invalid credentials message Passed");
            Reporter.log("Test case asserting Invalid credentials message Passed");
        } catch (AssertionError | Exception e) {
            takeScreenshot("loginAndAssertErrorMessage_Failure");
            Reporter.log("Test case asserting Invalid credentials message Failed");
            throw e;
        }
    }
    
    public void loginAndAssertEmptyCredentials(String expectedRequiredMessage) {
        userNameDOM.sendKeys("");
        passwordDOM.sendKeys("");
        loginButton.click();
        takeScreenshot("loginAndAssertEmptyCredentials_AfterClickLogin");
        
        try {
            Assert.assertEquals(getRequiredMessage(), expectedRequiredMessage, "Required message does not match");
            takeScreenshot("loginAndAssertEmptyCredentials_Success");
            System.out.println("Test case asserting Empty Credentials Passed");
            Reporter.log("Test case asserting Empty Credentials Passed");
        } catch (AssertionError | Exception e) {
            takeScreenshot("loginAndAssertEmptyCredentials_Failure");
            Reporter.log("Test case asserting Empty Credentials Failed");
            throw e;
        }
    }

    // Method to capture screenshots
    @Override
    public void takeScreenshot(String fileName) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String filePath = System.getProperty("user.dir") + "/screenshots/" + fileName + "_" + timestamp + ".png";

        try {
            FileHandler.copy(screenshot, new File(filePath));
            System.out.println("Screenshot saved: " + filePath);
        } catch (IOException e) {
            System.out.println("Failed to save screenshot: " + e.getMessage());
        }
    }
}
