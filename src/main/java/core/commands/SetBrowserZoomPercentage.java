package core.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static core.commands.ExecuteJavascript.ExecuteJavascript;

public class SetBrowserZoomPercentage {
    private static final Logger logger = LoggerFactory.getLogger(SetBrowserZoomPercentage.class);
    public static SetBrowserZoomPercentage SetBrowserZoomPercentage =
            ThreadLocal.withInitial(SetBrowserZoomPercentage::new).get();

    private SetBrowserZoomPercentage() {
        if (SetBrowserZoomPercentage != null) {
            logger.error("Use SetBrowserZoomPercentage variable to get the single instance of this class.");
            throw new RuntimeException("Use SetBrowserZoomPercentage variable to get the single instance of this class.");
        }
    }

    public void setBrowserZoomPercentage(String browserZoomLevel) {
        ExecuteJavascript.executeJavascript("document.body.style.zoom='" + browserZoomLevel + "%'");
        logger.info(String.format("Set Browser Zoom Percentage: set browser percentage to '%s'", browserZoomLevel));
    }

    public void resetBrowserZoomLevel() {
        ExecuteJavascript.executeJavascript("document.body.style.zoom='100%'");
        logger.info("Reset Browser Zoom level: reset browser percentage to 100%");
    }

}
