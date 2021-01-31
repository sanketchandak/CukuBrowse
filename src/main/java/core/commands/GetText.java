package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static core.commands.Find.Find;

public class GetText {
    private static final Logger logger = LoggerFactory.getLogger(GetText.class);
    public static GetText GetText =
            ThreadLocal.withInitial(GetText::new).get();

    public GetText() {
        if (GetText != null) {
            logger.error("Use GetText variable to get the single instance of this class.");
            throw new RuntimeException("Use GetText variable to get the single instance of this class.");
        }
    }

    public String getText(By elementBy) {
        String retrievedText = Find.find(elementBy).getText();
        logger.info(String.format("Get Text: '%s' element have text as '%s'", elementBy.toString(), retrievedText));
        return retrievedText;
    }

    public String getText(WebElement element) {
        String retrievedText = element.getText();
        logger.info(String.format("Get Text: '%s' element have text as '%s'", element.toString(), retrievedText));
        return retrievedText;
    }
}
