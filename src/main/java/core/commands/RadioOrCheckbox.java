package core.commands;

import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static core.commands.FindAll.FindAll;
import static core.commands.Click.Click;

public class RadioOrCheckbox {
    private static final Logger logger = LoggerFactory.getLogger(RadioOrCheckbox.class);
    public static RadioOrCheckbox RadioOrCheckbox =
            ThreadLocal.withInitial(RadioOrCheckbox::new).get();

    private RadioOrCheckbox() {
        if (RadioOrCheckbox != null) {
            logger.error("Use RadioOrCheckbox variable to get the single instance of this class.");
            throw new RuntimeException("Use RadioOrCheckbox variable to get the single instance of this class.");
        }
    }

    public WebElement getSelectedRadioOrCheckbox(By radioField) {
        logger.info(String.format("Get Selected Radio Or Checkbox: get Selected Radio Or Checkbox '%s'", radioField.toString()));
        for (WebElement radioOrCheckbox : FindAll.findAll(radioField)) {
            if (radioOrCheckbox.getAttribute("checked") != null || radioOrCheckbox.isSelected()) {
                return radioOrCheckbox;
            }
        }
        return null;
    }

    public List<WebElement> getSelectedRadiosOrCheckboxes(By radioField) {
        logger.info(String.format("Get Selected Radios Or Checkboxes: get Selected Radios Or Checkboxes '%s'", radioField.toString()));
        List<WebElement> selectedRadiosOrCheckboxes = new ArrayList<>();
        for (WebElement radioOrCheckbox : FindAll.findAll(radioField)) {
            if (radioOrCheckbox.getAttribute("checked") != null || radioOrCheckbox.isSelected()) {
                selectedRadiosOrCheckboxes.add(radioOrCheckbox);
            }
        }
        return selectedRadiosOrCheckboxes;
    }

    public void selectRadioOrCheckbox(By elementBy, String... radioOrCheckboxText) {
        assert radioOrCheckboxText != null;
        List<WebElement> radioOrCheckboxElements = FindAll.findAll(elementBy);
        if (radioOrCheckboxElements.isEmpty()) {
            logger.error("Cannot locate radio button or checkbox containing text: " + Arrays.toString(radioOrCheckboxText) + " for:" + elementBy.toString());
            throw new NoSuchElementException("Cannot locate radio button or checkbox containing text: " + Arrays.toString(radioOrCheckboxText) + " for:" + elementBy.toString());
        }
        if (radioOrCheckboxText.length == 0) {
            logger.info("Select Radio Or Checkbox: select all enabled radio or checkbox");
            radioOrCheckboxElements.stream().filter(WebElement::isEnabled).forEach(element -> Click.clickOn(element, ClickType.JSClick));
            return;
        }
        List<WebElement> filteredRadioOrCheckboxElements = radioOrCheckboxElements.stream().filter(element -> element.getText().trim().equals(radioOrCheckboxText[0])).collect(Collectors.toList());
        for (WebElement filteredRadioOrCheckboxElement : filteredRadioOrCheckboxElements) {
            if (!filteredRadioOrCheckboxElement.isEnabled()) {
                logger.error("Cannot select readonly radio button or checkbox");
                throw new InvalidElementStateException("Cannot select readonly radio button or checkbox");
            }
            if (!filteredRadioOrCheckboxElement.isSelected()) {
                Click.clickOn(filteredRadioOrCheckboxElement, ClickType.JSClick);
            }
        }
        logger.info(String.format("Select Radio Or Checkbox: select all radio button or checkbox for '%s' element having '%s' text.",
                elementBy.toString(), Arrays.toString(radioOrCheckboxText)));
    }

    public void deselectCheckbox(By elementBy, String... checkboxText) {
        assert checkboxText != null;
        List<WebElement> checkboxElements = FindAll.findAll(elementBy);
        if (checkboxElements.isEmpty()) {
            logger.error("Deselect Checkbox: Cannot locate radio button or checkbox containing text: " + Arrays.toString(checkboxText) + " for:" + elementBy);
            throw new NoSuchElementException("Cannot locate radio button or checkbox containing text: " + Arrays.toString(checkboxText) + " for:" + elementBy);
        }
        if (checkboxText.length == 0) {
            logger.info("Deselect Checkbox: deselect all enabled and selected checkbox");
            checkboxElements.stream().filter(element -> element.isEnabled() & element.isSelected()).forEach(element -> Click.clickOn(element, ClickType.JSClick));
            return;
        }
        List<WebElement> filteredCheckboxElements = checkboxElements.stream().filter(element -> element.getText().trim().equals(checkboxText[0])).collect(Collectors.toList());
        for (WebElement filteredCheckboxElement : filteredCheckboxElements) {
            if (!filteredCheckboxElement.isEnabled()) {
                logger.error("Deselect Checkbox: Cannot deselect readonly checkbox button");
                throw new InvalidElementStateException("Cannot deselect readonly checkbox button");
            }
            if (!filteredCheckboxElement.isSelected()) {
                Click.clickOn(filteredCheckboxElement, ClickType.JSClick);
            }
        }
        logger.info(String.format("Deselect Checkbox: deselect all enabled and selected checkbox for '%s' element having '%s' text.",
                elementBy.toString(), Arrays.toString(checkboxText)));
    }
}
