package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static core.commands.Click.Click;
import static core.commands.FindAll.FindAll;
import static core.commands.Find.Find;
import static core.commands.GetInnerHtml.GetInnerHtml;

public class SelectOptionByTextOrIndex {
    private static final Logger logger = LoggerFactory.getLogger(SelectOptionByTextOrIndex.class);
    public static SelectOptionByTextOrIndex SelectOptionByTextOrIndex =
            ThreadLocal.withInitial(SelectOptionByTextOrIndex::new).get();

    private SelectOptionByTextOrIndex() {
        if (SelectOptionByTextOrIndex != null) {
            logger.error("Use SelectOptionByTextOrIndex variable to get the single instance of this class.");
            throw new RuntimeException("Use SelectOptionByTextOrIndex variable to get the single instance of this class.");
        }
    }

    public void selectOptionsByIndexes(By dropdownBy, int[] indexes, By... elementDropdownOptionsBy) {
        logger.info(String.format("Select Options By Indexes: select '%s' options from '%s' dropdown with indexes as '%s'",
                (elementDropdownOptionsBy.length != 0) ? Arrays.stream(elementDropdownOptionsBy).map(By::toString).collect(Collectors.toList()) : "",
                dropdownBy.toString(),
                Arrays.toString(indexes)
        ));
        selectOptionsByIndexes(Find.find(dropdownBy), indexes, elementDropdownOptionsBy);
    }

    public void selectOptionsByIndexes(WebElement dropdownElement, int[] indexes, By... elementDropdownOptionsBy) {
        assert elementDropdownOptionsBy != null;
        logger.info(String.format("Select Options By Indexes: select '%s' options from '%s' dropdown with indexes as '%s'",
                (elementDropdownOptionsBy.length != 0) ? Arrays.stream(elementDropdownOptionsBy).map(By::toString).collect(Collectors.toList()) : "",
                GetInnerHtml.getInnerHtml(dropdownElement),
                Arrays.toString(indexes)
        ));
        if ("select".equalsIgnoreCase(dropdownElement.getTagName())) {
            Select select = new Select(dropdownElement);
            for (int index : indexes) {
                try {
                    select.selectByIndex(index);
                } catch (NoSuchElementException e) {
                    logger.error("Select Options By Indexes: /option[index:" + index + "] is not present");
                    throw new NoSuchElementException("/option[index:" + index + "] is not present");
                }
            }
        } else {
            dropdownElement.click();
            if (elementDropdownOptionsBy.length == 0) {
                logger.error("Select Options By Indexes: 1 Child element 'By' is required.");
                throw new ArrayIndexOutOfBoundsException("1 Child element 'By' is required.");
            }
            List<WebElement> options = FindAll.findAll(dropdownElement, elementDropdownOptionsBy[0]);
            for (int index : indexes) {
                try {
                    WebElement element = options.get(index);
                    if (!element.isSelected()) {
                        Click.clickOn(element, ClickType.JSClick);
                    }
                } catch (IndexOutOfBoundsException e) {
                    logger.error("Select Options By Indexes: Dropdown option with index:" + index + " is not present for: " + Arrays.toString(elementDropdownOptionsBy));
                    throw new NoSuchElementException("Dropdown option with index:" + index + " is not present for: " + Arrays.toString(elementDropdownOptionsBy));
                }
            }
        }
    }

    public void selectOptionsByTexts(By dropdownBy, String[] texts, By... elementDropdownOptionsBy) throws Exception {
        logger.info(String.format("Select Options By Texts: select '%s' options from '%s' dropdown having text as '%s'",
                (elementDropdownOptionsBy.length != 0) ? Arrays.stream(elementDropdownOptionsBy).map(By::toString).collect(Collectors.toList()) : "",
                dropdownBy.toString(),
                Arrays.toString(texts)
        ));
        selectOptionsByTexts(Find.find(dropdownBy), texts, elementDropdownOptionsBy);
    }

    public void selectOptionsByTexts(WebElement dropdownElement, String[] texts, By... elementDropdownOptionsBy) throws Exception {
        assert elementDropdownOptionsBy != null;
        logger.info(String.format("Select Options By Texts: select '%s' options from '%s' dropdown having text as '%s'",
                (elementDropdownOptionsBy.length != 0) ? Arrays.stream(elementDropdownOptionsBy).map(By::toString).collect(Collectors.toList()) : "",
                GetInnerHtml.getInnerHtml(dropdownElement),
                Arrays.toString(texts)
        ));
        if ("select".equalsIgnoreCase(dropdownElement.getTagName())) {
            Select select = new Select(dropdownElement);
            for (String text : texts) {
                try {
                    select.selectByVisibleText(text);
                } catch (NoSuchElementException e) {
                    throw new NoSuchElementException("/option[text:" + text + "] is not present");
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
            Map<String, WebElement> elements = allOptions.stream().collect(Collectors.toMap(WebElement::getText, element -> element));
            for (String text : texts) {
                WebElement element = elements.get(text);
                if (element == null) {
                    throw new NoSuchElementException("Dropdown option with text:" + text + " is not present for: " + Arrays.toString(elementDropdownOptionsBy));
                }
                if (!element.isSelected()) {
                    Click.clickOn(element, ClickType.JSClick);
                }
            }
        }
    }
}
