package org.orangeHRM.AbstractComponents;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import io.netty.handler.timeout.TimeoutException;

public class AbstractComponents {
    private static final Duration SHORT_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration LONG_TIMEOUT = Duration.ofSeconds(15);

    WebDriver driver;

    public AbstractComponents(WebDriver driver) {
        this.driver = driver;
    }
    
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
    
    public void waitForElementToAppear(By findBy) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, LONG_TIMEOUT);
            wait.until(ExpectedConditions.visibilityOfElementLocated(findBy));
            Reporter.log("Waited for the Element and Located the element "+findBy);
            System.out.println("Element located: " + findBy);
        } catch (TimeoutException e) {
        	Reporter.log("Waited for the Element and "+ findBy+ " is not Located ");
            System.out.println("Timeout waiting for element: " + findBy);
            throw e;
        }
    }

    public WebElement waitAndGetElement(By findBy) {
        waitForElementToAppear(findBy);
        return driver.findElement(findBy);
    }

    public void clickElement(By findBy) {
        WebElement domElement = waitAndGetElement(findBy);
        String domName = domElement.getText();
        domElement.click();
        System.out.println(domName + " is clicked !!");
        Reporter.log(domName + " is clicked !!");
        Reporter.log("Waiting for the Element until its loading ");
        System.out.println("Please wait until it's Loading...");
    }

    public void getCurrentURL(String expectedURL) {
        String actualURL = driver.getCurrentUrl();
        Assert.assertEquals(actualURL, expectedURL, "URL does not match: expected " + expectedURL + " but found " + actualURL);
        System.out.println("URL matches as expected.");
        Reporter.log("URL matches as expected.");
    }

    public void navigateToPage(String landingURL) {
        driver.get(landingURL);
    }

    public void assertTextInputAndEnterValue(By findBy, String valueToBeEntered) {
        WebElement domElement = waitAndGetElement(findBy);
        Assert.assertTrue(domElement.isDisplayed(), "Input element is not displayed.");
        domElement.sendKeys(Keys.CONTROL + "a"); // Select all text
        domElement.sendKeys(Keys.BACK_SPACE);
        domElement.sendKeys(valueToBeEntered);
        Assert.assertTrue(domElement.isEnabled(), "Input element is not enabled after entering value.");
        System.out.println(valueToBeEntered + " Value Entered");
        Reporter.log(valueToBeEntered + " Value Entered");
    }

    public void assertTitle(By findBy, String fullName) {
        WebElement domElement = waitAndGetElement(findBy);
        Assert.assertTrue(domElement.isDisplayed(), "Element was not displayed.");
        System.out.println("Found element with text: " + domElement.getText());
        Reporter.log("Found the Element "+ findBy+ " with text "+fullName);
    }
    
    public void selectValueFromTheList(String classNameVariable, String xpathValue, String locationPath, String jobLocation ) {
        WebDriverWait wait = new WebDriverWait(driver, LONG_TIMEOUT);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className(classNameVariable)));
        WebElement dropdown = driver.findElement(By.xpath(xpathValue));
        dropdown.click();
        Reporter.log("Job Location Dropdown is Clicked");
        WebElement option = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locationPath)));
        option.click();
        Reporter.log("Option "+jobLocation+ " is Selected");
        String selectedValue = dropdown.getText();
        Assert.assertEquals(selectedValue, jobLocation, "The selected value is not " + jobLocation + ", it's: " + selectedValue);
        System.out.println("Assertion Passed: The selected value is " + jobLocation);
        Reporter.log("Asserting Selected option: "+jobLocation+" Passed");
    }

    public void selectValueFromDropDown(String dropdownXPath, String dropdownClassName, String optionXPath, String expectedValue) {
        WebDriverWait wait = new WebDriverWait(driver, LONG_TIMEOUT);
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dropdownXPath)));
        dropdown.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(dropdownClassName)));
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(optionXPath)));
        option.click();
        String selectedValue = dropdown.getText();
        Assert.assertEquals(selectedValue, expectedValue, "The selected value is not " + expectedValue + ", it's: " + selectedValue);
        System.out.println("Assertion Passed: The selected value is " + expectedValue);
        Reporter.log("Assertion Passed: The selected value is " + expectedValue);
    }

    public boolean isGridVisible(String classNameVariable) {
        WebDriverWait wait = new WebDriverWait(driver, LONG_TIMEOUT);
        By gridLocator = By.className(classNameVariable);
        try {
            WebElement gridElement = wait.until(ExpectedConditions.visibilityOfElementLocated(gridLocator));
            System.out.println("The grid is visible.");
            Reporter.log("The grid with Employee details is VISIBLE.");
            return gridElement.isDisplayed();
        } catch (TimeoutException e) {
        	Reporter.log("The grid with Employee details is NOT VISIBLE.");
            System.out.println("The grid is not visible.");
            return false;
        }
    }

    public void assertRecordsFound(String xPathVariable) {
        WebElement recordCountElement = driver.findElement(By.xpath(xPathVariable));
        String recordCountText = recordCountElement.getText();
        Assert.assertFalse(recordCountText.contains("No Records Found"), "Test Failed: " + recordCountText);
        System.out.println("Test Passed: " + recordCountText);
        Reporter.log("Test Passed: " + recordCountText);
    }

    public void waitForLoadingToComplete() {
        By loadingIndicator = By.xpath("//div[contains(@class, 'loading-spinner')]");
        try {
            WebDriverWait wait = new WebDriverWait(driver, SHORT_TIMEOUT);
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingIndicator));
            System.out.println("Loading completed.");
            Reporter.log("Loading Completed.");
        } catch (TimeoutException e) {
            System.out.println("Timeout waiting for loading to complete.");
            Reporter.log("Timeout waiting for loading to complete.");
            throw e;
        }
    }

    public boolean isEmployeeInList(String fullName, By gridItemsLocator, By headerLocator) {
        WebDriverWait wait = new WebDriverWait(driver, SHORT_TIMEOUT);
        List<WebElement> gridItems = driver.findElements(gridItemsLocator);
        for (WebElement item : gridItems) {
            try {
                WebElement header = wait.until(ExpectedConditions.visibilityOf(item.findElement(headerLocator)));
                String nameText = header.getText().trim();
                if (nameText.equalsIgnoreCase(fullName)) {
                    System.out.println("Employee found: " + nameText);
                    Reporter.log("Employee found: " + nameText);
                    return true;
                }
            } catch (Exception e) {
                System.out.println("Failed to locate header element within grid item: " + e.getMessage());
                Reporter.log("Failed to locate header element within grid item: " + e.getMessage());
            }
        }
        System.out.println("Employee not found: " + fullName);
        Reporter.log("Employee not found: " + fullName);
        return false;
    }

    public void openToggleButton() {
        WebDriverWait wait = new WebDriverWait(driver, SHORT_TIMEOUT);
        By toggleButtonLocator = By.xpath("//div[@class='--toggle']//button[@class='oxd-icon-button']");
        By toggleIconLocator = By.xpath("//div[@class='--toggle']//i");

        WebElement toggleButton = wait.until(ExpectedConditions.elementToBeClickable(toggleButtonLocator));
        WebElement toggleIcon = driver.findElement(toggleIconLocator);
        String iconClass = toggleIcon.getAttribute("class");

        if (iconClass.contains("bi-caret-down-fill")) {
            toggleButton.click();
            System.out.println("Toggle was closed. Opened the toggle.");
            Reporter.log("Toggle was closed. Opened the toggle.");
            wait.until(ExpectedConditions.attributeToBe(toggleIconLocator, "class", "oxd-icon bi-caret-up-fill"));
            System.out.println("Verified: The toggle is now open.");
            Reporter.log("Verified: The toggle is now open.");
        } else if (iconClass.contains("bi-caret-up-fill")) {
            System.out.println("Toggle is already open.");
            Reporter.log("Toggle is already open.");
        } else {
            Assert.fail("Unexpected state for the toggle button.");
        }
    }

    public void checkAndSearchAgain(WebDriver driver2, By recordsFoundSpan, By gridItemsVerify, By searchButton) {
        WebElement recordCountElement = driver2.findElement(recordsFoundSpan);
        String recordCountText = recordCountElement.getText();
        int recordCount = Integer.parseInt(recordCountText.replaceAll("[^0-9]", ""));
        List<WebElement> gridItems = driver2.findElements(gridItemsVerify);
        int gridCount = gridItems.size();

        if (recordCount != gridCount) {
            System.out.println("Record count mismatch: found " + gridCount + " items, expected " + recordCount);
            Reporter.log("Record count mismatch: found " + gridCount + " items, expected " + recordCount);
            WebElement searchBtn = waitAndGetElement(searchButton);
            searchBtn.click();
            System.out.println("Clicked the search button to reload the results.");
            Reporter.log("Clicked the search button to reload the results.");
            waitForLoadingToComplete();
            checkAndSearchAgain(driver2, recordsFoundSpan, gridItemsVerify, searchButton);  // Recursive call
        } else {
            System.out.println("Record count matches: found " + gridCount + " items.");
            Reporter.log("Record count matches: found " + gridCount + " items.");
        }
    }
}
