package core.commands;

import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;

import static java.lang.System.nanoTime;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static core.commands.GetInnerHtml.GetInnerHtml;

public class UploadFile {
    public static UploadFile UploadFile =
            ThreadLocal.withInitial(UploadFile::new).get();

    private UploadFile() {
        if (UploadFile != null) {
            throw new RuntimeException("Use UploadFile variable to get the single instance of this class.");
        }
    }

    public void uploadFile(WebElement inputWebElement, File fileToUpload) {
        checkFilesExist(fileToUpload);
        checkValidInputField(inputWebElement);
        String fileNames = canonicalPath(fileToUpload);
        uploadFiles(inputWebElement, fileNames);
    }

    private void checkFilesExist(File fileToUpload) {
        if (!fileToUpload.exists()) {
            throw new IllegalArgumentException("File not found: " + fileToUpload.getAbsolutePath());
        }
    }

    private String canonicalPath(File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot get canonical path of file " + file, e);
        }
    }

    private void uploadFiles(WebElement inputField, String fileNames) {
        Stopwatch stopwatch = new Stopwatch(60000);
        do {
            try {
                inputField.sendKeys(fileNames);
                break;
            } catch (ElementNotInteractableException notInteractable) {
                if (stopwatch.isTimeoutReached()) {
                    throw notInteractable;
                }
                stopwatch.sleep(2000);
            }
        } while (!stopwatch.isTimeoutReached());
    }

    private void checkValidInputField(WebElement inputElement) {
        if (!"input".equalsIgnoreCase(inputElement.getTagName())) {
            throw new IllegalArgumentException("Cannot upload file because '" + GetInnerHtml.getInnerHtml(inputElement) + "' is not an INPUT");
        }
    }
}

class Stopwatch {
    private final long endTimeNano;

    Stopwatch(long timeoutMs) {
        this.endTimeNano = nanoTime() + MILLISECONDS.toNanos(timeoutMs);
    }

    boolean isTimeoutReached() {
        return nanoTime() > endTimeNano;
    }

    void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
