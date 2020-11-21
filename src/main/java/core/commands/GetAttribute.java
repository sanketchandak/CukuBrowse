package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static core.commands.Find.Find;

public class GetAttribute {

    public static GetAttribute GetAttribute =
            ThreadLocal.withInitial(GetAttribute::new).get();

    private GetAttribute() {
        if (GetAttribute != null) {
            throw new RuntimeException("Use GetAttribute variable to get the single instance of this class.");
        }
    }

    public String getAttribute(By elementBy, String attribute) {
        return getAttribute(Find.find(elementBy), attribute);
    }

    public String getAttribute(WebElement element, String attribute) {
        return element.getAttribute(attribute);
    }

    public Map<String, String> getAttributes(By elementBy, List<String> attributes) {
        return getAttributes(Find.find(elementBy), attributes);
    }

    public Map<String, String> getAttributes(WebElement element, List<String> attributes) {
        return attributes.stream().collect(Collectors.toMap(attribute -> attribute, element::getAttribute));
    }
}
