package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static core.commands.Find.Find;

public class GetSelectedDropdownOption {
    public static GetSelectedDropdownOption GetSelectedDropdownOption =
            ThreadLocal.withInitial(GetSelectedDropdownOption::new).get();

    private GetSelectedDropdownOption() {
        if (GetSelectedDropdownOption != null) {
            throw new RuntimeException("Use GetSelectedDropdownOption variable to get the single instance of this class.");
        }
    }

    public WebElement getSelectedDropdownOption(By dropdownBy, By... dropdownChildOptionBy) {
        return getSelectedDropdownOption(Find.find(dropdownBy), dropdownChildOptionBy);
    }

    public WebElement getSelectedDropdownOption(WebElement dropdownElement, By... dropdownChildOptionBy) {
        assert dropdownChildOptionBy != null;
        dropdownElement.click();
        return "select".equalsIgnoreCase(dropdownElement.getTagName()) ?
                new Select(dropdownElement).getFirstSelectedOption() :
                dropdownChildOptionBy.length == 0 ?
                        dropdownElement :
                        Find.find(dropdownElement, dropdownChildOptionBy[0]);
    }
}
