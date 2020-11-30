package core.commands;

import org.openqa.selenium.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static core.commands.FindAll.FindAll;
import static core.commands.Click.Click;

public class RadioOrCheckbox {
    public static RadioOrCheckbox RadioOrCheckbox =
            ThreadLocal.withInitial(RadioOrCheckbox::new).get();

    private RadioOrCheckbox() {
        if (RadioOrCheckbox != null) {
            throw new RuntimeException("Use PressTap variable to get the single instance of this class.");
        }
    }

    public WebElement getSelectedRadioOrCheckbox(By radioField) {
        for (WebElement radioOrCheckbox : FindAll.findAll(radioField)) {
            if (radioOrCheckbox.getAttribute("checked") != null || radioOrCheckbox.isSelected()) {
                return radioOrCheckbox;
            }
        }
        return null;
    }

    public List<WebElement> getSelectedRadiosOrCheckboxes(By radioField) {
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
            throw new NoSuchElementException("Cannot locate radio button or checkbox containing text: " + Arrays.toString(radioOrCheckboxText) + " for:" + elementBy);
        }
        if (radioOrCheckboxText.length == 0) {
            radioOrCheckboxElements.stream().filter(WebElement::isEnabled).forEach(element -> Click.clickOn(element, ClickType.JSClick));
            return;
        }
        List<WebElement> filteredRadioOrCheckboxElements = radioOrCheckboxElements.stream().filter(element -> element.getText().trim().equals(radioOrCheckboxText[0])).collect(Collectors.toList());
        for (WebElement filteredRadioOrCheckboxElement : filteredRadioOrCheckboxElements) {
            if (!filteredRadioOrCheckboxElement.isEnabled()) {
                throw new InvalidElementStateException("Cannot select readonly radio button or checkbox");
            }
            if(!filteredRadioOrCheckboxElement.isSelected()) {
                Click.clickOn(filteredRadioOrCheckboxElement, ClickType.JSClick);
            }
        }
    }

    public void deselectCheckbox(By elementBy, String... checkboxText) {
        assert checkboxText != null;
        List<WebElement> checkboxElements = FindAll.findAll(elementBy);
        if (checkboxElements.isEmpty()) {
            throw new NoSuchElementException("Cannot locate radio button or checkbox containing text: " + Arrays.toString(checkboxText) + " for:" + elementBy);
        }
        if (checkboxText.length == 0) {
            checkboxElements.stream().filter(element -> element.isEnabled() & element.isSelected()).forEach(element -> Click.clickOn(element, ClickType.JSClick));
            return;
        }
        List<WebElement> filteredCheckboxElements = checkboxElements.stream().filter(element -> element.getText().trim().equals(checkboxText[0])).collect(Collectors.toList());
        for (WebElement filteredCheckboxElement : filteredCheckboxElements) {
            if (!filteredCheckboxElement.isEnabled()) {
                throw new InvalidElementStateException("Cannot deselect readonly checkbox button");
            }
            if(!filteredCheckboxElement.isSelected()) {
                Click.clickOn(filteredCheckboxElement, ClickType.JSClick);
            }
        }
    }
}
