package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.Collections;
import java.util.List;

import static core.commands.Find.Find;
import static core.commands.FindAll.FindAll;

public class GetSelectedDropdownOptions {
    public static GetSelectedDropdownOptions GetSelectedDropdownOptions =
            ThreadLocal.withInitial(GetSelectedDropdownOptions::new).get();

    private GetSelectedDropdownOptions() {
        if (GetSelectedDropdownOptions != null) {
            throw new RuntimeException("Use GetSelectedDropdownOption variable to get the single instance of this class.");
        }
    }

    public List<WebElement> getSelectedDropdownOptions(By dropdownBy, By... dropdownChildOptionBy) {
        return getSelectedDropdownOptions(Find.find(dropdownBy), dropdownChildOptionBy);
    }

    public List<WebElement> getSelectedDropdownOptions(WebElement dropdownElement, By... dropdownChildOptionBy) {
        assert dropdownChildOptionBy != null;
        dropdownElement.click();
        return "select".equalsIgnoreCase(dropdownElement.getTagName()) ?
                new Select(dropdownElement).getAllSelectedOptions() :
                dropdownChildOptionBy.length == 0 ?
                        Collections.singletonList(dropdownElement) :
                        FindAll.findAll(dropdownElement, dropdownChildOptionBy[0]);
    }
}
