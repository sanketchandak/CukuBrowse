package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static core.commands.Find.Find;
import static core.commands.FindAll.FindAll;

public class GetSelectedDropdownOption {
    public static GetSelectedDropdownOption GetSelectedDropdownOption =
            ThreadLocal.withInitial(GetSelectedDropdownOption::new).get();

    private GetSelectedDropdownOption() {
        if (GetSelectedDropdownOption != null) {
            throw new RuntimeException("Use GetSelectedDropdownOption variable to get the single instance of this class.");
        }
    }

    public WebElement getSelectedDropdownOption(By dropdownBy, By... dropdownChildOptionBy) throws Exception {
        return getSelectedDropdownOption(Find.find(dropdownBy), dropdownChildOptionBy);
    }

    public WebElement getSelectedDropdownOption(WebElement dropdownElement, By... dropdownChildOptionBy) throws Exception {
        assert dropdownChildOptionBy != null;
        if("select".equalsIgnoreCase(dropdownElement.getTagName())){
            return new Select(dropdownElement).getFirstSelectedOption();
        } else {
            dropdownElement.click();
            if(dropdownChildOptionBy.length == 0){
                throw new Exception("Element is not having 'Select' tag. 'dropdownChildOptionBy' argument value is required.");
            } else {
                return Find.find(dropdownElement, dropdownChildOptionBy[0]);
            }
        }
    }
}
