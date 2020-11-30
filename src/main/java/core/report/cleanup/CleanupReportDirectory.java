package core.report.cleanup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.cleanup.CleanupDirectory;

import java.util.Arrays;

public class CleanupReportDirectory {
    private static final Logger logger = LoggerFactory.getLogger(CleanupReportDirectory.class);

    public static void main(String[] args) {
        try {
            if (args.length < 2) {
                logger.error("Clean up Report Directory: Invalid number of parameters provided. Expected 2 parameters. " +
                        "1st Boolean flag to decide whether to clean report dir or not and 2nd is path to report dir for cleanup");
            } else if (args.length == 2) {
                new CleanupDirectory(args[0], args[1]).cleanupDir();
            } else {
                logger.error("Clean up Report Directory: Invalid number of parameters provided. Expected 2 parameter only, but got " + args.length + " parameters as " + Arrays.toString(args) + ". " +
                        "1st is path to report dir for zip.");
            }
        } catch (Exception e) {
            logger.error("Clean up Report Directory: Cleaned up Failed - Exception: " + e.toString());
            e.printStackTrace();
        }
    }

}
