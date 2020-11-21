package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static core.commands.Find.Find;

public class Clear {
    public static Clear Clear =
            ThreadLocal.withInitial(Clear::new).get();
    private final Find find;

    private Clear() {
        find = Find;
        if (Clear != null) {
            throw new RuntimeException("Use Clear variable to get the single instance of this class.");
        }
    }

    public void clearBox(By boxToClearBy) {
        clearBox(find.find(boxToClearBy));
    }

    public void clearBox(WebElement boxToClearElement) {
        boxToClearElement.clear();
    }
}
