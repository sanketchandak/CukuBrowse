package core.web.commands;

import core.web.browser.runner.WebDriverRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.KeyEvent;

import static core.web.commands.ExecuteJavascript.ExecuteJavascript;

public class SetBrowserZoomPercentage {
    private static final Logger logger = LoggerFactory.getLogger(SetBrowserZoomPercentage.class);
    private WebDriver driver;
    public static SetBrowserZoomPercentage SetBrowserZoomPercentage =
            ThreadLocal.withInitial(SetBrowserZoomPercentage::new).get();

    private SetBrowserZoomPercentage() {
        if (SetBrowserZoomPercentage != null) {
            logger.error("Use SetBrowserZoomPercentage variable to get the single instance of this class.");
            throw new RuntimeException("Use SetBrowserZoomPercentage variable to get the single instance of this class.");
        }
    }

    private void setDriverSession() {
        driver = WebDriverRunner.getInstance().getWebDriver();
    }

    public void setBrowserZoomPercentage(int browserZoomLevel) {
        ExecuteJavascript.executeJavascript("document.body.style.zoom='" + browserZoomLevel + "%'");
        logger.info(String.format("Set Browser Zoom Percentage: set browser percentage to '%s'", browserZoomLevel));
    }

    public void resetBrowserZoomLevel() {
        setDriverSession();
        String browserName = ((RemoteWebDriver) driver).getCapabilities().getBrowserName().toLowerCase();
        if (browserName.contains("chrome")) {
            ExecuteJavascript.executeJavascript("document.body.style.zoom='100%'");
        } else {
            try {
                Robot robot = new Robot();
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_0);
                robot.keyRelease(KeyEvent.VK_CONTROL);
                robot.keyRelease(KeyEvent.VK_0);
            } catch (AWTException ignore) {
            }
        }
        logger.info("Reset Browser Zoom level: reset browser percentage to 100%");
    }

}
