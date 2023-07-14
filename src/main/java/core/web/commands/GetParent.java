package core.web.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CukeBrowseException;

import static core.web.commands.Find.Find;

public class GetParent {
    private static final Logger logger = LoggerFactory.getLogger(GetParent.class);
    public static GetParent GetParent =
            ThreadLocal.withInitial(GetParent::new).get();

    private GetParent() {
        if (GetParent != null) {
            logger.error("Use GetParent variable to get the single instance of this class.");
            throw new CukeBrowseException("Use GetParent variable to get the single instance of this class.");
        }
    }

    public WebElement get(By elementBy) {
        logger.info(String.format("Get Parent: get parent of element: '%s'", elementBy.toString()));
        return Find.findElement(elementBy, By.xpath(".."));
    }

    public WebElement get(WebElement element) {
        logger.info(String.format("Get Parent: get parent of element: '%s'", element.toString()));
        return Find.findElement(element, By.xpath(".."));
    }
}
