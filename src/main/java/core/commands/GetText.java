package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static core.commands.Find.Find;

public class GetText {
    public static GetText GetText =
            ThreadLocal.withInitial(GetText::new).get();

    public GetText() {
        if (GetText != null) {
            throw new RuntimeException("Use GetText variable to get the single instance of this class.");
        }
    }

    public String getText(By elementBy) {
        return getText(Find.find(elementBy));
    }

    public String getText(WebElement element) {
        return element.getText();
    }
}
