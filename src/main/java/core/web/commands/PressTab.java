package core.web.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CukeBrowseException;

import static core.web.commands.Find.Find;

public class PressTab {
    private static final Logger logger = LoggerFactory.getLogger(PressTab.class);
    public static PressTab PressTab =
            ThreadLocal.withInitial(PressTab::new).get();

    private PressTab() {
        if (PressTab != null) {
            logger.error("Use PressTap variable to get the single instance of this class.");
            throw new CukeBrowseException("Use PressTap variable to get the single instance of this class.");
        }
    }

    public void press(By elementBy) {
        press(Find.findElement(elementBy));
        logger.info(String.format("Press Tab: press tab on element '%s'", elementBy.toString()));
    }

    public void press(WebElement element) {
        element.sendKeys(Keys.TAB);
        logger.info(String.format("Press Tab: press tab on element '%s'", element));
    }
}
