package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static core.commands.FindAll.FindAll;

public class Submit {
    public static Submit Submit =
            ThreadLocal.withInitial(Submit::new).get();

    private Submit() {
        if (Submit != null) {
            throw new RuntimeException("Use Submit variable to get the single instance of this class.");
        }
    }

    public void submit(By elementIdentifierBy) {
        List<WebElement> elements = FindAll.findAll(elementIdentifierBy);
        if (!elements.isEmpty()) {
            submit(elements.get(0));
        } else {
            throw new RuntimeException("Element " + elementIdentifierBy + " not present on UI");
        }
    }

    public void submit(WebElement element) {
        if (element.getTagName().equalsIgnoreCase("form")
                || !FindAll.findAll(element, By.xpath("//ancestor::form")).isEmpty()) {
            element.submit();
        } else {
            throw new RuntimeException("Submit action can only perform on 'form' element");
        }
    }
}
