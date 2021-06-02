package core.web.commands;

import core.web.browser.runner.WebDriverRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CukeBrowseException;

public class BrowserAlert {
    private static final Logger logger = LoggerFactory.getLogger(BrowserAlert.class);
    public static BrowserAlert BrowserAlert =
            ThreadLocal.withInitial(BrowserAlert::new).get();
    private WebDriver driver;

    private BrowserAlert() {
        if (BrowserAlert != null) {
            logger.error("Use BrowserAlert variable to get the single instance of this class.");
            throw new CukeBrowseException("Use BrowserAlert variable to get the single instance of this class.");
        }
    }

    private void setDriverSession() {
        driver = WebDriverRunner.getInstance().getWebDriver();
    }

    public String acceptAlert() {
        setDriverSession();
        String alertMsg = getAlertMessage();
        driver.switchTo().alert().accept();
        logger.info("Accept Alert: Driver switch to alert and accept it.");
        return alertMsg;
    }

    public String acceptAlert(String expectedDialogText) {
        if (getAlertMessage().equalsIgnoreCase(expectedDialogText.trim())) {
            return acceptAlert();
        } else {
            logger.error(String.format("Accept Alert: Alert with text '%s' is not present to accept.", expectedDialogText));
            throw new CukeBrowseException(String.format("Accept Alert: Alert with text '%s' is not present to accept.", expectedDialogText));
        }
    }

    public String dismissAlert() {
        setDriverSession();
        String alertMsg = getAlertMessage();
        driver.switchTo().alert().dismiss();
        logger.info("Dismiss Alert: Driver switch to alert and dismiss it.");
        return alertMsg;
    }

    public String dismissAlert(String expectedDialogText) {
        if (getAlertMessage().equalsIgnoreCase(expectedDialogText.trim())) {
            return dismissAlert();
        } else {
            logger.error(String.format("Dismiss Alert: Alert with text '%s' is not present to dismiss.", expectedDialogText));
            throw new CukeBrowseException(String.format("Dismiss Alert: Alert with text '%s' is not present to dismiss.", expectedDialogText));
        }
    }

    public String getAlertMessage() {
        setDriverSession();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        //Wait for the alert to be displayed
        String alertMessage = wait.until(ExpectedConditions.alertIsPresent()).getText();
        logger.info(String.format("Get Alert Message: Alert present with message '%s'.", alertMessage));
        return alertMessage;
    }
}
