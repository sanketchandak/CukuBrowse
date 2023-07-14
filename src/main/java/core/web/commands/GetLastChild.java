package core.web.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CukeBrowseException;

import static core.web.commands.Find.Find;

public class GetLastChild {
    private static final Logger logger = LoggerFactory.getLogger(GetLastChild.class);
    public static GetLastChild GetLastChild =
            ThreadLocal.withInitial(GetLastChild::new).get();

    private GetLastChild() {
        if (GetLastChild != null) {
            logger.error("Use GetLastChild variable to get the single instance of this class.");
            throw new CukeBrowseException("Use GetLastChild variable to get the single instance of this class.");
        }
    }

    public WebElement get(By parentBy) {
        logger.info(String.format("Get Last Child: get last child of element: '%s'", parentBy.toString()));
        return Find.findElement(parentBy, By.xpath("*[last()]"));
    }

    public WebElement get(WebElement parentElement) {
        logger.info(String.format("Get Last Child: get last child of element: '%s'", parentElement.toString()));
        return Find.findElement(parentElement, By.xpath("*[last()]"));
    }
}
