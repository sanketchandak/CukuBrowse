package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import static core.commands.Find.Find;

public class PressEnter {
    public static PressEnter PressEnter =
            ThreadLocal.withInitial(PressEnter::new).get();

    private PressEnter() {
        if (PressEnter != null) {
            throw new RuntimeException("Use PressEnter variable to get the single instance of this class.");
        }
    }

    public void pressEnter(By elementBy) {
        Find.find(elementBy).sendKeys(Keys.ENTER);
    }

    public void pressEnter(WebElement element) {
        element.sendKeys(Keys.ENTER);
    }
}
