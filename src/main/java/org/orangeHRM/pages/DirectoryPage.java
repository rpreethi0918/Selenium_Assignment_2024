package org.orangeHRM.pages;

import java.time.Duration;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.orangeHRM.AbstractComponents.AbstractComponents;
import org.testng.Reporter;

public class DirectoryPage extends AbstractComponents {
    WebDriver driver;

    private static final By dashboardTitleBy = By.xpath("//h6[normalize-space()='Dashboard']");
    private static final By dirElementBy = By.xpath("//span[normalize-space()='Directory']");
    private static final By DirectoryTitleBy = By.xpath("//h6[normalize-space()='Directory']");
    private static final By searchEmployeeBtn = By.xpath("//button[normalize-space()='Search']");
    private static final By recordFoundDOMBy = By.xpath("(//span[@class='oxd-text oxd-text--span'])[1]");
    private static final By gridItemsLocator = By.className("oxd-grid-item");
    private static final By headerLocator = By.className("orangehrm-directory-card-header");
    
    String searchByLocationXPathVariable = "(//span[@class='oxd-text oxd-text--span'])[1]";
    String selJobLocationDropdownXPath = "(//div[contains(text(),'-- Select --')])[2]";
    String selJobLocationDropdownClassName = "oxd-select-dropdown";
    String selJobLocationPath = "//span[normalize-space()='%s']";
    String empListClassNameVariable = "oxd-grid-4";

    @FindBy(xpath="//h6[normalize-space()='Dashboard']")
    WebElement dashboardTitle;

    @FindBy(xpath="//span[normalize-space()='Directory']")
    WebElement dirElement;

    @FindBy(xpath="//h6[normalize-space()='Directory']")
    WebElement DirectoryTitle;

    @FindBy(xpath="//button[normalize-space()='Search']")
    WebElement searchEmployee;

    @FindBy(xpath="(//span[@class='oxd-text oxd-text--span'])[1]")
    WebElement recordFoundDOM;

    public DirectoryPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void assertDashboard(String expectedURL) {
        waitForElementToAppear(dashboardTitleBy);
        getCurrentURL(expectedURL);
        takeScreenshot("assertDashboard");
    }

    public void goToDirectory() {
        clickElement(dirElementBy);
        takeScreenshot("goToDirectory");
    }

    public void assertDirectoryScreen(String expectedURL) {
        waitForElementToAppear(DirectoryTitleBy);
        getCurrentURL(expectedURL);
        takeScreenshot("assertDirectoryScreen");
    }

    public void openToggle() {
        openToggleButton();
        takeScreenshot("openToggle");
    }

    public void selectJobLocation(String jobLocation) {
        String locationPath = String.format(selJobLocationPath, jobLocation);
        selectValueFromDropDown(selJobLocationDropdownXPath, selJobLocationDropdownClassName, locationPath, jobLocation);
        takeScreenshot("selectJobLocation");
    }

    public void searchEmployeeDetails() {
        clickElement(searchEmployeeBtn);
        waitForLoadingToComplete();
        takeScreenshot("searchEmployeeDetails");
    }

    public void assertSearchByLocationFound() {
        assertRecordsFound(searchByLocationXPathVariable);
        takeScreenshot("assertSearchByLocationFound");
    }

    public void employeeListVisible() {
        isGridVisible(empListClassNameVariable);
        takeScreenshot("employeeListVisible");
    }

    public void assertEmployeeAvailabilityInList(String fullName) {
        boolean isFound = isEmployeeInList(fullName, gridItemsLocator, headerLocator);
        if (isFound) {
            System.out.println("Test Passed: Employee " + fullName + " is present in the directory.");
            Reporter.log("Asserted Employee record with Fullname: "+fullName+" in the directory.");
        } else {
            System.out.println("Test Failed: Employee " + fullName + " is not found in the directory.");
            Reporter.log("Test Failed: Employee " + fullName + " is not found in the directory.");
        }
        takeScreenshot("assertEmployeeAvailabilityInList_" + fullName);
    }

    public void verifyEmployeeInDirectory(By directoryContainer, By cardHeader, String fullName) {
        boolean employeeFound = false;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement container;

        try {
            container = wait.until(ExpectedConditions.visibilityOfElementLocated(directoryContainer));
        } catch (TimeoutException e) {
            takeScreenshot("verifyEmployeeInDirectory_Timeout");
            throw e;
        }

        List<WebElement> headers = container.findElements(cardHeader);

        for (WebElement header : headers) {
            String headerText = header.getText();
            if (headerText.equals(fullName)) {
                employeeFound = true;
                System.out.println("Found a card with name: " + fullName);
                Reporter.log("Found a card with name: " + fullName);
                System.out.println("TEST PASSED: Employee record " + fullName + " returned from the Directory successfully !!!");
                Reporter.log("TEST PASSED: Employee record " + fullName + " returned from the Directory successfully !!!");
                break;
            }
        }

        if (!employeeFound) {
            System.out.println("Employee not found after iterating through all headers.");
            Reporter.log("TEST FAILED: Employee "+fullName+ " not found after iterating through all headers.");
            takeScreenshot("verifyEmployeeInDirectory_NotFound_" + fullName);
        }
    }
}
