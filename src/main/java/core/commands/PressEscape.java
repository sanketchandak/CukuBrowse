package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import static core.commands.Find.Find;

public class PressEscape {
    public static PressEscape PressEscape =
            ThreadLocal.withInitial(PressEscape::new).get();

    private PressEscape() {
        if (PressEscape != null) {
            throw new RuntimeException("Use PressEscape variable to get the single instance of this class.");
        }
    }

    public void pressEscape(By elementBy) {
        pressEscape(Find.find(elementBy));
    }

    public void pressEscape(WebElement element) {
        element.sendKeys(Keys.ESCAPE);
    }
}
