package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import static core.commands.Find.Find;

public class Exists {
    private final Find find;
    public static Exists Exists =
            ThreadLocal.withInitial(Exists::new).get();

    private Exists() {
        find = Find;
        if (Exists != null) {
            throw new RuntimeException("Use Exists variable to get the single instance of this class.");
        }
    }

    public boolean exists(By elementBy) {
        try {
            return find.find(elementBy) != null;
        } catch (WebDriverException exception) {
            return false;
        }
    }

    public boolean exists(WebElement element) {
        try {
            return element != null;
        } catch (WebDriverException exception) {
            return false;
        }
    }
}
