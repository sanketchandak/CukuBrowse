package core.web.commands;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CukeBrowseException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static core.web.commands.Find.Find;

public class GetAttribute {
    private static final Logger logger = LoggerFactory.getLogger(GetAttribute.class);
    public static GetAttribute GetAttribute =
            ThreadLocal.withInitial(GetAttribute::new).get();

    private GetAttribute() {
        if (GetAttribute != null) {
            logger.error("Use GetAttribute variable to get the single instance of this class.");
            throw new CukeBrowseException("Use GetAttribute variable to get the single instance of this class.");
        }
    }

    public String get(By elementBy, String attribute) {
        String retrievedAttributeValue = Find.findElement(elementBy).getAttribute(attribute);
        logger.info(String.format("Get Attribute: Attribute: '%s' of element: '%s' having value as '%s'", attribute, elementBy.toString(), retrievedAttributeValue));
        return retrievedAttributeValue;
    }

    public String get(WebElement element, String attribute) {
        String retrievedAttributeValue = element.getAttribute(attribute);
        logger.info(String.format("Get Attribute: Attribute: '%s' of element: '%s' having value as '%s'", attribute, element, retrievedAttributeValue));
        return retrievedAttributeValue;
    }

    public Map<String, String> get(By elementBy, List<String> attributes) {
        Map<String, String> retrievedAttributesValues = get(Find.findElement(elementBy), attributes);
        logger.info(String.format("Get Attributes: Attribute: '%s' of element: '%s' having values as '%s'", attributes, elementBy.toString(), retrievedAttributesValues));
        return retrievedAttributesValues;
    }

    public Map<String, String> get(WebElement element, List<String> attributes) {
        Map<String, String> retrievedAttributesValues = attributes.stream().collect(Collectors.toMap(attribute -> attribute, element::getAttribute));
        logger.info(String.format("Get Attributes: Attribute: '%s' of element: '%s' having values as '%s'", attributes, element, retrievedAttributesValues));
        return retrievedAttributesValues;
    }
}
