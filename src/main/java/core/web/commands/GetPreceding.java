package core.web.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CukeBrowseException;

import static core.web.commands.Find.Find;

public class GetPreceding {
    private static final Logger logger = LoggerFactory.getLogger(GetPreceding.class);
    public static GetPreceding GetPreceding =
            ThreadLocal.withInitial(GetPreceding::new).get();

    private GetPreceding() {
        if (GetPreceding != null) {
            logger.error("Use GetPreceding variable to get the single instance of this class.");
            throw new CukeBrowseException("Use GetPreceding variable to get the single instance of this class.");
        }
    }

    public WebElement getPreceding(By elementBy, int siblingIndex) {
        logger.info(String.format("Get Preceding: get preceding of '%s' element at index: '%s'", elementBy.toString(), siblingIndex));
        return Find.find(elementBy, By.xpath(String.format("preceding-sibling::*[%d]", siblingIndex)));
    }

    public WebElement getPreceding(WebElement element, int siblingIndex) {
        logger.info(String.format("Get Preceding: get preceding of '%s' element at index: '%s'", element.toString(), siblingIndex));
        return Find.find(element, By.xpath(String.format("preceding-sibling::*[%d]", siblingIndex)));
    }
}
