package core.browser.manager.impl;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            /* Set the pre defined capability – ENSURING_CLEAN_SESSION value to true */
            DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
            capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
            capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
            capabilities.setCapability(InternetExplorerDriver.ENABLE_ELEMENT_CACHE_CLEANUP, true);
            capabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
            /* Enable below to Opens Internet Explorer in incognito/private mode */
            //capabilities.setCapability(InternetExplorerDriver.IE_SWITCHES, "-private");
            /* Set the pre defined capability – ACCEPT_SSL_CERTS value to true */
            capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            capabilities.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
            /* merge options and capability */
            options.merge(capabilities);
            logger.info("Create Web Driver: Internet Explorer Driver created successfully.");
            new InternetExplorerDriver(options);
        } catch (Exception e) {
            logger.error("Create Web Driver: Web Driver creation failed due to " + e.toString(), e);
            e.printStackTrace();
        }
        return null;
    }
}
