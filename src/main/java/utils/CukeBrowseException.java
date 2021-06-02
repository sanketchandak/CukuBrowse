package utils;

import org.openqa.selenium.WebDriverException;

public class CukeBrowseException extends WebDriverException {
    public CukeBrowseException(String message) {
        super(message);
    }

    public CukeBrowseException(String message, Throwable cause) {
        super(message, cause);
    }
}

