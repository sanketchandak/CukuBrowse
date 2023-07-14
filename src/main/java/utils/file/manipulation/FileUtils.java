package utils.file.manipulation;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CukeBrowseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.*;
import java.util.stream.Stream;

public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static void createFolderIfNotPresent(String directoryPath) throws IOException {
        File directory = new File(directoryPath);
        boolean createdFlag = false;
        if(!directory.exists()){
            createdFlag = directory.mkdirs();
            if (!createdFlag) {
                throw new IOException("Failed to create directory: " + directoryPath);
            }
        }
        logger.info("Folder '"+directoryPath+"' "+ (createdFlag ? "created" : "already present"));
    }

    public static String getOSFriendlyFilePath(String path){
        return path.replace("//", File.separator).replace("/", File.separator).replace("\\",File.separator);
    }

    public static synchronized void deleteAllFilesHavingName(String directoryPath, String fileNameStartsWith) throws IOException {
        try (Stream<Path> paths = Files.list(Paths.get(Objects.requireNonNull(getOSFriendlyFilePath(directoryPath))))) {
            paths.filter(p -> p.getFileName().toString().startsWith(fileNameStartsWith) && !Files.isDirectory(p))
                    .forEach(p -> {
                        try {
                            Files.deleteIfExists(p);
                        } catch (Exception e){
                            logger.error("Exception while deleting file name starts with '"+fileNameStartsWith+"' in '"+directoryPath+"' directory: "+ Arrays.toString(e.getStackTrace()));
                        }
                    });
        }
        logger.info("Successfully deleted files where name starts with '"+fileNameStartsWith+"' in '"+directoryPath+"' directory");
    }

    public static void renameFile(String filePath, String newFileName) {
        File file = new File(getOSFriendlyFilePath(filePath));
        if (file.exists()) {
            String parentDirectory = file.getParent();
            String newFilePath = parentDirectory + File.separator + newFileName;
            File newFile = new File(newFilePath);
            if(!file.renameTo(newFile)){
                logger.error("Fail to rename file '"+filePath+"' as '"+newFileName+"'");
            }
        }
    }

    public static List<String> listFilesInDirectory(String directoryPath) {
        List<String> fileList = new ArrayList<>();
        File directory = new File(getOSFriendlyFilePath(directoryPath));
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        fileList.add(file.getName());
                    }
                }
            }
        }
        return fileList;
    }

    public static String readTextFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(getOSFriendlyFilePath(filePath)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    public static void writeTextFile(String filePath, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getOSFriendlyFilePath(filePath)))) {
            writer.write(content);
        }
    }

    public static List<String[]> readCSV(String filePath) throws IOException, CsvException {
        return readCSV(filePath, '\u0000');
    }

    public static List<String[]> readCSV(String filePath, char csvSeparator) throws IOException, CsvException {
        boolean validFileStatus = checkValidFile(filePath);
        if(!validFileStatus || !filePath.endsWith(".csv")) {
            throw new CukeBrowseException("'" + filePath + "' is either doesn't exist or not '.csv' file or is a directory");
        }
        CSVParser parser;
        CSVReader csvReader;
        if(csvSeparator != '\u0000') {
            parser = new CSVParserBuilder()
                    .withQuoteChar('\"')
                    .build();
        } else {
            parser = new CSVParserBuilder().withSeparator(csvSeparator)
                    .withQuoteChar('\"')
                    .build();
        }
        csvReader = new CSVReaderBuilder(new FileReader(getOSFriendlyFilePath(filePath)))
                .withCSVParser(parser)
                .build();
        return csvReader.readAll();
    }

    public static boolean checkValidFile(String fileName) {
        boolean valid = true;
        try {
            File f = new File(getOSFriendlyFilePath(fileName));
            if (!f.exists() || f.isDirectory()) {
                valid = false;
            }
        } catch (Exception e) {
            valid = false;
        }
        return valid;
    }

    public static double getFileSize(String filePath, DataUnit unit) {
        File file = new File(getOSFriendlyFilePath(filePath));
        long fileSize = file.length();
        return (double) fileSize/unit.getBytes();
    }

    public static String getFilePermissions(String filePath) {
        File file = new File(getOSFriendlyFilePath(filePath));
        Set<PosixFilePermission> permissions;
        try {
            Path path = file.toPath();
            permissions = Files.getPosixFilePermissions(path);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return PosixFilePermissions.toString(permissions);
    }

    public static void copyFile(String sourceFilePath, String destinationFilePath) throws IOException {
        File sourceFile = new File(sourceFilePath);
        File destinationFile = new File(destinationFilePath);
        Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}
