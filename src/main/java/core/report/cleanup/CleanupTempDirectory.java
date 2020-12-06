package core.report.cleanup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;

public class CleanupTempDirectory {
    private static final Logger logger = LoggerFactory.getLogger(CleanupTempDirectory.class);

    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                logger.error("Clean up Report Directory: Invalid number of parameters provided. Expected 1 parameters. " +
                        "1st is path to BAT file which will cleanup temp dir.");
            } else if (args.length == 1) {
                if(System.getProperty("os.name").toLowerCase().contains("win")) {
                    String cleanUpTempRelativePath = args[0];
                    if (!cleanUpTempRelativePath.contains(System.getProperty("user.dir"))) {
                        cleanUpTempRelativePath = System.getProperty("user.dir") + File.separator + cleanUpTempRelativePath;
                    }
                    Runtime.getRuntime().exec("cmd /C start /MIN " + cleanUpTempRelativePath);
                    logger.info("Clean up 'Temp' Directory: Directory cleaned up successfully on window.");
                } else {
                    logger.info("Clean up 'Temp' Directory: Directory can not cleaned as it's not window OS.");
                }
            } else {
                logger.error("Clean up Report Directory: Invalid number of parameters provided. Expected 1 parameter only, but got " + args.length + " parameters as " + Arrays.toString(args) + ". " +
                        "1st is path to BAT file which will cleanup temp dir.");
            }
        } catch (Exception e) {
            logger.error("Clean up 'Temp' Directory: Cleaned up Failed - Exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
