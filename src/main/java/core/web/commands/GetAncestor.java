package core.web.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static core.web.commands.Find.Find;

public class GetAncestor {
    private static final Logger logger = LoggerFactory.getLogger(GetAncestor.class);
    public static GetAncestor GetAncestor =
            ThreadLocal.withInitial(GetAncestor::new).get();

    private GetAncestor() {
        if (GetAncestor != null) {
            logger.error("Use GetAncestor variable to get the single instance of this class.");
            throw new RuntimeException("Use GetAncestor variable to get the single instance of this class.");
        }
    }

    public WebElement getAncestor(By elementBy, String ancestorPath) {
        logger.info(String.format("Get Ancestor: get ancestor: '%s' for element: '%s'", ancestorPath, elementBy.toString()));
        return Find.find(elementBy, By.xpath("ancestor::" + ancestorPath));
    }

    public WebElement getAncestor(WebElement element, String ancestorPath) {
        logger.info(String.format("Get Ancestor: get ancestor: '%s' for element: '%s'", ancestorPath, element.toString()));
        return Find.find(element, By.xpath("ancestor::" + ancestorPath));
    }
}
