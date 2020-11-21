package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static core.commands.Find.Find;

public class SendKeys {
    public static SendKeys SendKeys =
            ThreadLocal.withInitial(SendKeys::new).get();

    private SendKeys() {
        if (SendKeys != null) {
            throw new RuntimeException("Use SendKeys variable to get the single instance of this class.");
        }
    }

    public void sendKeys(By elementIdentifierBy, String textToSend) {
        sendKeys(Find.find(elementIdentifierBy), textToSend);
    }

    public void sendKeys(WebElement element, String textToSend) {
        element.sendKeys(textToSend);
    }
}
