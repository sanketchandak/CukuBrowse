package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static core.commands.Find.Find;

public class GetCssValue {
    public static GetCssValue GetCssValue =
            ThreadLocal.withInitial(GetCssValue::new).get();

    private GetCssValue() {
        if (GetCssValue != null) {
            throw new RuntimeException("Use GetCssValue variable to get the single instance of this class.");
        }
    }

    public String getCssValue(By elementBy, String cssPropertyName) {
        return getCssValue(Find.find(elementBy), cssPropertyName);
    }

    public String getCssValue(WebElement element, String cssPropertyName) {
        return element.getCssValue(cssPropertyName);
    }

    public Map<String, String> getCssValues(By elementBy, List<String> cssPropertyNames) {
        return getCssValues(Find.find(elementBy), cssPropertyNames);
    }

    public Map<String, String> getCssValues(WebElement element, List<String> cssPropertyNames) {
        return cssPropertyNames.stream().collect(Collectors.toMap(cssPropertyName -> cssPropertyName, element::getCssValue));
    }
}
