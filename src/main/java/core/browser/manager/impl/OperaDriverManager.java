package core.browser.manager.impl;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            logger.info("Create Web Driver: Opera Driver created successfully.");
            return new OperaDriver(options);
        } catch (Exception e) {
            logger.error("Create Web Driver: Web Driver creation failed due to " + e.toString(), e);
            e.printStackTrace();
        }
        return null;
    }
}
