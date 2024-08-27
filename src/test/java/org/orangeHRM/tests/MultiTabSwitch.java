package org.orangeHRM.tests;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;

public class MultiTabSwitch {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.google.com");
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get("https://doodles.google/");
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get("https://www.google.com/maps");
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        for (String handle : tabs) {
            driver.switchTo().window(handle);
            if (driver.getTitle().contains("Google Maps")) {
                System.out.println("Switched to Google Maps by Title");
                break;
            }
        }
        for (String handle : tabs) {
            driver.switchTo().window(handle);
            if (driver.getCurrentUrl().contains("https://doodles.google/")) {
            	System.out.println("Switched to Google Doodle by URL");
                break;
            }
        }
    }
}
