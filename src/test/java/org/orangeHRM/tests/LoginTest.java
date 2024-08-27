package org.orangeHRM.tests;

import org.orangeHRM.TestComponents.BaseTest;
import org.orangeHRM.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.TestNG;
import org.testng.Reporter;

public class LoginTest extends BaseTest {

    private LoginPage loginPage;

    private String validUsername;
    private String validPassword;
    private String invalidUsername;
    private String invalidPassword;
    private String expectedDashboardUrl;
    private String expectedDashboardTitle;
    private String expectedErrorMessage;
    private String expectedRequiredMessage;

    @BeforeMethod
    public void setUp() {
        loginPage = new LoginPage(driver);

        validUsername = env.getProperty("ADMIN_USERNAME");
        validPassword = env.getProperty("ADMIN_PASSWORD");
        invalidUsername = env.getProperty("INVALID_USERNAME");
        invalidPassword = env.getProperty("INVALID_PASSWORD");
        expectedDashboardUrl = env.getProperty("DASHBOARD_URL");
        expectedDashboardTitle = staticData.getProperty("EXPECTED_DASHBOARD_TITLE");
        expectedErrorMessage = staticData.getProperty("EXPECTED_ERROR_MESSAGE");
        expectedRequiredMessage = staticData.getProperty("EXPECTED_REQUIRED_MESSAGE");

        loginPage.goTo();
        Reporter.log("The browser is opened and landed on OrangeHRM site");
    }

    @Test(priority = 1)
    public void loginWithValidCredentials() {
        loginPage.loginAndAssertDashboard(validUsername, validPassword, expectedDashboardUrl, expectedDashboardTitle);
        Reporter.log("Logged in Successfully with Valid Credentials");
    }

    @Test(priority = 2)
    
    public void loginWithInvalidCredentials() {
        loginPage.loginAndAssertErrorMessage(invalidUsername, invalidPassword, expectedErrorMessage);
        Reporter.log("Respective error message for Invalid Credentials displayed Successfully ");
    }

    @Test(priority = 3)
    public void loginWithEmptyCredentials() {
        loginPage.loginAndAssertEmptyCredentials(expectedRequiredMessage);
        Reporter.log("Required fileds message displayed, when empty text sent");
    }
}
