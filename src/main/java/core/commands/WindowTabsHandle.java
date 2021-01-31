package core.commands;

import core.browser.runner.WebDriverRunner;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class WindowTabsHandle {
    private static final Logger logger = LoggerFactory.getLogger(WindowTabsHandle.class);
    private WebDriver driver;
    public static WindowTabsHandle WindowTabsHandle =
            ThreadLocal.withInitial(WindowTabsHandle::new).get();

    private WindowTabsHandle() {
        if (WindowTabsHandle != null) {
            logger.error("Use WindowTabsHandle variable to get the single instance of this class.");
            throw new RuntimeException("Use WindowTabsHandle variable to get the single instance of this class.");
        }
    }

    private void setDriverSession() {
        driver = WebDriverRunner.getInstance().getWebDriver();
    }

    public String getCurrentWindowHandle() {
        setDriverSession();
        String currentWindowHandle = driver.getWindowHandle();
        logger.info(String.format("Get Current Windows Handle: current window as: '%s'", currentWindowHandle));
        return currentWindowHandle;
    }

    public Set<String> getCurrentWindowHandles() {
        setDriverSession();
        Set<String> windowHandles = driver.getWindowHandles();
        logger.info(String.format("Get Current Windows Handles: all current windows as: '%s'", windowHandles));
        return windowHandles;
    }

    public void closeCurrentWindow() {
        WebDriverRunner.getInstance().closeWindow();
        logger.info("Close Current Windows: close current windows");
    }

    public void closeAllChildWindows() {
        setDriverSession();
        String parent = getCurrentWindowHandle();
        Set<String> allWindows = getCurrentWindowHandles();
        allWindows.remove(parent);
        if (!allWindows.isEmpty()) {
            allWindows.forEach(window -> driver.switchTo().window(window).close());
        }
        driver.switchTo().window(parent);
        logger.info(String.format("Close All Child Windows: close all child windows and switch to parent window with id '%s'", parent));
    }

    public void switchToWindow(String windowId) {
        setDriverSession();
        driver.switchTo().window(windowId);
        logger.info(String.format("Switch To Window: switch to window with id '%s'", windowId));
    }
}
