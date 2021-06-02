package core.web.browser.manager.impl;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CukeBrowseException;

public class FirefoxDriverManager implements DriverManager {
    private static final Logger logger = LoggerFactory.getLogger(FirefoxDriverManager.class);

    /**
     * provided link can be referred for FirefoxProfile
     * http://kb.mozillazine.org/About:config_entries
     */
    @Override
    public WebDriver createDriver() {
        try {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("test-type");
            /* Start Chrome maximized */
            options.addArguments("start-maximized");
            /* Enable below to Opens Chrome in headless mode */
            //options.addArguments("--headless");
            /* Enable below to Opens Chrome in incognito/private mode */
            //options.addArguments("--private");
            FirefoxProfile profile = new FirefoxProfile();
            /* Enable or Disable memory cache */
            profile.setPreference("browser.cache.memory.enable", false);
            /* Enable or Disable disk cache */
            profile.setPreference("browser.cache.disk.enable", false);
            /* Set the pre defined capability – ENSURING_CLEAN_SESSION value to true */
            DesiredCapabilities capabilities = DesiredCapabilities.firefox();
            capabilities.setCapability(FirefoxDriver.PROFILE, profile);
            capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
            /* Set the pre defined capability – ACCEPT_SSL_CERTS value to true */
            capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            /* merge options and capability */
            options.merge(capabilities);
            logger.info("Create WebDriver: Firefox Driver created successfully.");
            return new FirefoxDriver(options);
        } catch (Exception e) {
            logger.error("Create WebDriver: Firefox Driver creation failed due to Exception: " + e);
            throw new CukeBrowseException("Create WebDriver: Firefox Driver creation failed due to Exception:", e);
        }
    }
}
