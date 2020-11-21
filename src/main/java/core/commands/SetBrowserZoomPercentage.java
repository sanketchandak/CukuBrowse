package core.commands;

import static core.commands.ExecuteJavascript.ExecuteJavascript;

public class SetBrowserZoomPercentage {
    public static SetBrowserZoomPercentage SetBrowserZoomPercentage =
            ThreadLocal.withInitial(SetBrowserZoomPercentage::new).get();

    private SetBrowserZoomPercentage() {
        if (SetBrowserZoomPercentage != null) {
            throw new RuntimeException("Use SetBrowserZoomPercentage variable to get the single instance of this class.");
        }
    }

    public void setBrowserZoomPercentage(String browserZoomLevel) {
        ExecuteJavascript.executeJavascript("document.body.style.zoom='" + browserZoomLevel + "%'");
    }

    public void resetBrowserZoomLevel() {
        ExecuteJavascript.executeJavascript("document.body.style.zoom='100%'");
    }

}
