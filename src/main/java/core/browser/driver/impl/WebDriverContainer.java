package core.browser.driver.impl;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;

public interface WebDriverContainer {

    void addListener(WebDriverEventListener listener);

    WebDriver setWebDriver(WebDriver driver);

    WebDriver getWebDriver();

    void closeWindow();

    void closeWebDriver();

    boolean hasWebDriverStarted();

    void clearBrowserCache();

    void clearBrowserCookie(String cookieName);

    void clearBrowserCookie(Cookie cookie);

    void setWindowSize(int width, int height);

    void maximizeBrowser();

    Dimension getWindowSize();
}
