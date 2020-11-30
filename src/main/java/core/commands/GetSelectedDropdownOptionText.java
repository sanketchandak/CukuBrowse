package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static core.commands.Find.Find;
import static core.commands.GetSelectedDropdownOption.GetSelectedDropdownOption;

public class GetSelectedDropdownOptionText {
    public static GetSelectedDropdownOptionText GetSelectedDropdownOptionText =
            ThreadLocal.withInitial(GetSelectedDropdownOptionText::new).get();

    private GetSelectedDropdownOptionText() {
        if (GetSelectedDropdownOptionText != null) {
            throw new RuntimeException("Use GetSelectedDropdownOptionText variable to get the single instance of this class.");
        }
    }

    public String getSelectedDropdownOptionText(By dropdownBy, By... dropdownChildOptionBy) throws Exception {
        return getSelectedDropdownOptionText(Find.find(dropdownBy), dropdownChildOptionBy);
    }

    public String getSelectedDropdownOptionText(WebElement dropdownElement, By... dropdownChildOptionBy) throws Exception {
        return GetSelectedDropdownOption.getSelectedDropdownOption(dropdownElement, dropdownChildOptionBy).getText();
    }
}
