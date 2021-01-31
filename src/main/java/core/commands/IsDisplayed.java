package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static core.commands.Find.Find;

public class IsDisplayed {
    private static final Logger logger = LoggerFactory.getLogger(IsDisplayed.class);
    public static IsDisplayed IsDisplayed =
            ThreadLocal.withInitial(IsDisplayed::new).get();

    private IsDisplayed() {
        if (IsDisplayed != null) {
            logger.error("Use IsDisplayed variable to get the single instance of this class.");
            throw new RuntimeException("Use IsDisplayed variable to get the single instance of this class.");
        }
    }

    public boolean isDisplayed(By elementBy) {
        boolean status = isDisplayed(Find.find(elementBy));
        logger.info(String.format("Is Displayed: check if '%s' element displayed. Actual status: '%s'", elementBy.toString(), status));
        return status;
    }

    public boolean isDisplayed(WebElement element) {
        boolean status = false;
        try {
            status = element.isDisplayed();
        } catch (WebDriverException ignored) {
        }
        logger.info(String.format("Is Displayed: check if '%s' element displayed. Actual status: '%s'", element.toString(), status));
        return status;
    }
}
