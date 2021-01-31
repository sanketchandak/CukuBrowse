package core.commands;

import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static java.lang.System.nanoTime;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static core.commands.GetInnerHtml.GetInnerHtml;

public class UploadFile {
    private static final Logger logger = LoggerFactory.getLogger(UploadFile.class);
    public static UploadFile UploadFile =
            ThreadLocal.withInitial(UploadFile::new).get();

    private UploadFile() {
        if (UploadFile != null) {
            logger.error("Use UploadFile variable to get the single instance of this class.");
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
            logger.error("Check Files Exist: File not found: " + fileToUpload.getAbsolutePath());
            throw new IllegalArgumentException("File not found: " + fileToUpload.getAbsolutePath());
        }
    }

    private String canonicalPath(File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            logger.error("Canonical Path: Cannot get canonical path of file " + file + " error as: " + Arrays.toString(e.getStackTrace()));
            throw new IllegalArgumentException("Cannot get canonical path of file " + file, e);
        }
    }

    private void uploadFiles(WebElement inputField, String fileNames) {
        Stopwatch stopwatch = new Stopwatch(60000);
        do {
            try {
                inputField.sendKeys(fileNames);
                logger.info(String.format("Upload Files: upload file to '%s' element with '%s' filename", GetInnerHtml.getInnerHtml(inputField), fileNames));
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
            logger.error("Cannot upload file because '" + GetInnerHtml.getInnerHtml(inputElement) + "' is not an INPUT");
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
