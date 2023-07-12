package utils.sikuli;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;

import java.util.List;

public class SikuliUtils {

    private Screen screen;

    public SikuliUtils() {
        screen = new Screen();
    }

    public Match find(String imagePath) throws FindFailed {
        return find(imagePath, 10);
    }

    public Match find(String imagePath, double timeoutWait) throws FindFailed {
        Pattern pattern = new Pattern(imagePath);
        return screen.wait(pattern, timeoutWait);
    }

    public List<Match> findAll(String imagePath) throws FindFailed {
        Pattern pattern = new Pattern(imagePath);
        return screen.findAllList(pattern);
    }

    public boolean clickElement(String imagePath) throws FindFailed {
        return clickElement(imagePath, 10);
    }

    public boolean clickElement(String imagePath, double timeoutWait) throws FindFailed {
        Match match = find(imagePath, timeoutWait);
        if (match != null) {
            match.click();
            return true;
        }
        return false;
    }

    public boolean doubleClickElement(String imagePath) throws FindFailed {
        return doubleClickElement(imagePath, 10);
    }

    public boolean doubleClickElement(String imagePath, double timeoutWait) throws FindFailed {
        Match match = find(imagePath, timeoutWait);
        if (match != null) {
            match.doubleClick();
            return true;
        }
        return false;
}

    public boolean rightClickElement(String imagePath) throws FindFailed {
        return rightClickElement(imagePath, 10);
    }

    public boolean rightClickElement(String imagePath, double timeoutWait) throws FindFailed {
        Match match = find(imagePath, timeoutWait);
        if (match != null) {
            match.rightClick();
            return true;
        }
        return false;
    }

    public boolean dragAndDrop(String sourceImagePath, String targetImagePath) throws FindFailed {
        return dragAndDrop(sourceImagePath, targetImagePath, 10);
    }

    public boolean dragAndDrop(String sourceImagePath, String targetImagePath, double timeoutWait) throws FindFailed {
        Pattern targetPattern = new Pattern(targetImagePath);
        Match sourceMatch = find(sourceImagePath, timeoutWait);
        Match targetMatch = screen.wait(targetPattern, timeoutWait);
        if (sourceMatch != null && targetMatch != null) {
            sourceMatch.drag(targetPattern);
            return true;
        }
        return false;
    }

    public boolean isElementPresent(String imagePath) {
        Pattern pattern = new Pattern(imagePath);
        return screen.exists(pattern) != null;
    }

    public boolean typeText(String imagePath, String text) throws FindFailed {
        return typeText(imagePath, text, 10);
    }

    public boolean typeText(String imagePath, String text, double timeoutWait) throws FindFailed {
        Match match = find(imagePath, timeoutWait);
        if (match != null) {
            match.type(text);
            return true;
        }
        return false;
    }

    public boolean hover(String imagePath) throws FindFailed {
        return hover(imagePath, 10);
    }

    public boolean hover(String imagePath, double timeoutWait) throws FindFailed {
        Match match = find(imagePath, timeoutWait);
        if (match != null) {
            match.hover();
            return true;
        }
        return false;
    }

    public void waitUntilImageVanishes(String imagePath, int timeoutSeconds) {
        Pattern pattern = new Pattern(imagePath);
        screen.waitVanish(pattern, timeoutSeconds);
    }
}
