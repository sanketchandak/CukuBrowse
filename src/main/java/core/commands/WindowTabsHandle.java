package core.commands;

import core.browser.runner.WebDriverRunner;
import org.openqa.selenium.WebDriver;

import java.util.Set;

public class WindowTabsHandle {
    private WebDriver driver;
    public static WindowTabsHandle WindowTabsHandle =
            ThreadLocal.withInitial(WindowTabsHandle::new).get();

    private WindowTabsHandle() {
        if (WindowTabsHandle != null) {
            throw new RuntimeException("Use WindowTabsHandle variable to get the single instance of this class.");
        }
    }

    private void setDriverSession() {
        driver = WebDriverRunner.getInstance().getWebDriver();
    }

    public String getCurrentWindowHandle() {
        setDriverSession();
        return driver.getWindowHandle();
    }

    public Set<String> getCurrentWindowHandles() {
        setDriverSession();
        return driver.getWindowHandles();
    }

    public void closeCurrentWindow() {
        WebDriverRunner.getInstance().closeWindow();
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
    }

    public void switchToWindow(String windowId) {
        setDriverSession();
        driver.switchTo().window(windowId);
    }
}
