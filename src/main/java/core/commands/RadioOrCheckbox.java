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

    public WebElement getSelectedRadio(By radioField) {
        for (WebElement radio : FindAll.findAll(radioField)) {
            if (radio.getAttribute("checked") != null) {
                //getInstance().setElement(radio);
                return radio;
            }
        }
        return null;
    }

    public List<WebElement> getSelectedRadios(By radioField) {
        List<WebElement> selectedRadios = new ArrayList<>();
        for (WebElement radio : FindAll.findAll(radioField)) {
            if (radio.getAttribute("checked") != null) {
                //getInstance().setElement(radio);
                selectedRadios.add(radio);
            }
        }
        return selectedRadios;
    }

    public void selectRadioOrCheckbox(By elementBy, String... radioOrCheckboxText) {
        assert radioOrCheckboxText != null;
        List<WebElement> radioElements = FindAll.findAll(elementBy);
        if (radioElements.isEmpty()) {
            throw new NoSuchElementException("Cannot locate radio button having containing text: " + Arrays.toString(radioOrCheckboxText) + " for:" + elementBy);
        }
        if (radioOrCheckboxText.length == 0) {
            radioElements.stream().filter(WebElement::isEnabled).forEach(radioElement -> Click.clickOn(radioElement, ClickType.JSClick));
            return;
        }
        List<WebElement> filteredRadioElements = radioElements.stream().filter(element -> element.getText().contains(radioOrCheckboxText[0])).collect(Collectors.toList());
        for (WebElement filteredRadioElement : filteredRadioElements) {
            if (!filteredRadioElement.isEnabled()) {
                throw new InvalidElementStateException("Cannot select readonly radio button");
            }
            Click.clickOn(filteredRadioElement, ClickType.JSClick);
        }
    }
}
