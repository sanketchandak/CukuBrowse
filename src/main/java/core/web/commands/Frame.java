package core.web.commands;

import core.web.browser.runner.WebDriverRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Frame {
    private static final Logger logger = LoggerFactory.getLogger(Frame.class);
    private WebDriver driver;
    public static Frame Frame =
            ThreadLocal.withInitial(Frame::new).get();

    private Frame() {
        if (Frame != null) {
            logger.error("Use Frame variable to get the single instance of this class.");
            throw new RuntimeException("Use Frame variable to get the single instance of this class.");
        }
    }

    private void setDriverSession() {
        driver = WebDriverRunner.getInstance().getWebDriver();
    }

    public void switchToFrameByIndex(int frameIndex) {
        setDriverSession();
        logger.info(String.format("Switch To Frame By Index : Switch to frame by index '%s'", frameIndex));
        driver.switchTo().defaultContent();
        driver.switchTo().frame(frameIndex);
    }

    public void switchToFrameByNameOrId(String frameNameOrId) {
        setDriverSession();
        logger.info(String.format("Switch To Frame By Name or Id : Switch to frame by name or id '%s'", frameNameOrId));
        driver.switchTo().defaultContent();
        driver.switchTo().frame(frameNameOrId);
    }

    public void switchToFrameByFrameElement(WebElement frameElement) {
        setDriverSession();
        logger.info(String.format("Switch To Frame By Frame Element : Switch to frame by frame element '%s'", frameElement.toString()));
        driver.switchTo().defaultContent();
        driver.switchTo().frame(frameElement);
    }

    public void switchToMainFrame() {
        setDriverSession();
        logger.info("Switch To Main Frame : Switch to main frame");
        driver.switchTo().defaultContent();
    }

    public void switchToParentFrame() {
        setDriverSession();
        logger.info("Switch To Parent Frame : Switch to parent frame");
        driver.switchTo().parentFrame();
    }
}
