package core.web.browser.manager.impl;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CukeBrowseException;

public class EdgeDriverManager implements DriverManager {
    private static final Logger logger = LoggerFactory.getLogger(EdgeDriverManager.class);

    @Override
    public WebDriver createDriver() {
        try {
            WebDriverManager.edgedriver().setup();
            DesiredCapabilities capabilities = new DesiredCapabilities();
            logger.info("Create WebDriver: Edge Driver created successfully.");
            new EdgeDriver(new EdgeOptions().merge(capabilities));
        } catch (Exception e) {
            logger.error("Create WebDriver: Edge Driver creation failed due to Exception: " + e);
            throw new CukeBrowseException("Create WebDriver: Edge Driver creation failed due to Exception:", e);
        }
        return null;
    }
}
