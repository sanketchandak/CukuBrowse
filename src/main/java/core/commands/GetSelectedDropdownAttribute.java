package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

import static core.commands.Find.Find;
import static core.commands.GetSelectedDropdownOptions.GetSelectedDropdownOptions;

public class GetSelectedDropdownAttribute {
    public static GetSelectedDropdownAttribute GetSelectedDropdownAttribute =
            ThreadLocal.withInitial(GetSelectedDropdownAttribute::new).get();

    private GetSelectedDropdownAttribute() {
        if (GetSelectedDropdownAttribute != null) {
            throw new RuntimeException("Use GetSelectedDropdownAttribute variable to get the single instance of this class.");
        }
    }

    public List<String> getSelectedDropdownAttribute(By dropdownBy, String attribute, By... dropdownChildOptionBy) throws Exception {
        return getSelectedDropdownAttribute(Find.find(dropdownBy), attribute, dropdownChildOptionBy);
    }

    public List<String> getSelectedDropdownAttribute(WebElement dropdownElement, String attribute, By... dropdownChildOptionBy) throws Exception {
        assert dropdownChildOptionBy != null;
        List<WebElement> elements = GetSelectedDropdownOptions.getSelectedDropdownOptions(dropdownElement, dropdownChildOptionBy);
        return elements.stream().map(element -> element.getAttribute(attribute)).collect(Collectors.toList());
    }
}
