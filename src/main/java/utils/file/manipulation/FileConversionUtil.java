package utils.file.manipulation;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CukeBrowseException;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileConversionUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileConversionUtil.class);

    /**
     * Convert CSV file to Excel file
     *
     * @param csvFileNameWithPath Complete path for csv file
     * @param excelFileNameWithPath Complete path for Excel file
     * @throws Exception throw exception in case of any issue
     */
    public static void csvToEXCEL(String csvFileNameWithPath, String excelFileNameWithPath) throws Exception {
        csvToEXCEL(csvFileNameWithPath, excelFileNameWithPath, '\u0000');
    }

    /**
     * Convert CSV file to Excel file having csv separator
     *
     * @param csvFileNameWithPath Complete path for csv file
     * @param excelFileNameWithPath Complete path for Excel file
     * @param csvSeparator Separator used in csv file
     * @throws Exception throw exception in case of any issue
     */
    public static void csvToEXCEL(String csvFileNameWithPath, String excelFileNameWithPath, char csvSeparator) throws Exception {
        boolean validFileStatus = FileUtils.checkValidFile(csvFileNameWithPath);
        if (validFileStatus & csvFileNameWithPath.endsWith(".csv")) {
            FileOutputStream writer = null;
            Workbook myWorkBook = null;
            try {
                if (excelFileNameWithPath.endsWith("xls")) {
                    writer = new FileOutputStream(FileUtils.getOSFriendlyFilePath(excelFileNameWithPath));
                    myWorkBook = new HSSFWorkbook();
                } else if (excelFileNameWithPath.endsWith("xlsx")) {
                    writer = new FileOutputStream(FileUtils.getOSFriendlyFilePath(excelFileNameWithPath));
                    myWorkBook = new XSSFWorkbook();
                } else {
                    writer = new FileOutputStream(FileUtils.getOSFriendlyFilePath(excelFileNameWithPath + ".xlsx"));
                    myWorkBook = new XSSFWorkbook();
                }
                Sheet mySheet = myWorkBook.createSheet();
                List<String[]> csvDataList;
                CSVParser parser;
                CSVReader csvReader;
                if (csvSeparator != '\u0000') {
                    parser = new CSVParserBuilder()
                            .withQuoteChar('\"')
                            .build();
                } else {
                    parser = new CSVParserBuilder().withSeparator(csvSeparator)
                            .withQuoteChar('\"')
                            .build();
                }
                csvReader = new CSVReaderBuilder(new FileReader(FileUtils.getOSFriendlyFilePath(csvFileNameWithPath)))
                        .withCSVParser(parser)
                        .build();
                csvDataList = csvReader.readAll();
                for (int rowNo = 0; rowNo < csvDataList.size(); rowNo++) {
                    String[] columns = csvDataList.get(rowNo);
                    Row myRow = mySheet.createRow(rowNo);
                    for (int cellNo = 0; cellNo < columns.length; cellNo++) {
                        myRow.createCell(cellNo).setCellValue(columns[cellNo]);
                    }
                }
                myWorkBook.write(writer);
            } finally {
                myWorkBook.close();
                writer.close();
            }
        } else {
            throw new CukeBrowseException("'" + csvFileNameWithPath + "' is either doesn't exist or not '.csv' file or is a directory");
        }
    }

    public static void csvToJson(String csvFileNameWithPath, String jsonFileNameWithPath) throws Exception {
        csvToJson(csvFileNameWithPath, jsonFileNameWithPath, '\u0000');
    }

    public static void csvToJson(String csvFileNameWithPath, String jsonFileNameWithPath, char csvSeparator) throws Exception {
        boolean validFileStatus = FileUtils.checkValidFile(csvFileNameWithPath);
        if (validFileStatus & csvFileNameWithPath.endsWith(".csv")) {
            try(FileWriter jsonWriter = new FileWriter(FileUtils.getOSFriendlyFilePath(jsonFileNameWithPath))) {
                CSVReader csvReader;
                CSVParser parser;
                if(csvSeparator != '\u0000') {
                    parser = new CSVParserBuilder()
                            .withQuoteChar('\"')
                            .build();
                } else {
                    parser = new CSVParserBuilder().withSeparator(csvSeparator)
                            .withQuoteChar('\"')
                            .build();
                }
                csvReader = new CSVReaderBuilder(new FileReader(FileUtils.getOSFriendlyFilePath(csvFileNameWithPath)))
                        .withCSVParser(parser)
                        .build();
                String[] headers = csvReader.readNext();
                String[] nextLine;
                JsonArray jsonArray = new JsonArray();
                while ((nextLine = csvReader.readNext()) != null) {
                    JsonObject jsonObject = new JsonObject();
                    for (int i = 0; i < headers.length; i++) {
                        String header = headers[i];
                        String value = (i < nextLine.length) ? nextLine[i] : "";
                        jsonObject.addProperty(header, value);
                    }
                    jsonArray.add(jsonObject);
                }
                // Write the JSON array to file
                jsonWriter.write(jsonArray.toString());
            }
        } else {
            throw new CukeBrowseException("'" + csvFileNameWithPath + "' is either doesn't exist or not '.csv' file or is a directory");
        }
    }

    /**
     * Convert Excel file to CSV file
     *
     * @param excelFileNameWithPath Complete path for Excel file
     * @param csvFileNameWithPath Complete path for csv file
     * @throws Exception throw exception in case of any issue
     */
    public static void excelToCSV(String excelFileNameWithPath, String csvFileNameWithPath) throws Exception {
        excelToCSVWithSheetName(excelFileNameWithPath, null, csvFileNameWithPath, '\u0000');
    }

    public static void excelToCSV(String excelFileNameWithPath, String sheetName, String csvFileNameWithPath) throws Exception {
        excelToCSVWithSheetName(excelFileNameWithPath, sheetName, csvFileNameWithPath, '\u0000');
    }

    public static void excelToCSV(String excelFileNameWithPath, int sheetIndex, String csvFileNameWithPath) throws Exception {
        excelToCSVWithSheetIndex(excelFileNameWithPath, sheetIndex, csvFileNameWithPath, '\u0000');
    }

    public static void excelToCSV(String excelFileNameWithPath, String csvFileNameWithPath, char csvSeparator) throws Exception {
        excelToCSVWithSheetName(excelFileNameWithPath, null, csvFileNameWithPath, csvSeparator);
    }

    public static void excelToCSVWithSheetName(String excelFileNameWithPath, String sheetName, String csvFileNameWithPath, char csvSeparator) throws Exception {
        boolean validFileStatus = FileUtils.checkValidFile(excelFileNameWithPath);
        if(!validFileStatus) {
            throw new CukeBrowseException("'" + excelFileNameWithPath + "' is either doesn't exist or is a directory");
        }
        if(csvSeparator == '\u0000') {
            csvSeparator = ',';
        }
        try (Workbook workbook = WorkbookFactory.create(new File(FileUtils.getOSFriendlyFilePath(excelFileNameWithPath)));
             FileWriter csvFileWriter = new FileWriter(FileUtils.getOSFriendlyFilePath(csvFileNameWithPath));
             CSVPrinter csvPrinter = new CSVPrinter(csvFileWriter, CSVFormat.Builder.create().setDelimiter(csvSeparator).setEscape('\\').build())) {
                Sheet sheet;
                if(sheetName!=null) {
                    sheet = workbook.getSheet(sheetName);
                } else {
                    sheet = workbook.getSheetAt(0);
                }
                writeDataInCSV(sheet, csvPrinter);
        }
    }

    public static void excelToCSVWithSheetIndex(String excelFileNameWithPath, Integer sheetIndex, String csvFileNameWithPath, char csvSeparator) throws Exception {
        boolean validFileStatus = FileUtils.checkValidFile(excelFileNameWithPath);
        if(!validFileStatus) {
            throw new CukeBrowseException("'" + excelFileNameWithPath + "' is either doesn't exist or is a directory");
        }
        if(csvSeparator == '\u0000') {
            csvSeparator = ',';
        }
        try (Workbook workbook = WorkbookFactory.create(new File(FileUtils.getOSFriendlyFilePath(excelFileNameWithPath)));
             FileWriter csvFileWriter = new FileWriter(FileUtils.getOSFriendlyFilePath(csvFileNameWithPath));
             CSVPrinter csvPrinter = new CSVPrinter(csvFileWriter, CSVFormat.Builder.create().setDelimiter(csvSeparator).setEscape('\\').build())) {
            Sheet sheet;
            sheet = workbook.getSheetAt(Objects.requireNonNullElse(sheetIndex, 0));
            writeDataInCSV(sheet, csvPrinter);
        }
    }

    private static void writeDataInCSV(Sheet sheet, CSVPrinter csvPrinter) throws IOException {
        for (Row row : sheet) {
            for (Cell cell : row) {
                String cellValue;
                switch (cell.getCellType()) {
                    case STRING:
                        cellValue = cell.getStringCellValue();
                        break;
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            // Handle date cells
                            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            Date date = cell.getDateCellValue();
                            cellValue = dateFormat.format(date);
                        } else {
                            // Handle numeric cells
                            cellValue = String.valueOf(cell.getNumericCellValue());
                        }
                        break;
                    case BOOLEAN:
                        cellValue = String.valueOf(cell.getBooleanCellValue());
                        break;
                    case BLANK:
                    case _NONE:
                        // Handle blank cells
                        cellValue = "";
                        break;
                    case FORMULA:
                        // Handle formula cells
                        cellValue = cell.getCellFormula();
                        break;
                    case ERROR:
                        // Handle error cells
                        cellValue = "ERROR";
                        break;
                    default:
                        // Handle other cell types
                        cellValue = "N/A";
                        break;
                }
                csvPrinter.print(cellValue);
            }
            csvPrinter.println();
        }
    }

}
