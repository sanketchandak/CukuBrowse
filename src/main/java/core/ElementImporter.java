package core;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Helper;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElementImporter {

    private static final Logger logger = LoggerFactory.getLogger(ElementImporter.class);
    final Map<String, Boolean> importedFiles = new HashMap<>();
    private final Table<String, String, String> elementsTable = TreeBasedTable.create(String.CASE_INSENSITIVE_ORDER, String.CASE_INSENSITIVE_ORDER);

    public static ElementImporter ElementImporter =
            ThreadLocal.withInitial(ElementImporter::new).get();

    private ElementImporter() {
        if (ElementImporter != null) {
            logger.error("Use ElementImporter variable to get the single instance of this class.");
            throw new RuntimeException("Use ElementImporter variable to get the single instance of this class.");
        }
    }

    public void importElements(List<String> files) {
        files.forEach(fileName ->
                {
                    fileName = Helper.removeQuotes(fileName);
                    File fileToImport = Helper.resolveFileSafely(fileName);
                    if (!hasBeenImported(fileToImport)) {
                        try {
                            importElementsFromDictionary(fileToImport);
                            importedFiles.put(fileToImport.toURI().toURL().toString(), true);
                        } catch (Exception ignore) {
                        }
                    }
                }
        );
    }

    public By getElement(String elementName) {
        Map<String, String> idsElementsData = elementsTable.row(elementName);
        if (idsElementsData.containsKey("id")) {
            return By.id(idsElementsData.get("id"));
        }
        if (idsElementsData.containsKey("css")) {
            return By.cssSelector(idsElementsData.get("css"));
        }
        if (idsElementsData.containsKey("cssSelector")) {
            return By.cssSelector(idsElementsData.get("cssSelector"));
        }
        if (idsElementsData.containsKey("xpath")) {
            return By.xpath(idsElementsData.get("xpath"));
        }
        if (idsElementsData.containsKey("className")) {
            return By.className(idsElementsData.get("className"));
        }
        if (idsElementsData.containsKey("tagName")) {
            return By.tagName(idsElementsData.get("tagName"));
        }
        if (idsElementsData.containsKey("name")) {
            return By.name(idsElementsData.get("name"));
        }
        if (idsElementsData.containsKey("linkText")) {
            return By.linkText(idsElementsData.get("linkText"));
        }
        if (idsElementsData.containsKey("partialLinkText")) {
            return By.partialLinkText(idsElementsData.get("partialLinkText"));
        }
        throw new IllegalArgumentException("Invalid identifier present for '"+elementName+"'. " +
                "Valid identifiers are id, css, cssSelector, xpath, className, tagName, name, linkText and partialLinkText. " +
                "Actual identifiers is/are: "+elementsTable);
    }

    boolean hasBeenImported(File fileToImport) {
        boolean isFileAlreadyImported = false;
        try {
            isFileAlreadyImported = importedFiles.containsKey(fileToImport.toURI().toURL().toString());
            return isFileAlreadyImported;
        } catch (Exception ignore) {
        }
        return isFileAlreadyImported;
    }

    void importElementsFromDictionary(File fileToImport) {
        try {
            List<String> lines = Files.readAllLines(fileToImport.toPath());
            lines.stream().
                    map(String::trim).
                    filter(line -> !(line.startsWith("#") || line.equals(""))).
                    map(line -> line.split("\\|")).
                    forEach(line -> elementsTable.put(line[1].trim(), line[2].replaceAll("[\\s\\n\\r]*","").trim(), line[3].trim()));
        } catch (Exception ignore) {
        }
    }
}
