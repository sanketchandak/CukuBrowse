package utils.file.manipulation;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.*;

public class CSVComparator {
    protected static final Logger logger = LoggerFactory.getLogger(ExcelComparatorColumnBasedKey.class);
    private final CSV expectedCSV;
    private final CSV actualCSV;

    public CSVComparator(String expectedCSVPath, String actualCSVPath, char csvSeparator) throws IOException, CsvException {
        expectedCSV = new CSV(expectedCSVPath, csvSeparator);
        actualCSV = new CSV(actualCSVPath, csvSeparator);
    }

    public List<String> compareCSVForDifference() {
        logger.info("Compare '"+expectedCSV.getFilePath()+"' csv file with '"+actualCSV.getFilePath()+"' csv file.");
        List<String> difference = DifferenceEngine.difference(expectedCSV, actualCSV);
        if(!difference.isEmpty()) {
            logger.info("Breaks after comparing '"+expectedCSV.getFilePath()+"' csv file with '"+actualCSV.getFilePath()+"' csv file: "+difference);
        }
        logger.info("Successfully compared '"+expectedCSV.getFilePath()+"' csv file with '"+actualCSV.getFilePath()+"' csv file.");
        return difference;
    }

    public List<String> compareCSVForDifferenceForColumnAddress(int startColumnIndex, int endColumnIndex) {
        logger.info("Compare '"+expectedCSV.getFilePath()+"' csv file with '"+actualCSV.getFilePath()+"' csv file from column index '"+startColumnIndex+"' to '"+endColumnIndex);
        List<String> difference = DifferenceEngine.differenceBasedOnColumnIndex(expectedCSV, actualCSV, startColumnIndex, endColumnIndex);
        if(!difference.isEmpty()) {
            logger.info("Breaks after comparing '"+expectedCSV.getFilePath()+"' csv file with '"+actualCSV.getFilePath()+"' csv file: "+difference);
        }
        logger.info("Successfully compared '"+expectedCSV.getFilePath()+"' csv file with '"+actualCSV.getFilePath()+"' csv file.");
        return difference;
    }

    public List<String> compareCSVForDifferenceForRowRange(int startRowNumber, int endRowNumber) {
        logger.info("Compare '"+expectedCSV.getFilePath()+"' csv file with '"+actualCSV.getFilePath()+"' csv file from row number '"+startRowNumber+"' to '"+endRowNumber);
        List<String> difference = DifferenceEngine.differenceBasedOnRowNumber(expectedCSV, actualCSV, startRowNumber, endRowNumber);
        if(!difference.isEmpty()) {
            logger.info("Breaks after comparing '"+expectedCSV.getFilePath()+"' csv file with '"+actualCSV.getFilePath()+"' csv file from row number '"+startRowNumber+"' to '"+endRowNumber+"' having difference: "+difference);
        }
        logger.info("Successfully compared '"+expectedCSV.getFilePath()+"' csv file with '"+actualCSV.getFilePath()+"' csv file from row number '"+startRowNumber+"' to '"+endRowNumber);
        return difference;
    }

    private static class CSV {
        private final List<String[]> csvData;
        private final String filePath;
        private final char csvSeparator;

        private CSV (String filePath, char csvSeparator) throws IOException, CsvException {
            this.filePath = FileUtils.getOSFriendlyFilePath(filePath);
            this.csvSeparator = csvSeparator;
            csvData = readCSV(this.filePath);
        }

        private List<String[]> readCSV(String filePath) throws IOException, CsvException {
            FileReader fileReader = new FileReader(filePath);
            CSVParser parser = new CSVParserBuilder().withSeparator(this.csvSeparator).build();
            CSVReader csvReader = new CSVReaderBuilder(fileReader)
                    .withCSVParser(parser)
                    .build();
            return csvReader.readAll();
        }

        private char getCsvSeparator() {
            return csvSeparator;
        }

        private String getFilePath() {
            return filePath;
        }

        private List<String[]> getCsvData() {
            return csvData;
        }
    }

    private static class DifferenceEngine {
        private DifferenceEngine() {
            throw new IllegalStateException("Not designed to create object of DifferenceEngine class as contain static methods.");
        }

        private static List<String> difference(CSV expectedCSV, CSV actualCSV) {
            logger.info("Compare '"+expectedCSV.getFilePath()+"' csv file with '"+actualCSV.getFilePath()+"' csv file");
            int rowNumber = Math.max(expectedCSV.getCsvData().size(), actualCSV.getCsvData().size());
            List<String> differenceByRow = getDifference(expectedCSV, actualCSV, null, rowNumber, null, null);
            logger.info("Successfully compared '"+expectedCSV.getFilePath()+"' csv file with '"+actualCSV.getFilePath()+"' csv file");
            return differenceByRow;
        }

        private static List<String> differenceBasedOnColumnIndex(CSV expectedCSV, CSV actualCSV, int startColumnIndex, int endColumnIndex) {
            logger.info("Compare '"+expectedCSV.getFilePath()+"' csv file with '"+actualCSV.getFilePath()+"' csv file from column index '"+startColumnIndex+"' to '"+endColumnIndex+"'");
            int rowNumber = Math.max(expectedCSV.getCsvData().size(), actualCSV.getCsvData().size());
            if(startColumnIndex <=0) {
                String exceptionMessage = "Start column index should be greater than 0.";
                logger.error(exceptionMessage);
                throw new InvalidParameterException(exceptionMessage);
            }
            if (startColumnIndex > endColumnIndex) {
                String exceptionMessage = "Start column index '"+startColumnIndex+"' is greater than column index '"+endColumnIndex+"'";
                logger.error(exceptionMessage);
                throw new InvalidParameterException(exceptionMessage);
            }
            List<String> differenceByRow = getDifference(expectedCSV, actualCSV, null, rowNumber, startColumnIndex, endColumnIndex);
            logger.info("Successfully compared '"+expectedCSV.getFilePath()+"' csv file with "+actualCSV.getFilePath()+"' csv file from column index '"+startColumnIndex+"' to '"+endColumnIndex+"'");
            return differenceByRow;
        }

        private static List<String> differenceBasedOnRowNumber(CSV expectedCSV, CSV actualCSV, int startRowNumber, int endRowNumber) {
            logger.info("Compare '"+expectedCSV.getFilePath()+"' csv file with '"+actualCSV.getFilePath()+"' csv file from row number '"+startRowNumber+"' to '"+endRowNumber+"'");
            int rowNumber = Math.max(expectedCSV.getCsvData().size(), actualCSV.getCsvData().size());
            if(startRowNumber <=0) {
                String exceptionMessage = "Start row number should be greater than 0.";
                logger.error(exceptionMessage);
                throw new InvalidParameterException(exceptionMessage);
            }
            if (startRowNumber > endRowNumber) {
                String exceptionMessage = "Start row number '"+startRowNumber+"' is greater than end row number '"+endRowNumber+"'";
                logger.error(exceptionMessage);
                throw new InvalidParameterException(exceptionMessage);
            }
            List<String> differenceByRow = getDifference(expectedCSV, actualCSV, startRowNumber, endRowNumber, null, null);
            logger.info("Successfully compared '"+expectedCSV.getFilePath()+"' csv file with "+actualCSV.getFilePath()+"' csv file from row number '"+startRowNumber+"' to '"+endRowNumber+"'");
            return differenceByRow;
        }

        private static List<String> getDifference(CSV expectedCSV, CSV actualCSV, Integer startRowNumber, Integer endRowNumber, Integer startColumnIndex, Integer endColumnIndex) {
            LinkedList<String> differenceByRow = new LinkedList<>();
            Set<String> unmatchedColumns = new TreeSet<>();
            int rowStartNum = startRowNumber == null ? 1 : startRowNumber;
            int rowEndNum = endRowNumber == null ? Math.max(expectedCSV.getCsvData().size(), actualCSV.getCsvData().size()) : endRowNumber;
            int columnStartNum = startColumnIndex == null ? 1 : startColumnIndex;
            int columnEndNum = endColumnIndex == null ? 1 : endColumnIndex;
            for (int row = rowStartNum - 1; row < rowEndNum; row++) {
                // Actual csv has more rows
                if (expectedCSV.getCsvData().size() - 1 < row) {
                    differenceByRow.add("Row "+ (row + 1));
                    differenceByRow.add("expected -> expected row null |:| actual -> "+ Arrays.toString(actualCSV.getCsvData().get(row)));
                    continue;
                }

                // Expected csv has more rows
                if (actualCSV.getCsvData().size() - 1 < row) {
                    differenceByRow.add("Row "+ (row + 1));
                    differenceByRow.add("expected -> "+ Arrays.toString(actualCSV.getCsvData().get(row)) + " |:| actual -> actual row null");
                    continue;
                }

                int cellNum = Math.max(expectedCSV.getCsvData().get(row).length, actualCSV.getCsvData().get(row).length);

                // CSV start index is greater than max number of columns
                if(columnStartNum > cellNum) {
                    continue;
                }

                cellNum = columnEndNum == -1 ? cellNum : Math.min(columnEndNum, cellNum);

                for (int cell = columnStartNum - 1; cell < cellNum; cell++) {
                    String[] expectedRowCells = expectedCSV.getCsvData().get(row);
                    String[] actualRowCells = actualCSV.getCsvData().get(row);
                    String expectedCell = expectedRowCells.length-1 >= cell ? expectedRowCells[cell] : "";
                    String actualCell = actualRowCells.length-1 >= cell ? actualRowCells[cell] : "";
                    if(!expectedCell.equals(actualCell)) {
                        if (!differenceByRow.contains("Row "+ (row +1))) {
                            differenceByRow.add("Row "+ (row +1));
                        }
                        String sb = "Cell " + (cell +1)
                                + (StringUtils.isBlank(expectedCell) ? ": expected -> cell have no value" : ": expected -> " + expectedCell)
                                + (StringUtils.isBlank(actualCell) ? " |:| actual -> cell have no value" : ": actual -> " + actualCell);
                        differenceByRow.add(sb);
                        unmatchedColumns.add("Column "+ (cell+1));
                    }
                }
            }
            if (!differenceByRow.isEmpty()) {
                String discrepancySummary = unmatchedColumns.isEmpty() ? "Discrepancy summary" : "Discrepancy summary: " + String.join(", ", unmatchedColumns);
                differenceByRow.addFirst(discrepancySummary);
            }
            return differenceByRow;
         }
    }

}
