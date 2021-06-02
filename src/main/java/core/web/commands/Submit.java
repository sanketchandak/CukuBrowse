package core.web.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CukeBrowseException;

import java.util.List;

import static core.web.commands.FindAll.FindAll;

public class Submit {
    private static final Logger logger = LoggerFactory.getLogger(Submit.class);
    public static Submit Submit =
            ThreadLocal.withInitial(Submit::new).get();

    private Submit() {
        if (Submit != null) {
            logger.error("Use Submit variable to get the single instance of this class.");
            throw new CukeBrowseException("Use Submit variable to get the single instance of this class.");
        }
    }

    public void submit(By elementIdentifierBy) {
        List<WebElement> elements = FindAll.findAll(elementIdentifierBy);
        if (!elements.isEmpty()) {
            submit(elements.get(0));
            logger.info("Submit: submit form");
        } else {
            logger.error("Submit: Element " + elementIdentifierBy + " not present on UI");
            throw new CukeBrowseException("Element " + elementIdentifierBy + " not present on UI");
        }
    }

    public void submit(WebElement element) {
        if (element.getTagName().equalsIgnoreCase("form")
                || !FindAll.findAll(element, By.xpath("//ancestor::form")).isEmpty()) {
            element.submit();
            logger.info("Submit: submit form");
        } else {
            logger.error("Submit: Submit action can only perform on 'form' element");
            throw new CukeBrowseException("Submit action can only perform on 'form' element");
        }
    }
}
