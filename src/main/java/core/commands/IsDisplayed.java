package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import static core.commands.Find.Find;

public class IsDisplayed {
    public static IsDisplayed IsDisplayed =
            ThreadLocal.withInitial(IsDisplayed::new).get();

    private IsDisplayed() {
        if (IsDisplayed != null) {
            throw new RuntimeException("Use IsDisplayed variable to get the single instance of this class.");
        }
    }

    public boolean isDisplayed(By elementBy) {
        return isDisplayed(Find.find(elementBy));
    }

    public boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (WebDriverException exception) {
            return false;
        }
    }
}
