package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import static core.commands.Find.Find;

public class PressTab {
    public static PressTab PressTab =
            ThreadLocal.withInitial(PressTab::new).get();

    private PressTab() {
        if (PressTab != null) {
            throw new RuntimeException("Use PressTap variable to get the single instance of this class.");
        }
    }

    public void pressTab(By elementBy) {
        pressTab(Find.find(elementBy));
    }

    public void pressTab(WebElement element) {
        element.sendKeys(Keys.TAB);
    }
}
