package utils.file.manipulation;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.*;

public class ExcelComparatorColumnBasedKey {
    protected static final Logger logger = LoggerFactory.getLogger(ExcelComparatorColumnBasedKey.class);
    private static final String EXPECTED_CELL_IDENTIFIER = "Expected";
    private static final String ACTUAL_CELL_IDENTIFIER = "Actual";
    private final WorkbookData expectedWorkbookData;
    private final WorkbookData actualWorkbookData;

    public ExcelComparatorColumnBasedKey(String expectedWorkbookData, String actualWorkbookData) throws IOException {
        this.expectedWorkbookData = new WorkbookData(expectedWorkbookData);
        this.actualWorkbookData = new WorkbookData(actualWorkbookData);
    }

    public File compareWbForDifference(WorkbookSheetsKeyColumns workbookSheetsKeyColumns, String outputFilePath) throws IOException {
        logger.info("Compare all sheets of '"+expectedWorkbookData.getFilePath()+"' expected workbook and '"+actualWorkbookData.getFilePath()+"' actual workbook.");
        outputFilePath = Objects.requireNonNull(FileUtils.getOSFriendlyFilePath(outputFilePath));
        String directoryPath = outputFilePath.substring(0, outputFilePath.lastIndexOf(File.separator));
        FileUtils.createFolderIfNotPresent(directoryPath);
        FileUtils.deleteAllFilesHavingName(
                directoryPath,
                outputFilePath.substring(outputFilePath.lastIndexOf(File.separator) + 1)
        );
        Set<String> allSheetNames = new HashSet<>();
        expectedWorkbookData.getSheetData().forEach(sheetData -> allSheetNames.add(sheetData.getSheetName()));
        actualWorkbookData.getSheetData().forEach(sheetData -> allSheetNames.add(sheetData.getSheetName()));
        Workbook workbook = new XSSFWorkbook();
        Set<Boolean> diffPresentFlag = new HashSet<>();

        for (String sheetName : allSheetNames) {
            boolean sheetPresence = isSheetPresentForDifference(sheetName);
            if(sheetPresence) {
                diffPresentFlag.add(DifferenceEngine.Difference(expectedWorkbookData.getSheetData(sheetName), actualWorkbookData.getSheetData(sheetName), workbook, workbookSheetsKeyColumns));
                logger.info("Successfully compared '"+sheetName+"' sheet in expected and actual workbook");
            } else {
                logger.info("Can not compare '"+sheetName+"' sheet as it's either missing from expected and actual workbook");
            }
        }

        if(diffPresentFlag.contains(true)) {
            FileOutputStream out = new FileOutputStream(Objects.requireNonNull(outputFilePath));
            workbook.write(out);
            logger.info("Breaks after comparing 2 workbooks reported in: "+FileUtils.getOSFriendlyFilePath(outputFilePath));
        } else {
            FileOutputStream out = new FileOutputStream(Objects.requireNonNull(FileUtils.getOSFriendlyFilePath(outputFilePath)));
            workbook.write(out);
            logger.info("No breaks identified after comparing 2 workbooks reported in: "+FileUtils.getOSFriendlyFilePath(outputFilePath));
        }
        logger.info("Successfully compared all sheets of '"+expectedWorkbookData.getFilePath()+"' expected workbook and '"+actualWorkbookData.getFilePath()+"' actual workbook.");
        return new File(Objects.requireNonNull(FileUtils.getOSFriendlyFilePath(outputFilePath)));
    }

    public File compareWbForSheetDifference(WorkbookSheetsKeyColumns workbookSheetsKeyColumns, String sheetName, String outputFilePath) throws IOException {
        logger.info("Compare sheet '"+sheetName+"' of '"+expectedWorkbookData.getFilePath()+"' expected workbook and '"+actualWorkbookData.getFilePath()+"' actual workbook.");
        outputFilePath = Objects.requireNonNull(FileUtils.getOSFriendlyFilePath(outputFilePath));
        String directoryPath = outputFilePath.substring(0, outputFilePath.lastIndexOf(File.separator));
        FileUtils.createFolderIfNotPresent(directoryPath);
        FileUtils.deleteAllFilesHavingName(
                directoryPath,
                outputFilePath.substring(outputFilePath.lastIndexOf(File.separator) + 1)
        );

        Workbook workbook = new XSSFWorkbook();
        boolean diffPresentFlag = false;

        boolean sheetPresence = isSheetPresentForDifference(sheetName);
        if(sheetPresence) {
            diffPresentFlag = DifferenceEngine.Difference(expectedWorkbookData.getSheetData(sheetName), actualWorkbookData.getSheetData(sheetName), workbook, workbookSheetsKeyColumns);
            logger.info("Successfully compared '"+sheetName+"' sheet in expected and actual workbook");
        }

        if(diffPresentFlag) {
            FileOutputStream out = new FileOutputStream(Objects.requireNonNull(outputFilePath));
            workbook.write(out);
            logger.info("Breaks after comparing sheet '"+sheetName+"' in 2 workbooks reported in: "+FileUtils.getOSFriendlyFilePath(outputFilePath));
        } else {
            FileOutputStream out = new FileOutputStream(Objects.requireNonNull(FileUtils.getOSFriendlyFilePath(outputFilePath)));
            workbook.write(out);
            logger.info("No breaks identified after comparing sheet '"+sheetName+"' in 2 workbooks reported in: "+FileUtils.getOSFriendlyFilePath(outputFilePath));
        }
        logger.info("Successfully compared sheet '"+sheetName+"' of '"+expectedWorkbookData.getFilePath()+"' expected workbook and '"+actualWorkbookData.getFilePath()+"' actual workbook.");
        return new File(Objects.requireNonNull(FileUtils.getOSFriendlyFilePath(outputFilePath)));
    }

    public File compareWbForSheetDifferenceForColumnAddress(WorkbookSheetsKeyColumns workbookSheetsKeyColumns, String sheetName, String startColumnAddress, String endColumnAddress, String outputFilePath) throws IOException {
        logger.info("Compare sheet '"+sheetName+"' of '"+expectedWorkbookData.getFilePath()+"' expected workbook and '"+actualWorkbookData.getFilePath()+"' actual workbook from column address '"+startColumnAddress+"' to '"+endColumnAddress+"'.");
        outputFilePath = Objects.requireNonNull(FileUtils.getOSFriendlyFilePath(outputFilePath));
        String directoryPath = outputFilePath.substring(0, outputFilePath.lastIndexOf(File.separator));
        FileUtils.createFolderIfNotPresent(directoryPath);
        FileUtils.deleteAllFilesHavingName(
                directoryPath,
                outputFilePath.substring(outputFilePath.lastIndexOf(File.separator) + 1)
        );

        Workbook workbook = new XSSFWorkbook();
        boolean diffPresentFlag = false;

        boolean sheetPresence = isSheetPresentForDifference(sheetName);
        if(sheetPresence) {
            diffPresentFlag = DifferenceEngine.DifferenceBasedOnColumnAddress(expectedWorkbookData.getSheetData(sheetName), actualWorkbookData.getSheetData(sheetName), startColumnAddress, endColumnAddress, workbook, workbookSheetsKeyColumns);
            logger.info("Successfully compared '"+sheetName+"' sheet in expected and actual workbook from column address '"+startColumnAddress+"' to '"+endColumnAddress+"'.");
        }

        if(diffPresentFlag) {
            FileOutputStream out = new FileOutputStream(Objects.requireNonNull(outputFilePath));
            workbook.write(out);
            logger.info("Breaks after comparing sheet '"+sheetName+"' in 2 workbooks from column address '"+startColumnAddress+"' to '"+endColumnAddress+"' reported in: "+FileUtils.getOSFriendlyFilePath(outputFilePath));
        } else {
            FileOutputStream out = new FileOutputStream(Objects.requireNonNull(FileUtils.getOSFriendlyFilePath(outputFilePath)));
            workbook.write(out);
            logger.info("No breaks identified after comparing sheet '"+sheetName+"' in 2 workbooks from column address '"+startColumnAddress+"' to '"+endColumnAddress+"' reported in: "+FileUtils.getOSFriendlyFilePath(outputFilePath));
        }
        logger.info("Successfully compared sheet '"+sheetName+"' of '"+expectedWorkbookData.getFilePath()+"' expected workbook and '"+actualWorkbookData.getFilePath()+"' actual workbook from column address '"+startColumnAddress+"' to '"+endColumnAddress+"'.");
        return new File(Objects.requireNonNull(FileUtils.getOSFriendlyFilePath(outputFilePath)));
    }

    private boolean isSheetPresentForDifference(String sheetName) {
        SheetData expectedSheetData = expectedWorkbookData.getSheetData(sheetName);
        SheetData actualSheetData = actualWorkbookData.getSheetData(sheetName);
        return expectedSheetData != null && actualSheetData!= null;
    }

    /**
     * Represents data extracted from a workbook file.
     */
    private static class WorkbookData {
        private final List<SheetData> sheetData;
        private final String filePath;

        /**
         * Constructs a new WorkbookData object with the specified file path.
         * @param filePath the path of the workbook file
         * @throws IOException if an I/O error occurs while reading the file
         */
        private WorkbookData(String filePath) throws IOException {
            this.filePath = FileUtils.getOSFriendlyFilePath(filePath);
            sheetData = ExcelUtility.readExcel(filePath);
        }

        /**
         * Retrieves the sheet data contained in the workbook.
         * @return a list of SheetData objects representing the sheet data
         */
        public List<SheetData> getSheetData() {
            return sheetData;
        }

        /**
         * Retrieves the file path of the workbook.
         * @return the file path of the workbook
         */
        public String getFilePath() {
            return filePath;
        }

        /**
         * Retrieves the sheet data for the specified sheet name.
         * @param sheetName the name of the sheet to retrieve data for
         * @return the SheetData object for the specified sheet name, or null if not found
         */
        private SheetData getSheetData(String sheetName) {
            return sheetData
                    .stream()
                    .filter(sheetData -> sheetData.getSheetName().equals(sheetName))
                    .findFirst()
                    .orElse(null);
        }
    }

    private static class DifferenceEngine {

        private DifferenceEngine() {
            throw new IllegalStateException("Not designed to create object of DifferenceEngine class as contain static methods.");
        }

        private static boolean Difference(SheetData expectedSheetData, SheetData actualSheetData, Workbook workbook, WorkbookSheetsKeyColumns workbookSheetsKeyColumns) {
            logger.info("Compare '"+expectedSheetData.getSheetName()+"' sheet with '"+actualSheetData.getSheetName()+"' sheet.");
            boolean diffPresentFlag = getDifference(expectedSheetData, actualSheetData, null, null, workbook, workbookSheetsKeyColumns);
            logger.info("Successfully compared '"+expectedSheetData.getSheetName()+"' sheet with '"+actualSheetData.getSheetName()+"' sheet.");
            return diffPresentFlag;
        }

        private static boolean DifferenceBasedOnColumnAddress(SheetData expectedSheetData, SheetData actualSheetData, String startColumnAddress, String endColumnAddress, Workbook workbook, WorkbookSheetsKeyColumns workbookSheetsKeyColumns) {
            logger.info("Compare '"+expectedSheetData.getSheetName()+"' sheet with '"+actualSheetData.getSheetName()+"' sheet from column address '"+startColumnAddress+"' to '"+endColumnAddress+"'");
            int startColumnNumber = ExcelUtility.getExcelColumnNumber(startColumnAddress);
            int endColumnNumber = ExcelUtility.getExcelColumnNumber(endColumnAddress);
            if (startColumnNumber > endColumnNumber) {
                String exceptionMessage = "Exception in DifferenceBasedOnColumnAddress: Start column name '"+startColumnAddress+"' is higher than end column name '"+endColumnAddress+"'";
                logger.error(exceptionMessage);
                throw  new InvalidParameterException(exceptionMessage);
            }
            boolean differencePresentFlag = getDifference(expectedSheetData, actualSheetData, startColumnNumber, endColumnNumber, workbook, workbookSheetsKeyColumns);
            logger.info("Successfully compared '"+expectedSheetData.getSheetName()+"' sheet with '"+actualSheetData.getSheetName()+"' sheet from column address '"+startColumnAddress+"' to '"+endColumnAddress+"'");
            return differencePresentFlag;
        }

        private static Map<String, List<List<String>>> getDataAsKeyColumns(SheetData sheetData, List<String> keyColumns) {
            Map<String, List<List<String>>> keySheetData = new HashMap<>();
            for(List<String> rowData : sheetData.getSheetData()) {
                StringBuilder builderKey = new StringBuilder();
                Map<String, String> tempRowData = new HashMap<>();
                for (String data : rowData) {
                    tempRowData.put(data.split("->")[0], data);
                }
                keyColumns.forEach(key -> builderKey.append(tempRowData.getOrDefault(key, "")));
                List<List<String>> tempData;
                if(!keySheetData.containsKey(builderKey.toString())) {
                    tempData = new ArrayList<>();
                } else {
                    tempData = keySheetData.get(builderKey.toString());
                }
                tempData.add(rowData);
                keySheetData.put(builderKey.toString(), tempData);
            }
            return keySheetData;
        }

        private static boolean getDifference(SheetData expectedSheetData, SheetData actualSheetData, Integer startColumnNum, Integer lastColumnNum, Workbook workbook, WorkbookSheetsKeyColumns workbookSheetsKeyColumns) {
            List<String> keyColumns;
            String sheetName = expectedSheetData.getSheetName();
            if (workbookSheetsKeyColumns != null && workbookSheetsKeyColumns.isKeyColumnPresentForSheet(sheetName)) {
                keyColumns = workbookSheetsKeyColumns.getSheetKeyColumns(sheetName);
                if (keyColumns.isEmpty()) {
                    throw new IllegalArgumentException("Key column address not provided for '"+sheetName+"' sheet in WorkbookSheetsKeyColumns object");
                }
            } else {
                throw new IllegalArgumentException("Key column address not provided for '"+sheetName+"' sheet in WorkbookSheetsKeyColumns object");
            }

            Sheet sheet = workbook.createSheet(expectedSheetData.getSheetName());
            boolean differencePresentFlag = false;
            int workbookRowCounter = 0;
            int columnStartNumber = startColumnNum == null ? 1 : startColumnNum;
            int columnLastNumber = lastColumnNum == null ? 1 : lastColumnNum;

            Map<String, List<List<String>>> expectedKeySheetData = getDataAsKeyColumns(expectedSheetData, keyColumns);
            Set<String> expectedKeySequence = expectedKeySheetData.keySet();
            Map<String, List<List<String>>> actualKeySheetData = getDataAsKeyColumns(actualSheetData, keyColumns);
            Set<String> actualKeySequence = actualKeySheetData.keySet();
            Set<String> consolidatedKeys = new HashSet<>();
            consolidatedKeys.addAll(expectedKeySequence);
            consolidatedKeys.addAll(actualKeySequence);

            CellStyle styleForExpectedHeading = styleForExpectedHeading(workbook);
            CellStyle styleForActualHeading = styleForActualHeading(workbook);
            CellStyle styleForBreaksHeading = styleForBreaksHeading(workbook);
            CellStyle styleForMismatch = styleForMismatch(workbook);

            for (String consolidatedKey : consolidatedKeys) {

                if (!expectedKeySheetData.containsKey(consolidatedKey)) {
                    if (!differencePresentFlag) {
                        differencePresentFlag = true;
                    }
                    List<List<String>> actualData = actualKeySheetData.get(consolidatedKey);
                    for (List<String> data : actualData) {
                        int workbookCellNumber = 0;
                        Row columnAddressWorkbookRow = sheet.createRow(workbookRowCounter++);
                        Row expectedWorkbookRow = sheet.createRow(workbookRowCounter++);
                        Row actualWorkbookRow = sheet.createRow(workbookRowCounter++);
                        Cell expectedWorkbookCell = expectedWorkbookRow.createCell(workbookCellNumber);
                        Cell actualWorkbookCell = actualWorkbookRow.createCell(workbookCellNumber++);
                        expectedWorkbookCell.setCellValue(EXPECTED_CELL_IDENTIFIER);
                        expectedWorkbookCell.setCellStyle(styleForExpectedHeading);
                        actualWorkbookCell.setCellValue(ACTUAL_CELL_IDENTIFIER);
                        actualWorkbookCell.setCellStyle(styleForActualHeading);
                        for (String d : data) {
                            String[] splitData = d.split("->", 2);
                            Cell columnAddressWorkbookCell = columnAddressWorkbookRow.createCell(workbookCellNumber);
                            columnAddressWorkbookCell.setCellValue("Column Address: "+splitData[0]);
                            columnAddressWorkbookCell.setCellStyle(styleForBreaksHeading);
                            expectedWorkbookCell = expectedWorkbookRow.createCell(workbookCellNumber);
                            expectedWorkbookCell.setCellStyle(styleForMismatch);
                            actualWorkbookCell = actualWorkbookRow.createCell(workbookCellNumber++);
                            actualWorkbookCell.setCellValue(splitData[1]);
                            actualWorkbookCell.setCellStyle(styleForMismatch);
                        }
                        sheet.createRow(workbookRowCounter++);
                    }
                    continue;
                }

                if (!actualKeySheetData.containsKey(consolidatedKey)) {
                    if (!differencePresentFlag) {
                        differencePresentFlag = true;
                    }
                    List<List<String>> expectedData = expectedKeySheetData.get(consolidatedKey);
                    for (List<String> data : expectedData) {
                        int workbookCellNumber = 0;
                        Row columnAddressWorkbookRow = sheet.createRow(workbookRowCounter++);
                        Row expectedWorkbookRow = sheet.createRow(workbookRowCounter++);
                        Row actualWorkbookRow = sheet.createRow(workbookRowCounter++);
                        Cell expectedWorkbookCell = expectedWorkbookRow.createCell(workbookCellNumber);
                        Cell actualWorkbookCell = actualWorkbookRow.createCell(workbookCellNumber++);
                        expectedWorkbookCell.setCellValue(EXPECTED_CELL_IDENTIFIER);
                        expectedWorkbookCell.setCellStyle(styleForExpectedHeading);
                        actualWorkbookCell.setCellValue(ACTUAL_CELL_IDENTIFIER);
                        actualWorkbookCell.setCellStyle(styleForActualHeading);
                        for (String d : data) {
                            String[] splitData = d.split("->", 2);
                            Cell columnAddressWorkbookCell = columnAddressWorkbookRow.createCell(workbookCellNumber);
                            columnAddressWorkbookCell.setCellValue("Column Address: "+splitData[0]);
                            columnAddressWorkbookCell.setCellStyle(styleForBreaksHeading);
                            expectedWorkbookCell = expectedWorkbookRow.createCell(workbookCellNumber);
                            expectedWorkbookCell.setCellValue(splitData[1]);
                            expectedWorkbookCell.setCellStyle(styleForMismatch);
                            actualWorkbookCell = actualWorkbookRow.createCell(workbookCellNumber++);
                            actualWorkbookCell.setCellStyle(styleForMismatch);
                        }
                        sheet.createRow(workbookRowCounter++);
                    }
                    continue;
                }

                List<List<String>> expectedData = new ArrayList<>(expectedKeySheetData.get(consolidatedKey));
                List<List<String>> actualData = new ArrayList<>(actualKeySheetData.get(consolidatedKey));
                List<List<String>> removedData = new ArrayList<>();
                expectedData.stream().filter(actualData::contains).forEach(eData -> {
                    removedData.add(eData);
                    actualData.remove(eData);
                });
                removedData.forEach(expectedData::remove);
                removedData.clear();

                expectedData.stream().filter(data -> columnStartNumber <= data.size()).forEach(data -> {
                    int index = Collections.indexOfSubList(
                            actualData,
                            data.subList(
                                    Math.min(columnStartNumber, data.size()) -1,
                                    lastColumnNum == null ? data.size() : Math.min(lastColumnNum, data.size())
                            ));
                    if (index != -1) {
                        removedData.add(data);
                        actualData.remove(data);
                    }
                });
                removedData.forEach(expectedData::remove);

                if(!expectedData.isEmpty() || !actualData.isEmpty()){
                    if(!differencePresentFlag) {
                        differencePresentFlag = true;
                    }
                    int rowCounter = Math.max(expectedData.size(), actualData.size());
                    for (int counter = 0; counter < rowCounter; counter++) {
                        //actual excel has more rows
                        if(expectedData.size() - 1 < counter) {
                            int workbookCellNum = 0;
                            Row columnAddressWorkbookRow = sheet.createRow(workbookRowCounter++);
                            Row expectedWorkbookRow = sheet.createRow(workbookRowCounter++);
                            Row actualWorkbookRow = sheet.createRow(workbookRowCounter++);
                            Cell expectedWorkbookCell = expectedWorkbookRow.createCell(workbookCellNum);
                            Cell actualWorkbookCell = actualWorkbookRow.createCell(workbookCellNum++);
                            expectedWorkbookCell.setCellValue(EXPECTED_CELL_IDENTIFIER);
                            expectedWorkbookCell.setCellStyle(styleForExpectedHeading);
                            actualWorkbookCell.setCellValue(ACTUAL_CELL_IDENTIFIER);
                            actualWorkbookCell.setCellStyle(styleForActualHeading);
                            for (String data : actualData.get(counter)) {
                                String[] splitData = data.split("->", 2);
                                Cell columnAddressWorkbookCell = columnAddressWorkbookRow.createCell(workbookCellNum);
                                columnAddressWorkbookCell.setCellValue("Column Address: " + splitData[0]);
                                columnAddressWorkbookCell.setCellStyle(styleForBreaksHeading);
                                expectedWorkbookCell = expectedWorkbookRow.createCell(workbookCellNum);
                                expectedWorkbookCell.setCellStyle(styleForMismatch);
                                actualWorkbookCell = actualWorkbookRow.createCell(workbookCellNum++);
                                actualWorkbookCell.setCellValue(splitData[1]);
                                actualWorkbookCell.setCellStyle(styleForMismatch);
                            }
                            sheet.createRow(workbookRowCounter++);
                            continue;
                        }

                        //expected excel has more rows
                        if(actualData.size() - 1 < counter) {
                            int workbookCellNum = 0;
                            Row columnAddressWorkbookRow = sheet.createRow(workbookRowCounter++);
                            Row expectedWorkbookRow = sheet.createRow(workbookRowCounter++);
                            Row actualWorkbookRow = sheet.createRow(workbookRowCounter++);
                            Cell expectedWorkbookCell = expectedWorkbookRow.createCell(workbookCellNum);
                            Cell actualWorkbookCell = actualWorkbookRow.createCell(workbookCellNum++);
                            expectedWorkbookCell.setCellValue(EXPECTED_CELL_IDENTIFIER);
                            expectedWorkbookCell.setCellStyle(styleForExpectedHeading);
                            actualWorkbookCell.setCellValue(ACTUAL_CELL_IDENTIFIER);
                            actualWorkbookCell.setCellStyle(styleForActualHeading);
                            for (String data : expectedData.get(counter)) {
                                String[] splitData = data.split("->", 2);
                                Cell columnAddressWorkbookCell = columnAddressWorkbookRow.createCell(workbookCellNum);
                                columnAddressWorkbookCell.setCellValue("Column Address: " + splitData[0]);
                                columnAddressWorkbookCell.setCellStyle(styleForBreaksHeading);
                                expectedWorkbookCell = expectedWorkbookRow.createCell(workbookCellNum);
                                expectedWorkbookCell.setCellValue(splitData[1]);
                                expectedWorkbookCell.setCellStyle(styleForMismatch);
                                actualWorkbookCell = actualWorkbookRow.createCell(workbookCellNum++);
                                actualWorkbookCell.setCellStyle(styleForMismatch);
                            }
                            sheet.createRow(workbookRowCounter++);
                            continue;
                        }

                        List<String> tempExpectedData = expectedData.get(counter);
                        List<String> tempActualData = actualData.get(counter);
                        int expectedCellSize = tempExpectedData.size();
                        int actualCellSize = tempActualData.size();
                        List<String> tempColumnHeaderData = (expectedCellSize > actualCellSize) ? tempExpectedData : tempActualData;
                        int dataSizeMax = Math.max(expectedCellSize, actualCellSize);
                        int cellNum = columnLastNumber == -1 ? dataSizeMax : Math.min(columnLastNumber, dataSizeMax);

                        int workbookCellNum = 0;
                        Row columnAddressWorkbookRow = sheet.createRow(workbookRowCounter++);
                        Row expectedWorkbookRow = sheet.createRow(workbookRowCounter++);
                        Row actualWorkbookRow = sheet.createRow(workbookRowCounter++);
                        Cell expectedWorkbookCell = expectedWorkbookRow.createCell(workbookCellNum);
                        Cell actualWorkbookCell = actualWorkbookRow.createCell(workbookCellNum++);
                        expectedWorkbookCell.setCellValue(EXPECTED_CELL_IDENTIFIER);
                        expectedWorkbookCell.setCellStyle(styleForExpectedHeading);
                        actualWorkbookCell.setCellValue(ACTUAL_CELL_IDENTIFIER);
                        actualWorkbookCell.setCellStyle(styleForActualHeading);
                        for (int cnt = 0; cnt < dataSizeMax; cnt++) {
                            Cell columnAddressWorkbookCell = columnAddressWorkbookRow.createCell(workbookCellNum);
                            columnAddressWorkbookCell.setCellValue("Column Address: " + tempColumnHeaderData.get(cnt).split("->", 2)[0]);
                            columnAddressWorkbookCell.setCellStyle(styleForBreaksHeading);
                            String expData = (cnt < expectedCellSize) ? tempExpectedData.get(cnt).split("->", 2)[1] : "";
                            String actData = (cnt < actualCellSize) ? tempActualData.get(cnt).split("->", 2)[1] : "";

                            expectedWorkbookCell = expectedWorkbookRow.createCell(workbookCellNum);
                            expectedWorkbookCell.setCellValue(expData);
                            actualWorkbookCell = actualWorkbookRow.createCell(workbookCellNum++);
                            actualWorkbookCell.setCellValue(actData);
                            if (cnt >= columnStartNumber - 1 && cnt < cellNum && !actData.equals(expData)) {
                                expectedWorkbookCell.setCellStyle(styleForMismatch);
                                actualWorkbookCell.setCellStyle(styleForMismatch);
                            }
                        }
                        sheet.createRow(workbookRowCounter++);
                    }
                }
            }
            return differencePresentFlag;
        }

        private static CellStyle styleForExpectedHeading(Workbook workbook) {
            Font font = workbook.createFont();
            font.setColor(IndexedColors.WHITE.getIndex());
            font.setBold(true);
            CellStyle styleForExpectedHeading = workbook.createCellStyle();
            styleForExpectedHeading.setFillForegroundColor(IndexedColors.GREEN.getIndex());
            styleForExpectedHeading.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            styleForExpectedHeading.setFont(font);
            return styleForExpectedHeading;
        }

        private static CellStyle styleForActualHeading(Workbook workbook) {
            Font font = workbook.createFont();
            font.setColor(IndexedColors.WHITE.getIndex());
            font.setBold(true);
            CellStyle styleForActualHeading = workbook.createCellStyle();
            styleForActualHeading.setFillForegroundColor(IndexedColors.BLUE.getIndex());
            styleForActualHeading.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            styleForActualHeading.setFont(font);
            return styleForActualHeading;
        }

        private static CellStyle styleForBreaksHeading(Workbook workbook) {
            Font font = workbook.createFont();
            font.setColor(IndexedColors.BLACK.getIndex());
            font.setBold(true);
            CellStyle styleForBreaksHeading = workbook.createCellStyle();
            styleForBreaksHeading.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            styleForBreaksHeading.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            styleForBreaksHeading.setFont(font);
            return styleForBreaksHeading;
        }

        private static CellStyle styleForMismatch(Workbook workbook) {
            Font font = workbook.createFont();
            font.setColor(IndexedColors.BLACK.getIndex());
            font.setBold(true);
            CellStyle styleForMismatch = workbook.createCellStyle();
            styleForMismatch.setFillForegroundColor(IndexedColors.RED.getIndex());
            styleForMismatch.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            styleForMismatch.setFont(font);
            return styleForMismatch;
        }
    }


    /**
     * Utility class for reading Excel files and processing data.
     */
    private static class ExcelUtility {

        /**
         * Private constructor to prevent the creation of objects of the ExcelUtility class.
         * It contains only static methods and should not be instantiated.
         *
         * @throws IllegalStateException if an attempt is made to create an object of this class.
         */
        private ExcelUtility() {
            throw new IllegalStateException("Not designed to create object of ExcelUtility class as contain static methods only.");
        }

        /**
         * Reads an Excel file and returns the data in a list of SheetData objects.
         *
         * @param filePath the path to the Excel file to be read.
         * @return a list of SheetData objects representing the sheets and their data in the Excel file.
         * @throws IOException if an I/O error occurs while reading the file.
         */
        private static List<SheetData> readExcel(String filePath) throws IOException {
            logger.info("Read excel :'" + filePath + "' file");
            List<SheetData> rawData = new ArrayList<>();
            try (Workbook workbook = WorkbookFactory.create(new File(filePath))) {
                DataFormatter formatter = new DataFormatter();
                for (Sheet sheet : workbook) {
                    List<List<String>> allData = new ArrayList<>();
                    String sheetName = sheet.getSheetName();
                    for (Row row : sheet) {
                        List<String> dataInRow = new ArrayList<>();
                        if (row != null) {
                            for (int j = 0; j < row.getLastCellNum(); j++) {
                                Cell cell = row.getCell(j);
                                dataInRow.add(cell == null ? ExcelUtility.getExcelColumnAddress(j) + "->" :
                                        ExcelUtility.getExcelColumnAddress(j) + "->" + formatter.formatCellValue(cell));
                            }
                        }
                        allData.add(dataInRow);
                    }
                    SheetData sd = new SheetData(sheetName, allData);
                    rawData.add(sd);
                }
            }
            logger.info("Reading excel file : '" + filePath + "' is successful.");
            return rawData;
        }

        /**
         * Converts a column number to the corresponding Excel column address.
         *
         * @param columnNumber the column number to convert.
         * @return the Excel column address corresponding to the given column number.
         */
        private static String getExcelColumnAddress(int columnNumber){
            int dividend = columnNumber + 1;
            StringBuilder columnName = new StringBuilder();
            int modulo;
            while (dividend > 0) {
                modulo = (dividend - 1) % 16;
                columnName.insert(0, (char) (65 + modulo));
                dividend = (dividend - modulo) / 26;
            }
            return columnName.toString();
        }

        /**
         * Converts an Excel column address to the corresponding column number.
         *
         * @param columnAddress the Excel column address to convert.
         * @return the column number corresponding to the given Excel column address.
         */
        private static int getExcelColumnNumber(String columnAddress) {
            String[] ltrs = columnAddress.toUpperCase().split("");
            String chrs = " ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            int mode = chrs.length() - 1;
            int columnPosition = 0;
            for (int p =0; p < columnAddress.length(); p++) {
                columnPosition = columnPosition * mode + chrs.indexOf(ltrs[p]);
            }
            return columnPosition;
        }

    }

    /**
     * Represents the data of a single sheet in an Excel file.
     */
    private static class SheetData {
        private final String sheetName;
        private final List<List<String>> sheetData;

        /**
         * Constructs a new SheetData object with the specified sheet name and data.
         *
         * @param sheetName the name of the sheet.
         * @param sheetData the data contained in the sheet.
         */
        public SheetData (String sheetName, List<List<String>> sheetData){
            this.sheetName = sheetName;
            this.sheetData = sheetData;
        }

        /**
         * Returns the name of the sheet.
         * @return the name of the sheet.
         */
        public String getSheetName() {
            return sheetName;
        }

        /**
         * Returns the data contained in the sheet.
         * @return a list of rows, where each row is represented as a list of strings.
         */
        public List<List<String>> getSheetData() {
            return sheetData;
        }
    }
}
