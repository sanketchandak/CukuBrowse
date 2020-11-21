package core.report.cleanup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class CleanupTempDirectory {
    private static final Logger logger = LoggerFactory.getLogger(CleanupTempDirectory.class);

    public static void main(String[] args) {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                String cleanUpTempRelativePath = System.getProperty("user.dir") + File.separator + "src/main/resources/cleanup/cleantempdir.bat";
                Runtime.getRuntime().exec("cmd /C start /MIN " + cleanUpTempRelativePath);
                logger.info("Clean up 'Temp' Directory: Directory cleaned up successfully on window.");
            }
        } catch (Exception e) {
            logger.error("Clean up 'Temp' Directory: Cleaned up Failed - Exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
