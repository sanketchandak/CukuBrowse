package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import static core.commands.Find.Find;

public class PressControlC {
    public static PressControlC PressControlC =
            ThreadLocal.withInitial(PressControlC::new).get();

    private PressControlC() {
        if (PressControlC != null) {
            throw new RuntimeException("Use PressControlC variable to get the single instance of this class.");
        }
    }

    public synchronized String pressControlC(By elementBy) {
        return pressControlC(Find.find(elementBy));
    }

    public synchronized String pressControlC(WebElement element) {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            element.sendKeys(Keys.chord(Keys.CONTROL, "c"));
        } else if (osName.contains("mac")) {
            element.sendKeys(Keys.chord(Keys.COMMAND, "c"));
        }
        try {
            return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
