package org.orangeHRM.tests;

import java.io.IOException;
import org.orangeHRM.TestComponents.BaseTest;
import org.testng.annotations.Test;

public class AddEmployeeTest extends BaseTest {

    private static final String RECORDS_FOUND_SPAN_KEY = "RECORDS_FOUND_SPAN_ADD";
    private static final String GRID_ITEMS_KEY = "GRID_ITEMS_ADD";
    private static final String SEARCH_BUTTON_KEY = "SEARCH_BUTTON_ADD";

    @Test
    public void addEmployeeDetail() throws IOException {
        String baseUrl = env.getProperty("BASE_URL");
        String loginUrl = env.getProperty("LOGIN_URL");
        String dashboardUrl = env.getProperty("DASHBOARD_URL");
        String pimUrl = env.getProperty("PIM_URL");
        String addEmployeeUrl = env.getProperty("ADD_EMPLOYEE_URL");
        String adminUsername = env.getProperty("ADMIN_USERNAME");
        String adminPassword = env.getProperty("ADMIN_PASSWORD");

        String employeeFirstName = staticData.getProperty("EMPLOYEE_FIRST_NAME");
        String employeeLastName = staticData.getProperty("EMPLOYEE_LAST_NAME");
        String employeeId = staticData.getProperty("EMPLOYEE_ID");
        String employeeJobLocation = staticData.getProperty("JOB_LOCATION");
        String employeeFullName = employeeFirstName + " " + employeeLastName;
        String employeeFullNameDOM = selectors.getProperty("FULL_NAME_DOM");

        loginPage.loginOrangeHRM(adminUsername, adminPassword);

        pimPage.assertDashboard(dashboardUrl);
        pimPage.goToPIM();
        pimPage.assertPIMScreen(pimUrl);

        pimPage.goToAddEmployee();
        pimPage.assertAddEmployeeScreen(addEmployeeUrl);

        pimPage.assertFirstNameTextInputAndEnterFirstName(employeeFirstName);
        pimPage.assertLastNameTextInputAndEnterLastName(employeeLastName);
        pimPage.assertEmpIdTextInputAndEnterEmpId(employeeId);
        pimPage.saveEmployeeDetails();
        pimPage.assertFullNameTitle(employeeFullNameDOM, employeeFullName);

        pimPage.goToJob();
        pimPage.assertJobDetailsForm();
        pimPage.selectJobLocation(employeeJobLocation);
        pimPage.saveJobLocation();

        pimPage.goToEmployeeList();
        pimPage.assertEmployeeListTitle();
        pimPage.assertPIMScreen(pimUrl);

        pimPage.openToggleButton();
        pimPage.assertEmployeeNameTextInputAndEnterFirstName(employeeFirstName);
        pimPage.searchEmployeeDetails();
        pimPage.assertSearchByEmployeeNameFound();
        pimPage.employeeListVisible();

        abstractComponents.checkAndSearchAgain(
            driver,
            getSelector(RECORDS_FOUND_SPAN_KEY),
            getSelector(GRID_ITEMS_KEY),
            getSelector(SEARCH_BUTTON_KEY)
        );

        pimPage.employeeDetailsMatching(employeeFirstName, employeeLastName, employeeId);

        driver.quit();
    }
}
