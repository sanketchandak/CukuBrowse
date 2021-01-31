package core.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static core.commands.Find.Find;

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

    public String getCssValue(By elementBy, String cssPropertyName) {
        String retrievedCssValue = Find.find(elementBy).getCssValue(cssPropertyName);
        logger.info(String.format("Get CSS Value: css value: '%s' of element: '%s' having value as '%s'", cssPropertyName, elementBy.toString(), retrievedCssValue));
        return retrievedCssValue;
    }

    public String getCssValue(WebElement element, String cssPropertyName) {
        String retrievedCssValue = element.getCssValue(cssPropertyName);
        logger.info(String.format("Get CSS Value: css value: '%s' of element: '%s' having value as '%s'", cssPropertyName, element.toString(), retrievedCssValue));
        return retrievedCssValue;
    }

    public Map<String, String> getCssValues(By elementBy, List<String> cssPropertyNames) {
        Map<String, String> retrievedCssValues = getCssValues(Find.find(elementBy), cssPropertyNames);
        logger.info(String.format("Get CSS Values: css values: '%s' of element: '%s' having values as '%s'", cssPropertyNames, elementBy.toString(), retrievedCssValues));
        return retrievedCssValues;
    }

    public Map<String, String> getCssValues(WebElement element, List<String> cssPropertyNames) {
        Map<String, String> retrievedCssValues = cssPropertyNames.stream().collect(Collectors.toMap(cssPropertyName -> cssPropertyName, element::getCssValue));
        logger.info(String.format("Get CSS Values: css values: '%s' of element: '%s' having values as '%s'", cssPropertyNames, element.toString(), retrievedCssValues));
        return retrievedCssValues;
    }
}
