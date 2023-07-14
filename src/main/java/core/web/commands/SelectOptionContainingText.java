package core.web.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Quotes;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CukeBrowseException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static core.web.commands.Click.Click;
import static core.web.commands.FindAll.FindAll;
import static core.web.commands.Find.Find;
import static core.web.commands.GetInnerHtml.GetInnerHtml;

public class SelectOptionContainingText {
    private static final Logger logger = LoggerFactory.getLogger(SelectOptionContainingText.class);
    public static SelectOptionContainingText SelectOptionContainingText =
            ThreadLocal.withInitial(SelectOptionContainingText::new).get();

    private SelectOptionContainingText() {
        if (SelectOptionContainingText != null) {
            logger.error("Use SelectOptionContainingText variable to get the single instance of this class.");
            throw new CukeBrowseException("Use SelectOptionContainingText variable to get the single instance of this class.");
        }
    }

    public void selectOptionsContainingTexts(By dropdownBy, String text, By... elementDropdownOptionsBy) {
        logger.info(String.format("Select Options By Texts: select '%s' options from '%s' dropdown containing text as '%s'",
                (elementDropdownOptionsBy.length != 0) ? Arrays.stream(elementDropdownOptionsBy).map(By::toString).collect(Collectors.toList()) : "",
                dropdownBy.toString(),
                text
        ));
        selectOptionsContainingTexts(Find.findElement(dropdownBy), text, elementDropdownOptionsBy);
    }

    public void selectOptionsContainingTexts(WebElement dropdownElement, String text, By... elementDropdownOptionsBy) {
        assert elementDropdownOptionsBy != null;
        logger.info(String.format("Select Options By Texts: select '%s' options from '%s' dropdown containing text as '%s'",
                (elementDropdownOptionsBy.length != 0) ? Arrays.stream(elementDropdownOptionsBy).map(By::toString).collect(Collectors.toList()) : "",
                GetInnerHtml.get(dropdownElement),
                text
        ));
        if ("select".equalsIgnoreCase(dropdownElement.getTagName())) {
            Select select = new Select(dropdownElement);
            List<WebElement> options = dropdownElement.findElements(By.xpath(".//option[contains(normalize-space(.), " + Quotes.escape(text) + ")]"));
            if (options.isEmpty()) {
                logger.error("Select Options By Texts: Cannot locate option containing text: " + text);
                throw new CukeBrowseException("Cannot locate option containing text: " + text);
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
                logger.error("Select Options By Texts: 1 Child element 'By' is required.");
                throw new CukeBrowseException("1 Child element 'By' is required.");
            }
            List<WebElement> allOptions = FindAll.find(dropdownElement, elementDropdownOptionsBy[0]);
            if (allOptions.isEmpty()) {
                logger.error("Select Options By Texts: Cannot find Sub-Elements of '" + GetInnerHtml.get(elementDropdownOptionsBy[0]) + "' under: " + GetInnerHtml.get(dropdownElement));
                throw new CukeBrowseException("Cannot find Sub-Elements of '" + GetInnerHtml.get(elementDropdownOptionsBy[0]) + "' under: " + GetInnerHtml.get(dropdownElement));
            }
            List<WebElement> elements = allOptions.stream()
                    .filter(element -> element.getText().contains(text.trim()))
                    .collect(Collectors.toList());

            if (elements.isEmpty()) {
                logger.error("Select Options By Texts: Cannot locate option containing text: " + text);
                throw new CukeBrowseException("Cannot locate option containing text: " + text);
            }

            for (WebElement element : elements) {
                if (!element.isSelected()) {
                    Click.clickOn(element, ClickType.JSClick);
                }
            }
        }
    }
}
