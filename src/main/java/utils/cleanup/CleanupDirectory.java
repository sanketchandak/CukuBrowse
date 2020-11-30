package utils.cleanup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class CleanupDirectory {
    private static final Logger logger = LoggerFactory.getLogger(CleanupDirectory.class);
    private final String cleanReportDirFlag;
    private final String directoryPath;

    public CleanupDirectory(String cleanReportDirFlag, String directoryPath) {
        this.cleanReportDirFlag = cleanReportDirFlag;
        this.directoryPath = directoryPath;
    }

    public boolean cleanupDir() {
        Boolean flag = Boolean.FALSE;
        try {
            if (cleanReportDirFlag.toUpperCase().trim().equals("TRUE")) {
                Path directory = Paths.get(directoryPath);
                if (Files.isDirectory(directory)) {
                    Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                            Files.delete(file);
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                            Files.delete(dir);
                            return FileVisitResult.CONTINUE;
                        }
                    });
                    logger.warn("Clean up Report Directory: " + directoryPath + " cleaned up.");
                } else {
                    logger.warn("Clean up Report Directory: " + directoryPath + " directory doesn't exist for clean up.");
                    flag = Boolean.TRUE;
                }
            } else if (cleanReportDirFlag.toUpperCase().trim().equals("FALSE")) {
                logger.warn("Clean up Report Directory: Report clean up flag is set to FALSE.");
                flag = Boolean.TRUE;
            } else {
                logger.error("Clean up Report Directory: 1st parameters is Expected to be Boolean (TRUE?FALSE) value. It is used to decide whether to clean report dir." +
                        " Actual value is:" + cleanReportDirFlag);
                flag = Boolean.FALSE;
            }
        } catch (IOException e) {
            logger.error("Clean up Report Directory: Cleaned up Failed - Exception: " + e.toString(), e);
        }
        return flag;
    }
}
