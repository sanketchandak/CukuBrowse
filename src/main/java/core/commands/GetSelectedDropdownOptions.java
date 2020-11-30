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

    public List<WebElement> getSelectedDropdownOptions(By dropdownBy, By... dropdownChildOptionBy) throws Exception {
        return getSelectedDropdownOptions(Find.find(dropdownBy), dropdownChildOptionBy);
    }

    public List<WebElement> getSelectedDropdownOptions(WebElement dropdownElement, By... dropdownChildOptionBy) throws Exception {
        assert dropdownChildOptionBy != null;
        if("select".equalsIgnoreCase(dropdownElement.getTagName())){
            return new Select(dropdownElement).getAllSelectedOptions();
        } else {
            dropdownElement.click();
            if(dropdownChildOptionBy.length == 0){
                throw new Exception("Element is not having 'Select' tag. 'dropdownChildOptionBy' argument value is required.");
            } else {
                return FindAll.findAll(dropdownElement, dropdownChildOptionBy[0]);
            }
        }
    }
}
