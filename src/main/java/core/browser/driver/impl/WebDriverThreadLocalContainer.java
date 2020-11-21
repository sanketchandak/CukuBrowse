package core.browser.driver.impl;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Thread.currentThread;

public class WebDriverThreadLocalContainer implements WebDriverContainer {
    private static final Logger log = LoggerFactory.getLogger(WebDriverThreadLocalContainer.class);

    public List<WebDriverEventListener> listeners = new ArrayList<>();
    private final Map<Long, WebDriver> threadWebDriver = new ConcurrentHashMap<>(4);

    @Override
    public void addListener(WebDriverEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public WebDriver setWebDriver(WebDriver driver) {
        threadWebDriver.put(currentThread().getId(), addListeners(driver));
        return threadWebDriver.get(currentThread().getId());
    }

    @Override
    public WebDriver getWebDriver() {
        long threadId = currentThread().getId();
        if (!threadWebDriver.containsKey(threadId)) {
            throw new IllegalStateException("No webdriver is bound to current thread: " + threadId + ". You need to call open(url) first.");
        }
        return threadWebDriver.get(threadId);
    }

    @Override
    public void closeWindow() {
        getWebDriver().close();
    }

    @Override
    public void closeWebDriver() {
        long threadId = currentThread().getId();
        WebDriver driver = threadWebDriver.get(threadId);
        driver.close();
        threadWebDriver.remove(threadId);
        listeners.clear();
    }

    @Override
    public boolean hasWebDriverStarted() {
        WebDriver webDriver = threadWebDriver.get(currentThread().getId());
        return webDriver != null;
    }

    @Override
    public void clearBrowserCache() {
        if (hasWebDriverStarted()) {
            getWebDriver().manage().deleteAllCookies();
        }
    }

    @Override
    public void clearBrowserCookie(String cookieName) {
        getWebDriver().manage().deleteCookieNamed(cookieName);
    }

    @Override
    public void clearBrowserCookie(Cookie cookie) {
        getWebDriver().manage().deleteCookie(cookie);
    }

    @Override
    public void setWindowSize(int width, int height) {
        getWebDriver().manage().window().setSize(new Dimension(800, 480));
    }

    @Override
    public void maximizeBrowser() {
        getWebDriver().manage().window().maximize();
    }

    @Override
    public Dimension getWindowSize() {
        return getWebDriver().manage().window().getSize();
    }

    private WebDriver addListeners(WebDriver webDriver) {
        if (listeners.isEmpty()) {
            return webDriver;
        }
        EventFiringWebDriver wrapper = new EventFiringWebDriver(webDriver);
        for (WebDriverEventListener listener : listeners) {
            log.info("Add listner to webdriver: " + listener);
            wrapper.register(listener);
        }
        return wrapper;
    }
}
