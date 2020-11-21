package core.browser.manager.impl;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EdgeDriverManager implements DriverManager {
    private static final Logger logger = LoggerFactory.getLogger(EdgeDriverManager.class);

    @Override
    public WebDriver createDriver() {
        try {
            WebDriverManager.edgedriver().setup();
            DesiredCapabilities capabilities = DesiredCapabilities.edge();
            logger.info("Create Web Driver: Edge Driver created successfully.");
            new EdgeDriver(new EdgeOptions().merge(capabilities));
        } catch (Exception e) {
            logger.error("Create Web Driver: Web Driver creation failed due to " + e.toString(), e);
            e.printStackTrace();
        }
        return null;
    }
}
