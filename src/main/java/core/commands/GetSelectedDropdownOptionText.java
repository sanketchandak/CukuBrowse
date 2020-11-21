package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static core.commands.Find.Find;

public class GetSelectedDropdownOptionText {
    public static GetSelectedDropdownOptionText GetSelectedDropdownOptionText =
            ThreadLocal.withInitial(GetSelectedDropdownOptionText::new).get();

    private GetSelectedDropdownOptionText() {
        if (GetSelectedDropdownOptionText != null) {
            throw new RuntimeException("Use GetSelectedDropdownOptionText variable to get the single instance of this class.");
        }
    }

    public String getSelectedDropdownOptionText(By dropdownBy, By... dropdownChildOptionBy) {
        return getSelectedDropdownOptionText(Find.find(dropdownBy), dropdownChildOptionBy);
    }

    public String getSelectedDropdownOptionText(WebElement dropdownElement, By... dropdownChildOptionBy) {
        assert dropdownChildOptionBy != null;
        dropdownElement.click();
        return "select".equalsIgnoreCase(dropdownElement.getTagName()) ?
                new Select(dropdownElement).getFirstSelectedOption().getText() :
                dropdownChildOptionBy.length == 0 ?
                        dropdownElement.getText() :
                        Find.find(dropdownElement, dropdownChildOptionBy[0]).getText();
    }
}
