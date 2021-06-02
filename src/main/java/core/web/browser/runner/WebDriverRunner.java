package core.web.browser.runner;

import core.web.browser.driver.impl.WebDriverContainer;
import core.web.browser.driver.impl.WebDriverThreadLocalContainer;
import core.web.browser.manager.impl.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;
import utils.CukeBrowseException;

public class WebDriverRunner {
    private volatile static WebDriverRunner webDriverRunner;
    private final WebDriverContainer webdriverContainer = new WebDriverThreadLocalContainer();


    private WebDriverRunner() {
        if (webDriverRunner != null) {
            throw new CukeBrowseException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static WebDriverRunner getInstance() {
        if (webDriverRunner == null) {
            synchronized (WebDriverRunner.class) {
                if (webDriverRunner == null) {
                    webDriverRunner = new WebDriverRunner();
                }
            }
        }
        return webDriverRunner;
    }

    /**
     * Use this method BEFORE opening a browser to add custom event listeners to WebDriver.
     *
     * @param listener your listener of WebDriver events
     */
    public void addListener(WebDriverEventListener listener) {
        webdriverContainer.addListener(listener);
    }

    /**
     * Set the underlying instance of Selenium WebDriver.
     */
    public WebDriver setWebDriver(BrowserType browserType) {
        DriverManager driverManager;
        switch (browserType) {
            case CHROME: {
                driverManager = new ChromeDriverManager();
                break;
            }
            case FIREFOX: {
                driverManager = new FirefoxDriverManager();
                break;
            }
            case IE: {
                driverManager = new IEDriverManager();
                break;
            }
            case SAFARI: {
                driverManager = new SafariDriverManager();
                break;
            }
            case OPERA: {
                driverManager = new OperaDriverManager();
                break;
            }
            case EDGE: {
                driverManager = new EdgeDriverManager();
                break;
            }
            default: {
                throw new CukeBrowseException("Unknown Browser Type: " + browserType);
            }
        }
        return webdriverContainer.setWebDriver(driverManager.createDriver());
    }

    /**
     * Set the underlying instance of Selenium WebDriver.
     */
    public void setWebDriver(WebDriver driver) {
        webdriverContainer.setWebDriver(driver);
    }

    /**
     * Get the underlying instance of Selenium WebDriver.
     * This can be used for any operations directly with WebDriver.
     */
    public WebDriver getWebDriver() {
        return webdriverContainer.getWebDriver();
    }

    /**
     * Close the current window, quitting the browser if it's the last window currently open.
     *
     * @see WebDriver#close()
     */
    public void closeWindow() {
        webdriverContainer.closeWindow();
    }

    /**
     * <p>Close the browser if it's open.</p>
     * <br>
     * <p>NB! Method quits this driver, closing every associated window.</p>
     *
     * @see WebDriver#quit()
     */
    public void closeWebDriver() {
        webdriverContainer.closeWebDriver();
    }

    /**
     * @return true if instance of Selenium WebDriver is started in current thread
     */
    public boolean hasWebDriverStarted() {
        return webdriverContainer.hasWebDriverStarted();
    }

    /**
     * Delete all the browser cookies
     */
    public void clearBrowserCache() {
        webdriverContainer.clearBrowserCache();
    }

    /**
     * Set browser size
     */
    public void setWindowSize(int width, int height) {
        webdriverContainer.setWindowSize(width, height);
    }

    /**
     * Maximize Browser
     */
    public void maximizeBrowser() {
        webdriverContainer.maximizeBrowser();
    }

    /**
     * Get browser size
     */
    public Dimension getWindowSize() {
        return webdriverContainer.getWindowSize();
    }
}
