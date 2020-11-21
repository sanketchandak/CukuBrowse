package core.browser.manager.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SafariDriverManager implements DriverManager {
    private static final Logger logger = LoggerFactory.getLogger(SafariDriverManager.class);

    @Override
    public WebDriver createDriver() {
        try {
            SafariOptions options = new SafariOptions();
            options.setCapability("safari.cleanSession", true);
            DesiredCapabilities capabilities = DesiredCapabilities.safari();
            capabilities.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
            capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            /* merge options and capability */
            options.merge(capabilities);
            logger.info("Create Web Driver: Safari Driver created successfully.");
            return new SafariDriver(options);
        } catch (Exception e) {
            logger.error("Create Web Driver: Web Driver creation failed due to " + e.toString(), e);
            e.printStackTrace();
        }
        return null;
    }
}
