package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static core.commands.Find.Find;

public class PressControlA {
    private static final Logger logger = LoggerFactory.getLogger(PressControlA.class);
    public static PressControlA PressControlA =
            ThreadLocal.withInitial(PressControlA::new).get();

    private PressControlA() {
        if (PressControlA != null) {
            logger.error("Use PressControlA variable to get the single instance of this class.");
            throw new RuntimeException("Use PressControlA variable to get the single instance of this class.");
        }
    }

    public void pressControlA(By elementBy) {
        pressControlA(Find.find(elementBy));
        logger.info(String.format("Press Control A: do 'control + A' on '%s' element", elementBy.toString()));
    }

    public void pressControlA(WebElement element) {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        } else if (osName.contains("mac")) {
            element.sendKeys(Keys.chord(Keys.COMMAND, "a"));
        }
        logger.info(String.format("Press Control A: do 'control + A' on '%s' element", element.toString()));
    }
}
