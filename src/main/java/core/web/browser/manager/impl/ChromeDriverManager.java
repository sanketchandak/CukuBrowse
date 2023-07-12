package core.web.browser.manager.impl;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CukeBrowseException;

public class ChromeDriverManager implements DriverManager {
    private static final Logger logger = LoggerFactory.getLogger(ChromeDriverManager.class);

    /**
     * provided link can be referred for ChromeOptions
     * https://chromium.googlesource.com/chromium/src/+/master/chrome/common/chrome_switches.cc
     */
    @Override
    public WebDriver createDriver() {
        try {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("test-type");
            /* Start Chrome maximized */
            options.addArguments("start-maximized");
            /* Enable below to Opens Chrome in headless mode */
            //options.addArguments("-headless");
            /* Enable below to Opens Chrome in incognito mode */
            //options.addArguments("incognito");
            /* Disables existing extensions on Chrome browser */
            options.addArguments("disable-extensions");
            /* Disables pop-ups displayed on Chrome browser */
            options.addArguments("disable-popup-blocking");
            /*DesiredCapabilities capabilities = new DesiredCapabilities();
            *//* Set the pre-defined capability – ACCEPT_SSL_CERTS value to true *//*
            capabilities.setCapability("acceptSslCerts", true);
            *//* merge options and capability *//*
            options = options.merge(capabilities);*/
            logger.info("Create WebDriver: Chrome Driver created successfully.");
            return new ChromeDriver(options);
        } catch (Exception e) {
            logger.error("Create WebDriver: Chrome Driver creation failed due to Exception: " + e);
            throw new CukeBrowseException("Create WebDriver: Chrome Driver creation failed due to Exception:", e);
        }
    }
}
