package core;

import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class CommanderElements {

    private final List<CommanderElement> elements = new ArrayList<>();

    CommanderElements(List<WebElement> elements) {
        setElements(elements);
    }

    public List<CommanderElement> getElements() {
        return elements;
    }

    public void setElements(List<WebElement> elements) {
        for (WebElement element : elements){
            this.elements.add(new CommanderElement(element));
        }
    }

}
