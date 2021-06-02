package core.web.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CukeBrowseException;

import static core.web.commands.Find.Find;

public class GetSibling {
    private static final Logger logger = LoggerFactory.getLogger(GetSibling.class);
    public static GetSibling GetSibling =
            ThreadLocal.withInitial(GetSibling::new).get();

    private GetSibling() {
        if (GetSibling != null) {
            logger.error("Use GetSibling variable to get the single instance of this class.");
            throw new CukeBrowseException("Use GetSibling variable to get the single instance of this class.");
        }
    }

    public WebElement getFollowingSibling(By elementBy, int siblingIndex) {
        logger.info(String.format("Get Following Sibling: get '%s' following sibling of element '%s'",siblingIndex, elementBy.toString()));
        return Find.find(elementBy, By.xpath(String.format("following-sibling::*[%d]", siblingIndex)));
    }

    public WebElement getFollowingSibling(WebElement element, int siblingIndex) {
        logger.info(String.format("Get Following Sibling: get '%s' following sibling of element '%s'",siblingIndex, element.toString()));
        return Find.find(element, By.xpath(String.format("following-sibling::*[%d]", siblingIndex)));
    }

    public WebElement getPrecedingSibling(By elementBy, int siblingIndex) {
        logger.info(String.format("Get Preceding Sibling: get '%s' preceding sibling of element '%s'",siblingIndex, elementBy.toString()));
        return Find.find(elementBy, By.xpath(String.format("preceding-sibling::*[%d]", siblingIndex)));
    }

    public WebElement getPrecedingSibling(WebElement element, int siblingIndex) {
        logger.info(String.format("Get Preceding Sibling: get '%s' preceding sibling of element '%s'",siblingIndex, element.toString()));
        return Find.find(element, By.xpath(String.format("preceding-sibling::*[%d]", siblingIndex)));
    }
}
