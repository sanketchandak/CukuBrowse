package core.web.browser.manager.impl;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CukeBrowseException;

public class OperaDriverManager implements DriverManager {
    private static final Logger logger = LoggerFactory.getLogger(OperaDriverManager.class);

    @Override
    public WebDriver createDriver() {
        try {
            WebDriverManager.operadriver().setup();
            OperaOptions options = new OperaOptions();
            options.addArguments("private");
            DesiredCapabilities capabilities = DesiredCapabilities.operaBlink();
            capabilities.setAcceptInsecureCerts(true);
            capabilities.setJavascriptEnabled(true);
            /* merge options and capability */
            options.merge(capabilities);
            logger.info("Create WebDriver: Opera Driver created successfully.");
            return new OperaDriver(options);
        } catch (Exception e) {
            logger.error("Create WebDriver: Opera Driver creation failed due to Exception: " + e);
            throw new CukeBrowseException("Create WebDriver: Opera Driver creation failed due to Exception:", e);
        }
    }
}
