package core.report.zip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.zip.FileSizeUnits;
import utils.zip.ZipFilesDirectory;

import java.util.Arrays;

public class ZipExecutionReportDirectory {
    private static final Logger logger = LoggerFactory.getLogger(ZipExecutionReportDirectory.class);

    public static void main(String[] args) {
        try {
            if (args.length < 2) {
                logger.error("Zip Report Directory: Invalid number of parameters provided. Expected 2 parameters. " +
                        "1st is path of report dir to zip & 2nd is path to store zip file with name.");
            } else if (args.length == 2) {
                ZipFilesDirectory zipFilesDirectory = new ZipFilesDirectory(args[1]);
                boolean flag = zipFilesDirectory.createZipOfFolder(args[0]);
                if (flag && zipFilesDirectory.getZipFile().length() / FileSizeUnits.MB.getUnitSize() > 10) {
                    zipFilesDirectory.deleteZipFile();
                    flag = zipFilesDirectory.createSplitZipOfFolder(args[0], 10, FileSizeUnits.MB);

                }
                if (flag) {
                    logger.info("Zip Report Directory: Zip created successfully of: " + args[0] + " at: " + args[1]);
                } else {
                    logger.error("Zip Report Directory: Zip creation failed of: " + args[0] + " at: " + args[1]);
                }
            } else {
                logger.error("Zip Report Directory: Invalid number of parameters provided. Expected 2 parameter only, but got " + args.length + " parameters as " + Arrays.asList(args) + ". " +
                        "1st is path of report dir to zip & 2nd is path to store zip file with name.");
            }
        } catch (Exception e) {
            logger.error("Zip Report Directory: Zipping Failed - Exception: " + e.toString());
            e.printStackTrace();
        }
    }

}
