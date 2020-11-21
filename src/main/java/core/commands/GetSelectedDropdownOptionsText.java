package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static core.commands.Find.Find;
import static core.commands.FindAll.FindAll;

public class GetSelectedDropdownOptionsText {
    public static GetSelectedDropdownOptionsText GetSelectedDropdownOptionsText =
            ThreadLocal.withInitial(GetSelectedDropdownOptionsText::new).get();

    private GetSelectedDropdownOptionsText() {
        if (GetSelectedDropdownOptionsText != null) {
            throw new RuntimeException("Use GetSelectedDropdownOptionsText variable to get the single instance of this class.");
        }
    }

    public List<String> getSelectedDropdownOptionsText(By dropdownBy, By... dropdownChildOptionBy) {
        return getSelectedDropdownOptionsText(Find.find(dropdownBy), dropdownChildOptionBy);
    }

    public List<String> getSelectedDropdownOptionsText(WebElement dropdownElement, By... dropdownChildOptionBy) {
        assert dropdownChildOptionBy != null;
        dropdownElement.click();
        List<WebElement> selectedOptions = "select".equalsIgnoreCase(dropdownElement.getTagName()) ?
                new Select(dropdownElement).getAllSelectedOptions() :
                dropdownChildOptionBy.length == 0 ?
                        Collections.singletonList(dropdownElement) :
                        FindAll.findAll(dropdownElement, dropdownChildOptionBy[0]);
        return selectedOptions.stream().map(WebElement::getText).collect(Collectors.toList());
    }
}
