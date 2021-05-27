package core.web.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static core.web.commands.GetAttribute.GetAttribute;

public class GetInnerHtml {
    private static final Logger logger = LoggerFactory.getLogger(GetInnerHtml.class);
    public static GetInnerHtml GetInnerHtml =
            ThreadLocal.withInitial(GetInnerHtml::new).get();

    private GetInnerHtml() {
        if (GetInnerHtml != null) {
            logger.error("Use GetInnerHtml variable to get the single instance of this class.");
            throw new RuntimeException("Use GetInnerHtml variable to get the single instance of this class.");
        }
    }

    public String getInnerHtml(By elementBy) {
        String retrievedInnerHtml = GetAttribute.getAttribute(elementBy,"innerHTML");
        logger.info(String.format("Get Inner Html: inner html of element: '%s' having value as '%s'", elementBy.toString(), retrievedInnerHtml));
        return retrievedInnerHtml;
    }

    public String getInnerHtml(WebElement element) {
        String retrievedInnerHtml =  GetAttribute.getAttribute(element, "innerHTML");
        logger.info(String.format("Get Inner Html: inner html of element: '%s' having value as '%s'", element, retrievedInnerHtml));
        return retrievedInnerHtml;
    }
}
