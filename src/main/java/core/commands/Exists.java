package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static core.commands.Find.Find;

public class Exists {
    private static final Logger logger = LoggerFactory.getLogger(Exists.class);
    private final Find find;
    public static Exists Exists =
            ThreadLocal.withInitial(Exists::new).get();

    private Exists() {
        find = Find;
        if (Exists != null) {
            logger.error("Use Exists variable to get the single instance of this class.");
            throw new RuntimeException("Use Exists variable to get the single instance of this class.");
        }
    }

    public boolean exists(By elementBy) {
        try {
            logger.info(String.format("Exists: check if '%s' element present", elementBy));
            return find.find(elementBy) != null;
        } catch (WebDriverException exception) {
            return false;
        }
    }
}
