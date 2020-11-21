package core.commands;

import core.browser.runner.WebDriverRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BrowserAlert {
    public static BrowserAlert BrowserAlert =
            ThreadLocal.withInitial(BrowserAlert::new).get();
    private WebDriver driver;

    private BrowserAlert() {
        if (BrowserAlert != null) {
            throw new RuntimeException("Use BrowserAlert variable to get the single instance of this class.");
        }
    }

    private void setDriverSession() {
        driver = WebDriverRunner.getInstance().getWebDriver();
    }

    public String acceptAlert() {
        setDriverSession();
        String alertMsg = getAlertMessage();
        driver.switchTo().alert().accept();
        return alertMsg;
    }

    public String acceptAlert(String expectedDialogText) throws Exception {
        if (getAlertMessage().equalsIgnoreCase(expectedDialogText.trim())) {
            return acceptAlert();
        } else {
            throw new Exception("DialogTextMismatch");
        }
    }

    public String dismissAlert() {
        setDriverSession();
        String alertMsg = getAlertMessage();
        driver.switchTo().alert().dismiss();
        return alertMsg;
    }

    public String dismissAlert(String expectedDialogText) throws Exception {
        if (getAlertMessage().equalsIgnoreCase(expectedDialogText.trim())) {
            return dismissAlert();
        } else {
            throw new Exception("DialogTextMismatch");
        }
    }

    public String getAlertMessage() {
        setDriverSession();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        //Wait for the alert to be displayed
        return wait.until(ExpectedConditions.alertIsPresent()).getText();
    }
}
