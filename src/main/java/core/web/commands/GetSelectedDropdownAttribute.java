package core.web.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static core.web.commands.Find.Find;
import static core.web.commands.GetInnerHtml.GetInnerHtml;
import static core.web.commands.GetSelectedDropdownOptions.GetSelectedDropdownOptions;

public class GetSelectedDropdownAttribute {
    private static final Logger logger = LoggerFactory.getLogger(GetSelectedDropdownAttribute.class);
    public static GetSelectedDropdownAttribute GetSelectedDropdownAttribute =
            ThreadLocal.withInitial(GetSelectedDropdownAttribute::new).get();

    private GetSelectedDropdownAttribute() {
        if (GetSelectedDropdownAttribute != null) {
            logger.error("Use GetSelectedDropdownAttribute variable to get the single instance of this class.");
            throw new RuntimeException("Use GetSelectedDropdownAttribute variable to get the single instance of this class.");
        }
    }

    public List<String> get(By dropdownBy, String attribute, By... dropdownChildOptionBy) {
        List<String> selectedDropdownAttribute = get(Find.findElement(dropdownBy), attribute, dropdownChildOptionBy);
        logger.info(String.format("Get Selected Dropdown Attribute: '%s' attribute of '%s' dropdown having child options: '%s' having attributes as '%s'",
                attribute,
                dropdownBy.toString(),
                (dropdownChildOptionBy.length != 0) ? Arrays.stream(dropdownChildOptionBy).map(By::toString).collect(Collectors.toList()) : "",
                selectedDropdownAttribute
        ));
        return selectedDropdownAttribute;
    }

    public List<String> get(WebElement dropdownElement, String attribute, By... dropdownChildOptionBy) {
        assert dropdownChildOptionBy != null;
        List<WebElement> elements = GetSelectedDropdownOptions.get(dropdownElement, dropdownChildOptionBy);
        List<String> selectedDropdownAttribute = elements.stream().map(element -> element.getAttribute(attribute)).collect(Collectors.toList());
        logger.info(String.format("Get Selected Dropdown Attribute: '%s' attribute of '%s' dropdown having child options: '%s' having attributes as '%s'",
                attribute,
                GetInnerHtml.get(dropdownElement),
                (dropdownChildOptionBy.length != 0) ? Arrays.stream(dropdownChildOptionBy).map(By::toString).collect(Collectors.toList()) : "",
                selectedDropdownAttribute
        ));
        return selectedDropdownAttribute;
    }
}
