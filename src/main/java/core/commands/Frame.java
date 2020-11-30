package core.commands;

import core.browser.runner.WebDriverRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Frame {
    private WebDriver driver;
    public static Frame Frame =
            ThreadLocal.withInitial(Frame::new).get();

    private Frame() {
        if (Frame != null) {
            throw new RuntimeException("Use Frame variable to get the single instance of this class.");
        }
    }

    private void setDriverSession() {
        driver = WebDriverRunner.getInstance().getWebDriver();
    }

    public void switchToFrameByIndex(int frameIndex) {
        setDriverSession();
        driver.switchTo().defaultContent();
        driver.switchTo().frame(frameIndex);
    }

    public void switchToFrameByNameOrId(String frameNameOrId) {
        setDriverSession();
        driver.switchTo().defaultContent();
        driver.switchTo().frame(frameNameOrId);
    }

    public void switchToFrameByFrameElement(WebElement frameElement) {
        setDriverSession();
        driver.switchTo().defaultContent();
        driver.switchTo().frame(frameElement);
    }

    public void switchToMainFrame() {
        setDriverSession();
        driver.switchTo().defaultContent();
    }

    public void switchToParentFrame() {
        setDriverSession();
        driver.switchTo().parentFrame();
    }
}
