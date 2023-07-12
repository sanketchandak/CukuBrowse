package core.web.browser.manager.impl;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CukeBrowseException;

public class IEDriverManager implements DriverManager {
    private static final Logger logger = LoggerFactory.getLogger(IEDriverManager.class);

    /**
     * provided link can be referred for InternetExploreDriver
     * https://www.selenium.dev/selenium/docs/api/java/org/openqa/selenium/ie/InternetExplorerDriver.html
     */
    @Override
    public WebDriver createDriver() {
        try {
            WebDriverManager.iedriver().setup();
            InternetExplorerOptions options = new InternetExplorerOptions();
            /* Set the pre-defined capability – ENSURING_CLEAN_SESSION value to true */
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
            capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
            capabilities.setCapability(InternetExplorerDriver.ENABLE_ELEMENT_CACHE_CLEANUP, true);
            capabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
            /* Enable below to Opens Internet Explorer in incognito/private mode */
            //capabilities.setCapability(InternetExplorerDriver.IE_SWITCHES, "-private");
            /* Set the pre-defined capability – ACCEPT_SSL_CERTS value to true */
            capabilities.setCapability("acceptSslCerts", true);
            capabilities.setCapability("javascriptEnabled", true);
            /* merge options and capability */
            options = options.merge(capabilities);
            logger.info("Create WebDriver: Internet Explorer Driver created successfully.");
            new InternetExplorerDriver(options);
        } catch (Exception e) {
            logger.error("Create WebDriver: Internet Explorer Driver creation failed due to Exception: " + e);
            throw new CukeBrowseException("Create WebDriver: Internet Explorer Driver creation failed due to Exception:", e);
        }
        return null;
    }
}
