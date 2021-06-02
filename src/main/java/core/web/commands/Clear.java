package core.web.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CukeBrowseException;

import static core.web.commands.Find.Find;

public class Clear {
    private static final Logger logger = LoggerFactory.getLogger(Clear.class);
    public static Clear Clear =
            ThreadLocal.withInitial(Clear::new).get();
    private final Find find;

    private Clear() {
        find = Find;
        if (Clear != null) {
            logger.error("Use Clear variable to get the single instance of this class.");
            throw new CukeBrowseException("Use Clear variable to get the single instance of this class.");
        }
    }

    public void clearBox(By boxToClearBy) {
        clearBox(find.find(boxToClearBy));
        logger.info(String.format("Clear Box: clear data in element '%s'.", boxToClearBy.toString()));
    }

    public void clearBox(WebElement boxToClearElement) {
        boxToClearElement.clear();
        logger.info(String.format("Clear Box: clear data in element '%s'.", boxToClearElement));
    }
}
