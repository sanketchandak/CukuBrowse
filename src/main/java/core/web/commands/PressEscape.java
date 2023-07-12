package core.web.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CukeBrowseException;

import static core.web.commands.Find.Find;

public class PressEscape {
    private static final Logger logger = LoggerFactory.getLogger(PressEscape.class);
    public static PressEscape PressEscape =
            ThreadLocal.withInitial(PressEscape::new).get();

    private PressEscape() {
        if (PressEscape != null) {
            logger.error("Use PressEscape variable to get the single instance of this class.");
            throw new CukeBrowseException("Use PressEscape variable to get the single instance of this class.");
        }
    }

    public void pressEscape(By elementBy) {
        pressEscape(Find.find(elementBy));
        logger.info(String.format("Press Escape: press escape on element '%s'", elementBy.toString()));
    }

    public void pressEscape(WebElement element) {
        element.sendKeys(Keys.ESCAPE);
        logger.info(String.format("Press Escape: press escape on element '%s'", element));
    }
}
