package org.orangeHRM.tests;

import java.io.IOException;
import org.openqa.selenium.By;
import org.orangeHRM.TestComponents.BaseTest;
import org.testng.annotations.Test;

public class VerifyEmployeeTest extends BaseTest {
    private static final String DIRECTORY_CARD_HEADER_KEY = "DIRECTORY_CARD_HEADER";
    private static final String DIRECTORY_CONTAINER_KEY = "DIRECTORY_CONTAINER";
    private static final String RECORDS_FOUND_SPAN_KEY = "RECORDS_FOUND_SPAN";
    private static final String GRID_ITEMS_VERIFY_KEY = "GRID_ITEMS_VERIFY";
    private static final String SEARCH_BUTTON_KEY = "SEARCH_BUTTON";

    @Test
    public void VerifyEmployeeDetail() throws IOException {
        String baseUrl = env.getProperty("BASE_URL");
        String loginUrl = env.getProperty("LOGIN_URL");
        String dashboardUrl = env.getProperty("DASHBOARD_URL");
        String directoryUrl = env.getProperty("DIRECTORY_URL");
        String adminUsername = env.getProperty("ADMIN_USERNAME");
        String adminPassword = env.getProperty("ADMIN_PASSWORD");

        String firstName = staticData.getProperty("EMPLOYEE_FIRST_NAME");
        String lastName = staticData.getProperty("EMPLOYEE_LAST_NAME");
        String employeeFullName = firstName + " " + lastName;
        String jobLocation = staticData.getProperty("JOB_LOCATION");

        loginPage.loginOrangeHRM(adminUsername, adminPassword);

        dirPage.assertDashboard(dashboardUrl);
        dirPage.goToDirectory();
        dirPage.assertDirectoryScreen(directoryUrl);

        dirPage.openToggleButton();
        dirPage.selectJobLocation(jobLocation);
        dirPage.searchEmployeeDetails();
        dirPage.assertSearchByLocationFound();
        dirPage.employeeListVisible();
        abstractComponents.checkAndSearchAgain(
            driver,
            getSelector(RECORDS_FOUND_SPAN_KEY),
            getSelector(GRID_ITEMS_VERIFY_KEY),
            getSelector(SEARCH_BUTTON_KEY)
        );
        dirPage.verifyEmployeeInDirectory(
            getSelector(DIRECTORY_CONTAINER_KEY),
            getSelector(DIRECTORY_CARD_HEADER_KEY),
            employeeFullName
        );
    }
}
