package utils.cleanup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class CleanupDirectory {
    private static final Logger logger = LoggerFactory.getLogger(CleanupDirectory.class);
    private final String cleanReportDirOrFileFlag;
    private final String directoryOrFilePath;

    public CleanupDirectory(String cleanReportDirOrFileFlag, String directoryOrFilePath) {
        this.cleanReportDirOrFileFlag = cleanReportDirOrFileFlag;
        this.directoryOrFilePath = directoryOrFilePath;
    }

    public boolean cleanupDirOrFile() {
        Boolean flag = Boolean.FALSE;
        try {
            if (cleanReportDirOrFileFlag.toUpperCase().trim().equals("TRUE")) {
                Path directoryOrFile = Paths.get(directoryOrFilePath);
                if (Files.isDirectory(directoryOrFile)) {
                    Files.walkFileTree(directoryOrFile, new SimpleFileVisitor<Path>() {
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
                    logger.warn("Clean up Directory: " + directoryOrFilePath + " cleaned up.");
                } else {
                    Files.delete(directoryOrFile);
                    logger.warn("Clean up File: " + directoryOrFilePath + " File deleted.");
                    flag = Boolean.TRUE;
                }
            } else if (cleanReportDirOrFileFlag.toUpperCase().trim().equals("FALSE")) {
                logger.warn("Clean up Directory: Directory or File clean up flag is set to FALSE.");
                flag = Boolean.TRUE;
            } else {
                logger.error("Clean up Directory or File: 1st parameters is Expected to be Boolean (TRUE?FALSE) value. It is used to decide whether to clean dir or file." +
                        " Actual value is:" + cleanReportDirOrFileFlag);
                flag = Boolean.FALSE;
            }
        } catch (IOException e) {
            logger.error("Clean up Directory Or File: Cleaned up Failed - Exception: " + e.toString(), e);
        }
        return flag;
    }

    public boolean cleanupFiles(int purgeDaysBack) {
        Boolean flag = Boolean.FALSE;
        try {
            if (cleanReportDirOrFileFlag.toUpperCase().trim().equals("TRUE")) {
                Path directoryOrFile = Paths.get(directoryOrFilePath);
                long purgeTime = System.currentTimeMillis() - (purgeDaysBack * 24 * 60 * 60 * 1000);
                if (Files.isDirectory(directoryOrFile)) {
                    Files.walkFileTree(directoryOrFile, new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                            if(file.toFile().lastModified() < purgeTime) {
                                Files.delete(file);
                            }
                            return FileVisitResult.CONTINUE;
                        }
                    });
                    logger.warn("Clean up Directory: " + directoryOrFilePath + " cleaned up.");
                } else {
                    if(directoryOrFile.toFile().lastModified() < purgeTime) {
                        Files.delete(directoryOrFile);
                        logger.warn("Clean up File: " + directoryOrFilePath + " File deleted.");
                        flag = Boolean.TRUE;
                    }
                }
            } else if (cleanReportDirOrFileFlag.toUpperCase().trim().equals("FALSE")) {
                logger.warn("Clean up Files: File clean up flag is set to FALSE.");
                flag = Boolean.TRUE;
            } else {
                logger.error("Clean up Files: 1st parameters is Expected to be Boolean (TRUE?FALSE) value. It is used to decide whether to clean files." +
                        " Actual value is:" + cleanReportDirOrFileFlag);
                flag = Boolean.FALSE;
            }
        } catch (IOException e) {
            logger.error("Clean up Files: Cleaned up Failed - Exception: " + e.toString(), e);
        }
        return flag;
    }
}
