package utils.zip;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.AbstractFileHeader;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import net.lingala.zip4j.progress.ProgressMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class ZipFilesDirectory {

    private static final Logger logger = LoggerFactory.getLogger(ZipFilesDirectory.class);
    private File zipFile;
    private char[] password;

    public ZipFilesDirectory(String zipFilePathWithName) {
        zipFilePathWithName = !zipFilePathWithName.endsWith(".zip") ? zipFilePathWithName.concat(".zip") : zipFilePathWithName;
        zipFile = new File(zipFilePathWithName);
    }

    public void setZipFile(String zipFilePathWithName) {
        zipFilePathWithName = !zipFilePathWithName.endsWith(".zip") ? zipFilePathWithName.concat(".zip") : zipFilePathWithName;
        zipFile = new File(zipFilePathWithName);
    }

    public void setZipFile(File zipFilePathWithName) {
        zipFile = zipFilePathWithName;
    }

    public File getZipFile() {
        return zipFile;
    }

    public void setPassword(String password) {
        this.password = password.toCharArray();
    }

    private enum FileDirType {
        FILE("FILE"), DIRECTORY("DIRECTORY");
        private final String type;

        public String getType() {
            return this.type;
        }

        FileDirType(String type) {
            this.type = type;
        }
    }

    public boolean isZipFileExist() {
        if (zipFile == null) {
            throw new NullPointerException();
        }
        try {
            return zipFile.isFile();
        } catch (SecurityException | NullPointerException e) {
            logger.error("Is Zip File Exist: Check if zip file exist. " + e.getMessage(), e);
        }
        return false;
    }

    public boolean deleteZipFile() {
        if (zipFile == null) {
            logger.warn("Delete Zip File: Delete zip file failed as file to delete is not assigned.");
            return false;
        }
        boolean flag = false;
        try {
            ZipFile zip4JFile = new ZipFile(zipFile);
            if (zip4JFile.isSplitArchive()) {
                for (File splitFile : zip4JFile.getSplitZipFiles()) {
                    splitFile.delete();
                }
            }
            if (isZipFileExist()) {
                if (zipFile.delete()) {
                    logger.info("Delete Zip File: Zip file deleted successfully present as: " + zipFile.getPath());
                    flag = true;
                } else {
                    logger.error("Delete Zip File: Delete zip file failed for provided path as: " + zipFile.getPath());
                }
            } else {
                logger.warn("Delete Zip File: Zip File doesn't exist to delete. Provided path: " + zipFile.getPath());
            }
        } catch (SecurityException | NullPointerException | ZipException e) {
            logger.error("Delete Zip File: Delete zip file at: " + zipFile.getPath() + ". " + e.getMessage(), e);
        }
        return flag;
    }

    public boolean createZipOfFolder(String folderToZip) {
        return createZipOfFolders(Collections.singletonList(new File(folderToZip)));
    }

    public boolean createZipOfFolder(File folderToZip) {
        return createZipOfFolders(Collections.singletonList(folderToZip));
    }

    public boolean createZipOfFolders(List<File> foldersToZip) {
        if (zipFile == null) {
            logger.warn("Create Zip of Folders: Create zip file of a Folders failed as zip file path is not assigned.");
            return false;
        }
        boolean flag = false;
        try {
            deleteZipFile();
            ZipFile zip4JFile = new ZipFile(zipFile);
            ProgressMonitor progressMonitor = zip4JFile.getProgressMonitor();
            for (File folderToZip : foldersToZip) {
                zip4JFile.addFolder(folderToZip);
            }
            if (progressMonitor.getResult().equals(ProgressMonitor.Result.SUCCESS)) {
                logger.info("Create Zip of Folders: Zip file created successfully at: " + zipFile.getPath());
                flag = true;
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.ERROR)) {
                logger.error("Create Zip of Folders: Error occurred while creating zip file at: " + zipFile.getPath());
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.CANCELLED)) {
                logger.error("Create Zip of Folders: Zip creation cancelled at: " + zipFile.getPath());
            }
        } catch (ZipException e) {
            logger.error("Create Zip of Folders: Zip creation failed due to: " + e.getMessage() + " at:" + zipFile.getPath() + ".", e);
            e.printStackTrace();
        }
        return flag;
    }

    public boolean createSplitZipOfFolder(String folderToZip, long splitLength, FileSizeUnits fileSizeUnits) {
        return createSplitZipOfFolder(new File(folderToZip), splitLength, fileSizeUnits);
    }

    public boolean createSplitZipOfFolder(File foldersToZip, long splitLength, FileSizeUnits fileSizeUnits) {
        if (zipFile == null) {
            logger.warn("Create Split Zip of Folder: Create split zip file of Folders failed, As zip file path is not assigned.");
            return false;
        }
        boolean flag = false;
        try {
            deleteZipFile();
            ZipFile zip4JFile = new ZipFile(zipFile);
            ProgressMonitor progressMonitor = zip4JFile.getProgressMonitor();
            zip4JFile.createSplitZipFileFromFolder(foldersToZip, new ZipParameters(), true, splitLength * fileSizeUnits.getUnitSize());
            if (progressMonitor.getResult().equals(ProgressMonitor.Result.SUCCESS)) {
                logger.info("Create Split Zip of Folder: Split Zip file created successfully at: " + zipFile.getPath());
                flag = true;
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.ERROR)) {
                logger.error("Create Split Zip of Folder: Error occurred while creating split zip file at: " + zipFile.getPath());
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.CANCELLED)) {
                logger.error("Create Split Zip of Folder: Split Zip creation cancelled at: " + zipFile.getPath());
            }
        } catch (ZipException e) {
            logger.error("Create Split Zip of Folder: Split Zip creation failed due to: " + e.getMessage() + " at:" + zipFile.getPath() + ".", e);
        }
        return flag;
    }

    public boolean mergeSplitZip(String outputZipFilePathWithName) {
        return mergeSplitZip(new File(outputZipFilePathWithName));
    }

    public boolean mergeSplitZip(File outputZipFile) {
        if (zipFile == null) {
            logger.warn("Merge Split Zip: Merge split zip failed, As zip file path is not assigned.");
            return false;
        }
        boolean flag = false;
        try {
            ZipFile zip4JFile = new ZipFile(zipFile);
            if (!zip4JFile.isSplitArchive()) {
                logger.error("Merge Split Zip: Merge split zip will fail, As zip file is not 'split zip' file.");
            } else {
                ProgressMonitor progressMonitor = zip4JFile.getProgressMonitor();
                zip4JFile.mergeSplitFiles(outputZipFile);
                if (progressMonitor.getResult().equals(ProgressMonitor.Result.SUCCESS)) {
                    logger.info("Merge Split Zip: Merge Split Zip file created successfully at: " + outputZipFile.getPath());
                    flag = true;
                } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.ERROR)) {
                    logger.error("Merge Split Zip: Error occurred while merging split zip file at: " + outputZipFile.getPath());
                } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.CANCELLED)) {
                    logger.error("Merge Split Zip: Split Zip merging cancelled at: " + outputZipFile.getPath());
                }
            }
        } catch (ZipException e) {
            logger.error("Merge Split Zip: Split Zip merging failed due to: " + e.getMessage() + " at:" + outputZipFile.getPath() + ".", e);
        }
        return flag;
    }

    public boolean createPasswordProtectedSplitZipOfFolder(String folderToZip, ZipParameters zipParameters, long splitLength, FileSizeUnits fileSizeUnits) {
        return createPasswordProtectedSplitZipOfFolder(new File(folderToZip), zipParameters, splitLength, fileSizeUnits);
    }

    public boolean createPasswordProtectedSplitZipOfFolder(File folderToZip, ZipParameters zipParameters, long splitLength, FileSizeUnits fileSizeUnits) {
        if (zipFile == null) {
            logger.warn("Create Split Zip of Folder with Password Protection: Create split zip file of Folder failed, As zip file path is not assigned.");
            return false;
        }
        if (password == null || password.length == 0) {
            logger.warn("Create Split Zip of Folder with Password Protection: Password Protection split zip will fail as zip file password is null.");
            return false;
        }
        if (zipParameters.getEncryptionMethod() == EncryptionMethod.NONE) {
            logger.error("Create Split Zip of Folder with Password Protection: Password Protection split zip will fail as zip file 'Encryption Method' is not set.");
            return false;
        }
        if (zipParameters.isEncryptFiles() == Boolean.FALSE) {
            logger.error("Create Split Zip of Folder with Password Protection: Password Protection split zip will fail as zip file 'Encrypt Files' flag is not set.");
            return false;
        }
        boolean flag = false;
        try {
            deleteZipFile();
            ZipFile zip4JFile = new ZipFile(zipFile);
            ProgressMonitor progressMonitor = zip4JFile.getProgressMonitor();
            zip4JFile.createSplitZipFileFromFolder(folderToZip, zipParameters, true, splitLength * fileSizeUnits.getUnitSize());
            if (progressMonitor.getResult().equals(ProgressMonitor.Result.SUCCESS)) {
                logger.info("Create Split Zip of Folder with Password Protection: Password Protection Split Zip file created successfully at: " + zipFile.getPath());
                flag = true;
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.ERROR)) {
                logger.error("Create Split Zip of Folder with Password Protection: Error occurred while creating password protection split zip file at: " + zipFile.getPath());
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.CANCELLED)) {
                logger.error("Create Split Zip of Folder with Password Protection: Password Protection Split Zip creation cancelled at: " + zipFile.getPath());
            }
        } catch (ZipException e) {
            logger.error("Create Split Zip of Folder with Password Protection: Password Protection Split Zip creation failed due to: " + e.getMessage() + " at:" + zipFile.getPath() + ".", e);
        }
        return flag;
    }

    public boolean addFolderToZip(String folderToZip) {
        return addFoldersToZip(Collections.singletonList(new File(folderToZip)));
    }

    public boolean addFolderToZip(File folderToZip) {
        return addFoldersToZip(Collections.singletonList(folderToZip));
    }

    public boolean addFoldersToZip(List<File> foldersToZip) {
        if (zipFile == null) {
            logger.warn("Add Folders to Zip: Updating zip file failed as zip file path is not assigned.");
            return false;
        }
        boolean flag = false;
        try {
            ZipFile zip4JFile = new ZipFile(zipFile);
            ProgressMonitor progressMonitor = zip4JFile.getProgressMonitor();
            for (File folderToZip : foldersToZip) {
                zip4JFile.addFolder(folderToZip);
            }
            if (progressMonitor.getResult().equals(ProgressMonitor.Result.SUCCESS)) {
                logger.info("Add Folders to Zip: Zip file updated successfully at: " + zipFile.getPath());
                flag = true;
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.ERROR)) {
                logger.error("Add Folders to Zip: Error occurred while updating zip file at: " + zipFile.getPath());
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.CANCELLED)) {
                logger.error("Add Folders to Zip: Zip file updating process cancelled at: " + zipFile.getPath());
            }
        } catch (ZipException e) {
            logger.error("Add Folders to Zip: Zip file updating process failed due to: " + e.getMessage() + " at:" + zipFile.getPath() + ".", e);
        }
        return flag;
    }

    public boolean addPasswordProtectedFolderToZip(String folderToZip, ZipParameters zipParameters) {
        return addPasswordProtectedFoldersToZip(Collections.singletonList(new File(folderToZip)), zipParameters);
    }

    public boolean addPasswordProtectedFolderToZip(File folderToZip, ZipParameters zipParameters) {
        return addPasswordProtectedFoldersToZip(Collections.singletonList(folderToZip), zipParameters);
    }

    public boolean addPasswordProtectedFoldersToZip(List<File> foldersToZip, ZipParameters zipParameters) {
        if (zipFile == null) {
            logger.warn("Add Folders with Password Protection to Zip: Adding folders failed as zip file path is not assigned.");
            return false;
        }
        if (password == null || password.length == 0) {
            logger.warn("Add Folders with Password Protection to Zip: Adding folders will fail as zip file password is null.");
            return false;
        }
        if (zipParameters.getEncryptionMethod() == EncryptionMethod.NONE) {
            logger.error("Add Folders with Password Protection to Zip: Adding folders will fail as zip file 'Encryption Method' is not set.");
            return false;
        }
        if (zipParameters.isEncryptFiles() == Boolean.FALSE) {
            logger.error("Add Folders with Password Protection to Zip: Adding folders will fail as zip file 'Encrypt Files' flag is not set.");
            return false;
        }
        boolean flag = false;
        try {
            ZipFile zip4JFile = new ZipFile(zipFile);
            zip4JFile.setPassword(password);
            ProgressMonitor progressMonitor = zip4JFile.getProgressMonitor();
            for (File folderToZip : foldersToZip) {
                zip4JFile.addFolder(folderToZip, zipParameters);
            }
            if (progressMonitor.getResult().equals(ProgressMonitor.Result.SUCCESS)) {
                logger.info("Add Files with Password Protection to Zip: Zip file updated successfully at: " + zipFile.getPath());
                flag = true;
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.ERROR)) {
                logger.error("Add Files with Password Protection to Zip: Error occurred while updating zip file at: " + zipFile.getPath());
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.CANCELLED)) {
                logger.error("Add Files with Password Protection to Zip: Zip file updating process cancelled at: " + zipFile.getPath());
            }
        } catch (ZipException e) {
            logger.error("Add Files with Password Protection to Zip: Zip file updating process failed due to: " + e.getMessage() + " at:" + zipFile.getPath() + ".", e);
        }
        return flag;
    }

    public boolean createZipOfFile(String fileToZip) {
        return createZipOfFiles(Collections.singletonList(new File(fileToZip)));
    }

    public boolean createZipOfFile(File fileToZip) {
        return createZipOfFiles(Collections.singletonList(fileToZip));
    }

    public boolean createZipOfFiles(List<File> filesToZip) {
        if (zipFile == null) {
            logger.warn("Create Zip of Files: Create zip file of files failed as zip file path is not assigned.");
            return false;
        }
        boolean flag = false;
        try {
            deleteZipFile();
            ZipFile zip4JFile = new ZipFile(zipFile);
            ProgressMonitor progressMonitor = zip4JFile.getProgressMonitor();
            zip4JFile.addFiles(filesToZip);
            if (progressMonitor.getResult().equals(ProgressMonitor.Result.SUCCESS)) {
                logger.info("Create Zip of Files: Zip file created successfully at: " + zipFile.getPath());
                flag = true;
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.ERROR)) {
                logger.error("Create Zip of Files: Error occurred while creating zip file at: " + zipFile.getPath());
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.CANCELLED)) {
                logger.error("Create Zip of Files: Zip creation cancelled at: " + zipFile.getPath());
            }
        } catch (ZipException e) {
            logger.error("Create Zip of Files: Zip creation failed due to: " + e.getMessage() + " at:" + zipFile.getPath() + ".", e);
        }
        return flag;
    }

    public boolean createSplitZipOfFile(String fileToZip, long splitLength, FileSizeUnits fileSizeUnits) {
        return createSplitZipOfFiles(Collections.singletonList(new File(fileToZip)), splitLength, fileSizeUnits);
    }

    public boolean createSplitZipOfFile(File fileToZip, long splitLength, FileSizeUnits fileSizeUnits) {
        return createSplitZipOfFiles(Collections.singletonList(fileToZip), splitLength, fileSizeUnits);
    }

    public boolean createSplitZipOfFiles(List<File> filesToZip, long splitLength, FileSizeUnits fileSizeUnits) {
        if (zipFile == null) {
            logger.warn("Create Split Zip of Files: Create split zip file of files failed, As zip file path is not assigned.");
            return false;
        }
        boolean flag = false;
        try {
            deleteZipFile();
            ZipFile zip4JFile = new ZipFile(zipFile);
            ProgressMonitor progressMonitor = zip4JFile.getProgressMonitor();
            zip4JFile.createSplitZipFile(filesToZip, new ZipParameters(), true, splitLength * fileSizeUnits.getUnitSize());
            if (progressMonitor.getResult().equals(ProgressMonitor.Result.SUCCESS)) {
                logger.info("Create Split Zip of Files: Split Zip file created successfully at: " + zipFile.getPath());
                flag = true;
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.ERROR)) {
                logger.error("Create Split Zip of Files: Error occurred while creating split zip file at: " + zipFile.getPath());
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.CANCELLED)) {
                logger.error("Create Split Zip of Files: Split Zip creation cancelled at: " + zipFile.getPath());
            }
        } catch (ZipException e) {
            logger.error("Create Split Zip of Files: Split Zip creation failed due to: " + e.getMessage() + " at:" + zipFile.getPath() + ".", e);
        }
        return flag;
    }

    public boolean createPasswordProtectedSplitZipOfFile(String fileToZip, ZipParameters zipParameters, long splitLength, FileSizeUnits fileSizeUnits) {
        return createPasswordProtectedSplitZipOfFiles(Collections.singletonList(new File(fileToZip)), zipParameters, splitLength, fileSizeUnits);
    }

    public boolean createPasswordProtectedSplitZipOfFile(File fileToZip, ZipParameters zipParameters, long splitLength, FileSizeUnits fileSizeUnits) {
        return createPasswordProtectedSplitZipOfFiles(Collections.singletonList(fileToZip), zipParameters, splitLength, fileSizeUnits);
    }

    public boolean createPasswordProtectedSplitZipOfFiles(List<File> filesToZip, ZipParameters zipParameters, long splitLength, FileSizeUnits fileSizeUnits) {
        if (zipFile == null) {
            logger.warn("Create Split Zip of Files with Password Protection: Create split zip file of files failed, As zip file path is not assigned.");
            return false;
        }
        if (password == null || password.length == 0) {
            logger.warn("Create Split Zip of Files with Password Protection: Password Protection split zip will fail as zip file password is null.");
            return false;
        }
        if (zipParameters.getEncryptionMethod() == EncryptionMethod.NONE) {
            logger.error("Create Split Zip of Files with Password Protection: Password Protection split zip will fail as zip file 'Encryption Method' is not set.");
            return false;
        }
        if (zipParameters.isEncryptFiles() == Boolean.FALSE) {
            logger.error("Create Split Zip of Files with Password Protection: Password Protection split zip will fail as zip file 'Encrypt Files' flag is not set.");
            return false;
        }
        boolean flag = false;
        try {
            deleteZipFile();
            ZipFile zip4JFile = new ZipFile(zipFile);
            ProgressMonitor progressMonitor = zip4JFile.getProgressMonitor();
            zip4JFile.createSplitZipFile(filesToZip, zipParameters, true, splitLength * fileSizeUnits.getUnitSize());
            if (progressMonitor.getResult().equals(ProgressMonitor.Result.SUCCESS)) {
                logger.info("Create Split Zip of Files with Password Protection: Password Protection Split Zip file created successfully at: " + zipFile.getPath());
                flag = true;
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.ERROR)) {
                logger.error("Create Split Zip of Files with Password Protection: Error occurred while creating password protection split zip file at: " + zipFile.getPath());
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.CANCELLED)) {
                logger.error("Create Split Zip of Files with Password Protection: Password Protection Split Zip creation cancelled at: " + zipFile.getPath());
            }
        } catch (ZipException e) {
            logger.error("Create Split Zip of Files with Password Protection: Password Protection Split Zip creation failed due to: " + e.getMessage() + " at:" + zipFile.getPath() + ".", e);
        }
        return flag;
    }

    public boolean addFileToZip(String folderToZip) {
        return addFilesToZip(Collections.singletonList(new File(folderToZip)));
    }

    public boolean addFileToZip(File folderToZip) {
        return addFilesToZip(Collections.singletonList(folderToZip));
    }

    public boolean addFilesToZip(List<File> filesToZip) {
        if (zipFile == null) {
            logger.warn("Add Files to Zip: Updating zip file failed as zip file path is not assigned.");
            return false;
        }
        boolean flag = false;
        try {
            ZipFile zip4JFile = new ZipFile(zipFile);
            ProgressMonitor progressMonitor = zip4JFile.getProgressMonitor();
            for (File fileToZip : filesToZip) {
                zip4JFile.addFolder(fileToZip);
            }
            if (progressMonitor.getResult().equals(ProgressMonitor.Result.SUCCESS)) {
                logger.info("Add Files to Zip: Zip file updated successfully at: " + zipFile.getPath());
                flag = true;
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.ERROR)) {
                logger.error("Add Files to Zip: Error occurred while updating zip file at: " + zipFile.getPath());
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.CANCELLED)) {
                logger.error("Add Files to Zip: Zip file updating process cancelled at: " + zipFile.getPath());
            }
        } catch (ZipException e) {
            logger.error("Add Files to Zip: Zip file updating process failed due to: " + e.getMessage() + " at:" + zipFile.getPath() + ".", e);
        }
        return flag;
    }

    public boolean addPasswordProtectedFileToZip(String fileToZip, ZipParameters zipParameters) {
        return addPasswordProtectedFilesToZip(Collections.singletonList(new File(fileToZip)), zipParameters);
    }

    public boolean addPasswordProtectedFileToZip(File fileToZip, ZipParameters zipParameters) {
        return addPasswordProtectedFilesToZip(Collections.singletonList(fileToZip), zipParameters);
    }

    public boolean addPasswordProtectedFilesToZip(List<File> filesToZip, ZipParameters zipParameters) {
        if (zipFile == null) {
            logger.warn("Add Files with Password Protection to Zip: Adding files failed as zip file path is not assigned.");
            return false;
        }
        if (password == null || password.length == 0) {
            logger.warn("Add Files with Password Protection to Zip: Adding files will fail as zip file password is null.");
            return false;
        }
        if (zipParameters.getEncryptionMethod() == EncryptionMethod.NONE) {
            logger.error("Add Files with Password Protection to Zip: Adding files will fail as zip file 'Encryption Method' is not set.");
            return false;
        }
        if (zipParameters.isEncryptFiles() == Boolean.FALSE) {
            logger.error("Add Files with Password Protection to Zip: Adding files will fail as zip file 'Encrypt Files' flag is not set.");
            return false;
        }
        boolean flag = false;
        try {
            ZipFile zip4JFile = new ZipFile(zipFile);
            zip4JFile.setPassword(password);
            ProgressMonitor progressMonitor = zip4JFile.getProgressMonitor();
            for (File fileToZip : filesToZip) {
                zip4JFile.addFile(fileToZip, zipParameters);
            }
            if (progressMonitor.getResult().equals(ProgressMonitor.Result.SUCCESS)) {
                logger.info("Add Files with Password Protection to Zip: Zip file updated successfully at: " + zipFile.getPath());
                flag = true;
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.ERROR)) {
                logger.error("Add Files with Password Protection to Zip: Error occurred while updating zip file at: " + zipFile.getPath());
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.CANCELLED)) {
                logger.error("Add Files with Password Protection to Zip: Zip file updating process cancelled at: " + zipFile.getPath());
            }
        } catch (ZipException e) {
            logger.error("Add Files with Password Protection to Zip: Zip file updating process failed due to: " + e.getMessage() + " at:" + zipFile.getPath() + ".", e);
        }
        return flag;
    }

    public boolean removeFileOrFolderInZip(String filesFoldersToRemove) {
        return removeFilesFoldersInZip(Collections.singletonList(filesFoldersToRemove));
    }

    public boolean removeFilesFoldersInZip(List<String> filesFoldersToRemove) {
        if (zipFile == null) {
            logger.warn("Remove Files & Folders in Zip: Updating zip file failed as zip file path is not assigned.");
            return false;
        }
        boolean flag = false;
        try {
            ZipFile zip4JFile = new ZipFile(zipFile);
            ProgressMonitor progressMonitor = zip4JFile.getProgressMonitor();
            zip4JFile.removeFiles(filesFoldersToRemove);
            if (progressMonitor.getResult().equals(ProgressMonitor.Result.SUCCESS)) {
                logger.info("Remove Files & Folders in Zip: Zip file updated successfully at: " + zipFile.getPath());
                flag = true;
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.ERROR)) {
                logger.error("Remove Files & Folders in Zip: Error occurred while updating zip file at: " + zipFile.getPath());
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.CANCELLED)) {
                logger.error("Remove Files & Folders in Zip: Zip file updating process cancelled at: " + zipFile.getPath());
            }
        } catch (ZipException e) {
            logger.error("Remove Files & Folders in Zip: Zip file updating process failed due to: " + e.getMessage() + " at:" + zipFile.getPath() + ".", e);
        }
        return flag;
    }

    public boolean renameFilesFoldersInZip(String fileNameToRename, String newFileName) {
        return renameFilesFoldersInZip(Collections.singletonMap(fileNameToRename, newFileName));
    }

    public boolean renameFilesFoldersInZip(Map<String, String> filesFoldersNamesMap) {
        if (zipFile == null) {
            logger.warn("Rename Files & Folders in Zip: Renaming zip file failed as zip file path is not assigned.");
            return false;
        }
        boolean flag = false;
        try {
            ZipFile zip4JFile = new ZipFile(zipFile);
            ProgressMonitor progressMonitor = zip4JFile.getProgressMonitor();
            zip4JFile.renameFiles(filesFoldersNamesMap);
            if (progressMonitor.getResult().equals(ProgressMonitor.Result.SUCCESS)) {
                logger.info("Rename Files & Folders in Zip: Zip file updated successfully at: " + zipFile.getPath());
                flag = true;
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.ERROR)) {
                logger.error("Rename Files & Folders in Zip: Error occurred while updating zip file at: " + zipFile.getPath());
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.CANCELLED)) {
                logger.error("Rename Files & Folders in Zip: Zip file updating process cancelled at: " + zipFile.getPath());
            }
        } catch (ZipException e) {
            logger.error("Rename Files & Folders in Zip: Zip file updating process failed due to: " + e.getMessage() + " at:" + zipFile.getPath() + ".", e);
        }
        return flag;
    }

    public List<String> getAllFilesFoldersNamesInZip() {
        if (zipFile == null) {
            logger.warn("Get Files & Folders names in Zip: Fetching Files & Folder name in zip file failed as zip file path is not assigned.");
            return null;
        }
        List<String> fileNames = null;
        try {
            ZipFile zip4JFile = new ZipFile(zipFile);
            List<FileHeader> fileHeaders = zip4JFile.getFileHeaders();
            ProgressMonitor progressMonitor = zip4JFile.getProgressMonitor();
            fileNames = fileHeaders.stream().map(AbstractFileHeader::getFileName).collect(Collectors.toList());
            if (progressMonitor.getResult().equals(ProgressMonitor.Result.SUCCESS)) {
                logger.info("Get Files & Folders names in Zip: Zip file 'file & Folders' names fetched successfully.");
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.ERROR)) {
                logger.error("Get Files & Folders names in Zip: Error occurred while fetching zip file 'file & Folders' names.");
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.CANCELLED)) {
                logger.error("Get Files & Folders names in Zip: Fetching zip file 'file & Folders' names process cancelled.");
            }
        } catch (ZipException e) {
            logger.error("Get Files & Folders names in Zip: Fetching zip file 'file & Folders' names failed due to: " + e.getMessage() + " at:" + zipFile.getPath() + ".", e);
        }
        return fileNames;
    }

    public boolean extractZip(String extractDestinationPath) {
        if (zipFile == null) {
            logger.warn("Extract Zip: Extract zip failed as zip file path is not assigned.");
            return false;
        }
        boolean flag = false;
        try {
            ZipFile zip4JFile = new ZipFile(zipFile);
            ProgressMonitor progressMonitor = zip4JFile.getProgressMonitor();
            zip4JFile.extractAll(extractDestinationPath);
            if (progressMonitor.getResult().equals(ProgressMonitor.Result.SUCCESS)) {
                logger.info("Extract Zip: Zip file extracted successfully at: " + extractDestinationPath);
                flag = true;
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.ERROR)) {
                logger.error("Extract Zip: Error occurred while extracting zip file at: " + extractDestinationPath);
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.CANCELLED)) {
                logger.error("Extract Zip: Zip extraction cancelled at: " + extractDestinationPath);
            }
        } catch (ZipException e) {
            logger.error("Extract Zip: Zip extraction failed due to: " + e.getMessage() + " at:" + extractDestinationPath + ".", e);
        }
        return flag;
    }

    public boolean extractPasswordProtectedZip(String extractDestinationPath) {
        if (zipFile == null) {
            logger.warn("Extract Password Protected Zip: Extract password protected zip file failed as zip file path is not assigned.");
            return false;
        }
        if (password == null || password.length == 0) {
            logger.warn("Extract Password Protected Zip: Extracting files will fail as zip file password is null.");
            return false;
        }
        boolean flag = false;
        try {
            ZipFile zip4JFile = new ZipFile(zipFile);
            zip4JFile.setPassword(password);
            ProgressMonitor progressMonitor = zip4JFile.getProgressMonitor();
            zip4JFile.extractAll(extractDestinationPath);
            if (progressMonitor.getResult().equals(ProgressMonitor.Result.SUCCESS)) {
                logger.info("Extract Password Protected Zip: Password Protected Zip file extracted successfully at: " + extractDestinationPath);
                flag = true;
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.ERROR)) {
                logger.error("Extract Password Protected Zip: Error occurred while extracting password protected zip file at: " + extractDestinationPath);
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.CANCELLED)) {
                logger.error("Extract Password Protected Zip: Password Protected Zip extraction cancelled at: " + extractDestinationPath);
            }
        } catch (ZipException e) {
            logger.error("Extract Password Protected Zip: Password Protected Zip extraction failed due to: " + e.getMessage() + " at:" + extractDestinationPath + ".", e);
        }
        return flag;
    }

    public boolean extractFileOrFolderInZip(String fileFolderPath, String extractDestinationPath) {
        return extractFileOrFolderInZip(fileFolderPath, extractDestinationPath, null);
    }

    public boolean extractPasswordProtectedFileFolderInZip(String fileFolderPath, String extractDestinationPath) {
        return extractPasswordProtectedFileFolderInZip(fileFolderPath, extractDestinationPath, null);
    }

    public boolean extractFileOrFolderInZip(String fileFolderPath, String extractDestinationPath, String newFileFolderName) {
        if (zipFile == null) {
            logger.warn("Extract File Or Folder in Zip: Extract failed as zip file path is not assigned.");
            return false;
        }
        boolean flag = false;
        try {
            ZipFile zip4JFile = new ZipFile(zipFile);
            ProgressMonitor progressMonitor = zip4JFile.getProgressMonitor();
            zip4JFile.extractFile(fileFolderPath, extractDestinationPath, newFileFolderName);
            if (progressMonitor.getResult().equals(ProgressMonitor.Result.SUCCESS)) {
                logger.info("Extract File Or Folder in Zip: Zip file/folder extracted successfully at: " + extractDestinationPath);
                flag = true;
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.ERROR)) {
                logger.error("Extract File Or Folder in Zip: Error occurred while extracting at: " + extractDestinationPath);
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.CANCELLED)) {
                logger.error("Extract File Or Folder in Zip: Zip extraction cancelled at: " + extractDestinationPath);
            }
        } catch (ZipException e) {
            logger.error("Extract File Or Folder in Zip: Zip extraction failed due to: " + e.getMessage() + " at:" + extractDestinationPath + ".", e);
        }
        return flag;
    }

    public boolean extractPasswordProtectedFileFolderInZip(String fileFolderPath, String extractDestinationPath, String newFileFolderName) {
        if (zipFile == null) {
            logger.warn("Extract Password Protected File Or Folder Zip: Extract password protected zip failed as zip file path is not assigned.");
            return false;
        }
        if (password == null || password.length == 0) {
            logger.warn("Extract Password Protected File Or Folder Zip: Extracting will fail as zip file password is null.");
            return false;
        }
        boolean flag = false;
        try {
            ZipFile zip4JFile = new ZipFile(zipFile);
            zip4JFile.setPassword(password);
            ProgressMonitor progressMonitor = zip4JFile.getProgressMonitor();
            zip4JFile.extractFile(fileFolderPath, extractDestinationPath, newFileFolderName);
            if (progressMonitor.getResult().equals(ProgressMonitor.Result.SUCCESS)) {
                logger.info("Extract Password Protected File Or Folder Zip: Password Protected Zip extracted successfully at: " + extractDestinationPath);
                flag = true;
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.ERROR)) {
                logger.error("Extract Password Protected File Or Folder Zip: Error occurred while extracting password protected zip file at: " + extractDestinationPath);
            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.CANCELLED)) {
                logger.error("Extract Password Protected File Or Folder Zip: Password Protected Zip extraction cancelled at: " + extractDestinationPath);
            }
        } catch (ZipException e) {
            logger.error("Extract Password Protected File Or Folder Zip: Password Protected Zip extraction failed due to: " + e.getMessage() + " at:" + extractDestinationPath + ".", e);
        }
        return flag;
    }

}
