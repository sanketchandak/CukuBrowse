package core.web.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static core.web.commands.Find.Find;

public class GetCssValue {
    private static final Logger logger = LoggerFactory.getLogger(GetCssValue.class);
    public static GetCssValue GetCssValue =
            ThreadLocal.withInitial(GetCssValue::new).get();

    private GetCssValue() {
        if (GetCssValue != null) {
            logger.error("Use GetCssValue variable to get the single instance of this class.");
            throw new RuntimeException("Use GetCssValue variable to get the single instance of this class.");
        }
    }

    public String get(By elementBy, String cssPropertyName) {
        String retrievedCssValue = Find.findElement(elementBy).getCssValue(cssPropertyName);
        logger.info(String.format("Get CSS Value: css value: '%s' of element: '%s' having value as '%s'", cssPropertyName, elementBy.toString(), retrievedCssValue));
        return retrievedCssValue;
    }

    public String get(WebElement element, String cssPropertyName) {
        String retrievedCssValue = element.getCssValue(cssPropertyName);
        logger.info(String.format("Get CSS Value: css value: '%s' of element: '%s' having value as '%s'", cssPropertyName, element, retrievedCssValue));
        return retrievedCssValue;
    }

    public Map<String, String> get(By elementBy, List<String> cssPropertyNames) {
        Map<String, String> retrievedCssValues = get(Find.findElement(elementBy), cssPropertyNames);
        logger.info(String.format("Get CSS Values: css values: '%s' of element: '%s' having values as '%s'", cssPropertyNames, elementBy.toString(), retrievedCssValues));
        return retrievedCssValues;
    }

    public Map<String, String> get(WebElement element, List<String> cssPropertyNames) {
        Map<String, String> retrievedCssValues = cssPropertyNames.stream().collect(Collectors.toMap(cssPropertyName -> cssPropertyName, element::getCssValue));
        logger.info(String.format("Get CSS Values: css values: '%s' of element: '%s' having values as '%s'", cssPropertyNames, element, retrievedCssValues));
        return retrievedCssValues;
    }
}
