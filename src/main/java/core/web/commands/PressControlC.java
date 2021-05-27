package core.web.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;

import static core.web.commands.Find.Find;

public class PressControlC {
    private static final Logger logger = LoggerFactory.getLogger(PressControlC.class);
    public static PressControlC PressControlC =
            ThreadLocal.withInitial(PressControlC::new).get();

    private PressControlC() {
        if (PressControlC != null) {
            logger.error("Use PressControlC variable to get the single instance of this class.");
            throw new RuntimeException("Use PressControlC variable to get the single instance of this class.");
        }
    }

    public synchronized String pressControlC(By elementBy) {
        String copiedText = pressControlC(Find.find(elementBy));
        logger.info(String.format("Press Control C: do 'control + C' on '%s' element. Copied value: '%s'", elementBy.toString(), copiedText));
        return copiedText;
    }

    public synchronized String pressControlC(WebElement element) {
        String copiedText = null;
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            element.sendKeys(Keys.chord(Keys.CONTROL, "c"));
        } else if (osName.contains("mac")) {
            element.sendKeys(Keys.chord(Keys.COMMAND, "c"));
        }
        try {
            copiedText = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException | IOException e) {
            logger.error("Press Control C: "+ Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
        logger.info(String.format("Press Control C: do 'control + C' on '%s' element. Copied value: '%s'", element.toString(), copiedText));
        return copiedText;
    }
}
