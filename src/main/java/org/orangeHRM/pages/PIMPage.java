package org.orangeHRM.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.orangeHRM.AbstractComponents.AbstractComponents;
import org.testng.Reporter;

public class PIMPage extends AbstractComponents {
    WebDriver driver;

    // Constants for hardcoded values
    private static final String JOB_LOCATION_CLASS_NAME = "oxd-form-loader";
    private static final String JOB_LOCATION_XPATH = "(//div[@class='oxd-select-text-input'][normalize-space()='-- Select --'])[4]";
    private static final String JOB_LOCATION_PATH = "//span[normalize-space()='Texas R&D']";
    private static final String SEARCH_EMPLOYEE_XPATH = "(//span[@class='oxd-text oxd-text--span'])[1]";
    private static final String EMPLOYEE_LIST_CLASS_NAME = "oxd-table-card";

    // XPath for employee details row
    private static final String EMPLOYEE_DETAILS_XPATH = "//div[@role='row' and .//div[contains(text(),'%s')] and .//div[contains(text(),'%s')] and .//div[contains(text(),'%s')]]";

    // Selectors
    By dashboardTitleBy = By.xpath("//h6[normalize-space()='Dashboard']");
    By pimElementBy = By.xpath("//span[normalize-space()='PIM']");
    By PIMTitleBy = By.xpath("//h6[normalize-space()='PIM']");
    By addEmployeeBy = By.xpath("//a[normalize-space()='Add Employee']");
    By AddEmployeeTitleBy = By.xpath("//h6[normalize-space()='Add Employee']");
    By firstNameDOMBy = By.xpath("//input[@placeholder='First Name']");
    By lastNameDOMBy = By.xpath("//input[@placeholder='Last Name']");
    By empIdDOMBy = By.xpath("//label[text()='Employee Id']/parent::div/following-sibling::div//input");
    By saveEmployeeBtn = By.xpath("//button[normalize-space()='Save']");
    By clickJobBy = By.xpath("//a[normalize-space()='Job']");
    By jobTitleBy = By.xpath("//h6[normalize-space()='Job Details']");
    By saveJobLocationBy = By.xpath("//button[normalize-space()='Save']");
    By employeeListBy = By.xpath("//a[normalize-space()='Employee List']");
    By employeeInformationTitleBy = By.xpath("//h5[normalize-space()='Employee Information']");
    By employeeNameDOMBy = By.xpath("(//input[@placeholder='Type for hints...'])[1]");
    By searchEmployeeBtn = By.xpath("//button[normalize-space()='Search']");

    public PIMPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void assertDashboard(String expectedURL) {
        waitForElementToAppear(dashboardTitleBy);
        takeScreenshot("Dashboard_Screenshot");
        getCurrentURL(expectedURL);
    }

    public void goToPIM() {
        clickElement(pimElementBy);
        takeScreenshot("GoToPIM_Screenshot");
    }

    public void assertPIMScreen(String expectedURL) {
        waitForElementToAppear(PIMTitleBy);
        takeScreenshot("PIMScreen_Screenshot");
        getCurrentURL(expectedURL);
    }

    public void goToAddEmployee() {
        clickElement(addEmployeeBy);
        takeScreenshot("GoToAddEmployee_Screenshot");
    }

    public void assertAddEmployeeScreen(String expectedURL) {
        waitForElementToAppear(AddEmployeeTitleBy);
        takeScreenshot("AddEmployeeScreen_Screenshot");
        getCurrentURL(expectedURL);
    }

    public void assertFirstNameTextInputAndEnterFirstName(String firstName) {
        assertTextInputAndEnterValue(firstNameDOMBy, firstName);
        takeScreenshot("FirstNameTextInput_Screenshot");
    }

    public void assertLastNameTextInputAndEnterLastName(String lastName) {
        assertTextInputAndEnterValue(lastNameDOMBy, lastName);
        takeScreenshot("LastNameTextInput_Screenshot");
    }

    public void assertEmpIdTextInputAndEnterEmpId(String empId) {
        assertTextInputAndEnterValue(empIdDOMBy, empId);
        takeScreenshot("EmpIdTextInput_Screenshot");
    }

    public void saveEmployeeDetails() {
        clickElement(saveEmployeeBtn);
        takeScreenshot("SaveEmployeeDetails_Screenshot");
    }

    public By getFullNameTitleBy(String path, String fullName) {
        return By.xpath(String.format(path, fullName));
    }

    public void assertFullNameTitle(String path, String fullName) {
        By fullNameTitleBy = getFullNameTitleBy(path, fullName);
        waitForElementToAppear(fullNameTitleBy);
        takeScreenshot("FullNameTitle_Screenshot");
        assertTitle(fullNameTitleBy, fullName);
    }

    public void goToJob() {
        clickElement(clickJobBy);
        takeScreenshot("GoToJob_Screenshot");
    }

    public void assertJobDetailsForm() {
        waitForElementToAppear(jobTitleBy);
        takeScreenshot("JobDetailsForm_Screenshot");
    }

    public void selectJobLocation(String jobLocation) {
        selectValueFromTheList(JOB_LOCATION_CLASS_NAME, JOB_LOCATION_XPATH, JOB_LOCATION_PATH, jobLocation);
        takeScreenshot("JobLocation_Screenshot");
    }

    public void saveJobLocation() {
        clickElement(saveJobLocationBy);
        takeScreenshot("SaveJobLocation_Screenshot");
    }

    public void goToEmployeeList() {
        clickElement(employeeListBy);
        takeScreenshot("GoToEmployeeList_Screenshot");
    }

    public void assertEmployeeListTitle() {
        waitForElementToAppear(employeeInformationTitleBy);
        takeScreenshot("EmployeeListTitle_Screenshot");
    }

    public void openToggle() {
        openToggleButton();
        takeScreenshot("OpenToggle_Screenshot");
    }

    public void assertEmployeeNameTextInputAndEnterFirstName(String firstName) {
        assertTextInputAndEnterValue(employeeNameDOMBy, firstName);
        takeScreenshot("EmployeeNameTextInput_Screenshot");
    }

    public void searchEmployeeDetails() {
        clickElement(searchEmployeeBtn);
        waitForLoadingToComplete();
        takeScreenshot("SearchEmployeeDetails_Screenshot");
    }

    public void assertSearchByEmployeeNameFound() {
        assertRecordsFound(SEARCH_EMPLOYEE_XPATH);
        takeScreenshot("SearchEmployeeNameFound_Screenshot");
    }

    public void employeeListVisible() {
        isGridVisible(EMPLOYEE_LIST_CLASS_NAME);
        takeScreenshot("EmployeeListVisible_Screenshot");
    }

    public void employeeDetailsMatching(String firstName, String lastName, String empID) {
        // Using the formatted XPath
        String empMatchxpath = String.format(EMPLOYEE_DETAILS_XPATH, empID, firstName, lastName);
        WebElement row = driver.findElement(By.xpath(empMatchxpath));

        if (row != null) {
            System.out.println("Row with the specified empID, firstName, and lastName exists.");
            Reporter.log("TEST PASSED: " + firstName + " " + lastName + " with Employee ID " + empID + " is available in the Employee List!!!");
            System.out.println("TEST PASSED: " + firstName + " " + lastName + " with Employee ID " + empID + " is available in the Employee List!!!");
            takeScreenshot("EmployeeDetailsMatching_Screenshot");
        } else {
            System.out.println("TEST FAILED: " + firstName + " " + lastName + " with Employee ID " + empID + " is NOT AVAILABLE in the Employee List!!!");
            Reporter.log("");
            takeScreenshot("EmployeeDetailsNotFound_Screenshot");
        }
    }
}
