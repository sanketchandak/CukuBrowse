package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

import static core.commands.Find.Find;
import static core.commands.GetSelectedDropdownOptions.GetSelectedDropdownOptions;

public class GetSelectedDropdownOptionsText {
    public static GetSelectedDropdownOptionsText GetSelectedDropdownOptionsText =
            ThreadLocal.withInitial(GetSelectedDropdownOptionsText::new).get();

    private GetSelectedDropdownOptionsText() {
        if (GetSelectedDropdownOptionsText != null) {
            throw new RuntimeException("Use GetSelectedDropdownOptionsText variable to get the single instance of this class.");
        }
    }

    public List<String> getSelectedDropdownOptionsText(By dropdownBy, By... dropdownChildOptionBy) throws Exception {
        return getSelectedDropdownOptionsText(Find.find(dropdownBy), dropdownChildOptionBy);
    }

    public List<String> getSelectedDropdownOptionsText(WebElement dropdownElement, By... dropdownChildOptionBy) throws Exception {
        List<WebElement> selectedOptions = GetSelectedDropdownOptions.getSelectedDropdownOptions(dropdownElement, dropdownChildOptionBy);
        return selectedOptions.stream().map(WebElement::getText).collect(Collectors.toList());
    }
}
