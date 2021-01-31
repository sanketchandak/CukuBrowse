package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static core.commands.Find.Find;

public class PressEnter {
    private static final Logger logger = LoggerFactory.getLogger(PressEnter.class);
    public static PressEnter PressEnter =
            ThreadLocal.withInitial(PressEnter::new).get();

    private PressEnter() {
        if (PressEnter != null) {
            logger.error("Use PressEnter variable to get the single instance of this class.");
            throw new RuntimeException("Use PressEnter variable to get the single instance of this class.");
        }
    }

    public void pressEnter(By elementBy) {
        Find.find(elementBy).sendKeys(Keys.ENTER);
        logger.info(String.format("Press Enter: press enter on element '%s'", elementBy.toString()));
    }

    public void pressEnter(WebElement element) {
        element.sendKeys(Keys.ENTER);
        logger.info(String.format("Press Enter: press enter on element '%s'", element.toString()));
    }
}
