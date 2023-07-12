package core.web.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static core.web.commands.Find.Find;

public class SendKeys {
    private static final Logger logger = LoggerFactory.getLogger(SendKeys.class);
    public static SendKeys SendKeys =
            ThreadLocal.withInitial(SendKeys::new).get();

    private SendKeys() {
        if (SendKeys != null) {
            logger.error("Use SendKeys variable to get the single instance of this class.");
            throw new RuntimeException("Use SendKeys variable to get the single instance of this class.");
        }
    }

    public void sendKeys(By elementIdentifierBy, String textToSend) {
        Find.find(elementIdentifierBy).sendKeys(textToSend);
        logger.info(String.format("Send Keys: send '%s' to element '%s'", textToSend, elementIdentifierBy.toString()));
    }

    public void sendKeys(WebElement element, String textToSend) {
        element.sendKeys(textToSend);
        logger.info(String.format("Send Keys: send '%s' to element '%s'", textToSend, element));
    }
}
