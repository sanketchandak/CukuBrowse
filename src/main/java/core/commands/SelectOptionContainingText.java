package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Quotes;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

import static core.commands.Click.Click;
import static core.commands.FindAll.FindAll;
import static core.commands.Find.Find;
import static core.commands.GetInnerHtml.GetInnerHtml;

public class SelectOptionContainingText {
    public static SelectOptionContainingText SelectOptionContainingText =
            ThreadLocal.withInitial(SelectOptionContainingText::new).get();

    private SelectOptionContainingText() {
        if (SelectOptionContainingText != null) {
            throw new RuntimeException("Use SelectOptionContainingText variable to get the single instance of this class.");
        }
    }

    public void selectOptionsByTexts(By dropdownBy, String text, By... elementDropdownOptionsBy) throws Exception {
        selectOptionsByTexts(Find.find(dropdownBy), text, elementDropdownOptionsBy);
    }

    public void selectOptionsByTexts(WebElement dropdownElement, String text, By... elementDropdownOptionsBy) throws Exception {
        assert elementDropdownOptionsBy != null;
        if ("select".equalsIgnoreCase(dropdownElement.getTagName())) {
            Select select = new Select(dropdownElement);
            List<WebElement> options = dropdownElement.findElements(By.xpath(".//option[contains(normalize-space(.), " + Quotes.escape(text) + ")]"));
            if (options.isEmpty()) {
                throw new NoSuchElementException("Cannot locate option containing text: " + text);
            }
            for (WebElement option : options) {
                if (!option.isSelected()) {
                    Click.clickOn(option, ClickType.JSClick);
                }
                if (!select.isMultiple()) {
                    break;
                }
            }
        } else {
            dropdownElement.click();
            if (elementDropdownOptionsBy.length == 0) {
                throw new ArrayIndexOutOfBoundsException("1 Child element 'By' is required.");
            }
            List<WebElement> allOptions = FindAll.findAll(dropdownElement, elementDropdownOptionsBy[0]);
            if (allOptions.isEmpty()) {
                throw new Exception("Cannot find Sub-Elements of '" + GetInnerHtml.getInnerHtml(elementDropdownOptionsBy[0]) + "' under: " + GetInnerHtml.getInnerHtml(dropdownElement));
            }
            List<WebElement> elements = allOptions.stream()
                    .filter(element -> element.getText().contains(text.trim()))
                    .collect(Collectors.toList());

            if (elements.isEmpty()) {
                throw new NoSuchElementException("Cannot locate option containing text: " + text);
            }

            for (WebElement element : elements) {
                if (!element.isSelected()) {
                    Click.clickOn(element, ClickType.JSClick);
                }
            }
        }
    }
}
