package core.web.browser.manager.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CukeBrowseException;

public class SafariDriverManager implements DriverManager {
    private static final Logger logger = LoggerFactory.getLogger(SafariDriverManager.class);

    @Override
    public WebDriver createDriver() {
        try {
            SafariOptions options = new SafariOptions();
            options.setCapability("safari.cleanSession", true);
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("javascriptEnabled", true);
            capabilities.setCapability("acceptSslCerts", true);
            /* merge options and capability */
            options = options.merge(capabilities);
            logger.info("Create WebDriver: Safari Driver created successfully.");
            return new SafariDriver(options);
        } catch (Exception e) {
            logger.error("Create WebDriver: Safari Driver creation failed due to Exception: " + e);
            throw new CukeBrowseException("Create WebDriver: Safari Driver creation failed due to Exception:", e);
        }
    }
}
