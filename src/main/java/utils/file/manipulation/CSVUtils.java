package utils.file.manipulation;

import com.opencsv.*;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Utility class for manipulating CSV files using the OpenCSV library. */
public class CSVUtils {

    private static final Logger logger = LoggerFactory.getLogger(CSVUtils.class);

    private static final char DEFAULT_SEPARATOR = ',';

    /**
     * Reads the contents of a CSV file into a list of string arrays, representing rows and columns.
     * @param filePath The path to the CSV file.
     * @return A list of string arrays representing the rows and columns of the CSV file.
     * @throws IOException If an I/O error occurs while reading the file.
     * @throws CsvException If an error occurs while parsing the CSV content.
     */
    public static List<String[]> readCSV(String filePath) throws IOException, CsvException {
        return readCSV(filePath, DEFAULT_SEPARATOR);
    }

    /**
     * Reads the contents of a CSV file into a list of string arrays, representing rows and columns.
     * @param filePath The path to the CSV file.
     * @param separator The separator character used to separate values in the CSV file.
     * @return A list of string arrays representing the rows and columns of the CSV file.
     * @throws IOException If an I/O error occurs while reading the file.
     * @throws CsvException If an error occurs while parsing the CSV content.
     */
    public static List<String[]> readCSV(String filePath, char separator) throws IOException, CsvException {
        return readCSV(filePath, separator, CSVParser.DEFAULT_QUOTE_CHARACTER,
                CSVParser.DEFAULT_ESCAPE_CHARACTER);
    }

    /**
     * Reads the contents of a CSV file into a list of string arrays, representing rows and columns.
     * @param filePath The path to the CSV file.
     * @param separator The separator character used to separate values in the CSV file.
     * @param quoteChar The character used to quote values in the CSV file.
     * @param escapeChar The character used to escape special characters in the CSV file.
     * @return A list of string arrays representing the rows and columns of the CSV file.
     * @throws IOException If an I/O error occurs while reading the file.
     * @throws CsvException If an error occurs while parsing the CSV content.
     */
    public static List<String[]> readCSV(String filePath, char separator, char quoteChar, char escapeChar) throws IOException, CsvException {
        CSVParser parser = new CSVParserBuilder().withSeparator(separator)
                .withQuoteChar(quoteChar)
                .withEscapeChar(escapeChar)
                .build();
        try (FileReader fileReader = new FileReader(filePath);
             CSVReader reader = new CSVReaderBuilder(fileReader)
                     .withCSVParser(parser)
                     .build()) {
            return reader.readAll();
        }
    }

    /**
     * Writes the data represented by a list of string arrays into a CSV file.
     * @param data The data to write into the CSV file.
     * @param filePath The path to the CSV file.
     */
    public static void writeCSV(List<String[]> data, String filePath) {
        writeCSV(data, filePath, DEFAULT_SEPARATOR);
    }

    /**
     * Writes the data represented by a list of string arrays into a CSV file using the specified separator character.
     * @param data The data to write into the CSV file.
     * @param filePath The path to the CSV file.
     * @param separator The separator character used to separate values in the CSV file.
     */
    public static void writeCSV(List<String[]> data, String filePath, char separator) {
        writeCSV(data, filePath, separator, CSVWriter.DEFAULT_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
    }

    /**
     * Writes the data represented by a list of string arrays into a CSV file using the specified separator, quote character, escape character, and line end.
     * @param data The data to write into the CSV file.
     * @param filePath The path to the CSV file.
     * @param separator The separator character used to separate values in the CSV file.
     * @param quoteChar The character used to quote values in the CSV file.
     * @param escapeChar The character used to escape special characters in the CSV file.
     * @param lineEnd The line end character sequence used in the CSV file.
     */
    public static void writeCSV(List<String[]> data, String filePath, char separator, char quoteChar,
                                char escapeChar, String lineEnd) {
        if (data == null || data.isEmpty()) {
            return;
        }
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath), separator, quoteChar, escapeChar, lineEnd)) {
            writer.writeAll(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the number of columns in the CSV file.
     * @param filePath  The path to the CSV file.
     * @return The number of columns in the CSV file.
     * @throws IOException  If an I/O error occurs while reading the file.
     * @throws CsvException If an error occurs while parsing the CSV content.
     */
    public static int getColumnCount(String filePath) throws IOException, CsvException {
        return getColumnCount(filePath, DEFAULT_SEPARATOR);
    }

    /**
     * Returns the number of columns in the CSV file using the specified separator character.
     * @param filePath  The path to the CSV file.
     * @param separator The separator character used to separate values in the CSV file.
     * @return The number of columns in the CSV file.
     * @throws IOException  If an I/O error occurs while reading the file.
     * @throws CsvException If an error occurs while parsing the CSV content.
     */
    public static int getColumnCount(String filePath, char separator) throws IOException, CsvException {
        List<String[]> data = readCSV(filePath, separator);
        if (!data.isEmpty()) {
            return data.get(0).length;
        }
        return 0;
    }

    /**
     * Returns the number of rows in the CSV file.
     * @param filePath  The path to the CSV file.
     * @return          The number of rows in the CSV file.
     * @throws IOException  If an I/O error occurs while reading the file.
     * @throws CsvException If an error occurs while parsing the CSV content.
     */
    public static int getRowCount(String filePath) throws IOException, CsvException {
        return getRowCount(filePath, DEFAULT_SEPARATOR);
    }

    /**
     * Returns the number of rows in the CSV file using the specified separator character.
     *
     * @param filePath  The path to the CSV file.
     * @param separator The separator character used to separate values in the CSV file.
     * @return          The number of rows in the CSV file.
     * @throws IOException  If an I/O error occurs while reading the file.
     * @throws CsvException If an error occurs while parsing the CSV content.
     */
    public static int getRowCount(String filePath, char separator) throws IOException, CsvException {
        List<String[]> data = readCSV(filePath, separator);
        return data.size();
    }

    /**
     * Retrieves the value at the specified row and column in the CSV file.
     *
     * @param filePath  The path to the CSV file.
     * @param row       The row index.
     * @param col       The column index.
     * @return          The value at the specified row and column, or null if not found.
     * @throws IOException  If an I/O error occurs while reading the file.
     * @throws CsvException If an error occurs while parsing the CSV content.
     */
    public static String getValue(String filePath, int row, int col) throws IOException, CsvException {
        return getValue(filePath, row, col, DEFAULT_SEPARATOR);
    }

    /**
     * Retrieves the value at the specified row and column in the CSV file using the specified separator character.
     *
     * @param filePath  The path to the CSV file.
     * @param row       The row index.
     * @param col       The column index.
     * @param separator The separator character used to separate values in the CSV file.
     * @return          The value at the specified row and column, or null if not found.
     * @throws IOException  If an I/O error occurs while reading the file.
     * @throws CsvException If an error occurs while parsing the CSV content.
     */
    public static String getValue(String filePath, int row, int col, char separator) throws IOException, CsvException {
        List<String[]> data = readCSV(filePath, separator);
        if (row < data.size()) {
            String[] rowData = data.get(row);
            if (col < rowData.length) {
                return rowData[col];
            }
        }
        return null;
    }

    /**
     * Retrieves the values from the specified column in the CSV file.
     *
     * @param filePath  The path to the CSV file.
     * @param col       The column index.
     * @return          The list of values from the specified column.
     * @throws IOException  If an I/O error occurs while reading the file.
     * @throws CsvException If an error occurs while parsing the CSV content.
     */
    public static List<String> getColumnValues(String filePath, int col) throws IOException, CsvException {
        return getColumnValues(filePath, col, DEFAULT_SEPARATOR);
    }

    /**
     * Retrieves the values from the specified column in the CSV file using the specified separator character.
     *
     * @param filePath  The path to the CSV file.
     * @param col       The column index.
     * @param separator The separator character used to separate values in the CSV file.
     * @return          The list of values from the specified column.
     * @throws IOException  If an I/O error occurs while reading the file.
     * @throws CsvException If an error occurs while parsing the CSV content.
     */
    public static List<String> getColumnValues(String filePath, int col, char separator) throws IOException, CsvException {
        List<String> values = new ArrayList<>();
        List<String[]> data = readCSV(filePath, separator);
        for (String[] row : data) {
            if (col < row.length) {
                values.add(row[col]);
            }
        }
        return values;
    }

    /**
     * Sets the value at the specified row and column in the CSV file.
     *
     * @param filePath  The path to the CSV file.
     * @param row       The row index.
     * @param col       The column index.
     * @param value     The new value to set.
     * @throws IOException  If an I/O error occurs while reading or writing the file.
     * @throws CsvException If an error occurs while parsing the CSV content.
     */
    public static void setValue(String filePath, int row, int col, String value) throws IOException, CsvException {
        setValue(filePath, row, col, value, DEFAULT_SEPARATOR);
    }

    /**
     * Sets the value at the specified row and column in the CSV file using the specified separator character.
     *
     * @param filePath  The path to the CSV file.
     * @param row       The row index.
     * @param col       The column index.
     * @param value     The new value to set.
     * @param separator The separator character used to separate values in the CSV file.
     * @throws IOException  If an I/O error occurs while reading or writing the file.
     * @throws CsvException If an error occurs while parsing the CSV content.
     */
    public static void setValue(String filePath, int row, int col, String value, char separator) throws IOException, CsvException {
        List<String[]> data = readCSV(filePath, separator);
        if (row < data.size()) {
            String[] rowData = data.get(row);
            if (col < rowData.length) {
                rowData[col] = value;
            }
        }
        writeCSV(data, filePath, separator);
    }

    /**
     * Deletes the specified row from the CSV file.
     *
     * @param filePath  The path to the CSV file.
     * @param row       The row index to delete.
     * @throws IOException  If an I/O error occurs while reading or writing the file.
     * @throws CsvException If an error occurs while parsing the CSV content.
     */
    public static void deleteRow(String filePath, int row) throws IOException, CsvException {
        deleteRow(filePath, row, DEFAULT_SEPARATOR);
    }

    /**
     * Deletes the specified row from the CSV file using the specified separator character.
     *
     * @param filePath  The path to the CSV file.
     * @param row       The row index to delete.
     * @param separator The separator character used to separate values in the CSV file.
     * @throws IOException  If an I/O error occurs while reading or writing the file.
     * @throws CsvException If an error occurs while parsing the CSV content.
     */
    public static void deleteRow(String filePath, int row, char separator) throws IOException, CsvException {
        List<String[]> data = readCSV(filePath, separator);
        if (row < data.size()) {
            data.remove(row);
            writeCSV(data, filePath, separator);
        }
    }

    /**
     * Deletes the specified column from the CSV file.
     *
     * @param filePath  The path to the CSV file.
     * @param col       The column index to delete.
     * @throws IOException  If an I/O error occurs while reading or writing the file.
     * @throws CsvException If an error occurs while parsing the CSV content.
     */
    public static void deleteColumn(String filePath, int col) throws IOException, CsvException {
        deleteColumn(filePath, col, DEFAULT_SEPARATOR);
    }

    /**
     * Deletes the specified column from the CSV file using the specified separator character.
     *
     * @param filePath  The path to the CSV file.
     * @param col       The column index to delete.
     * @param separator The separator character used to separate values in the CSV file.
     * @throws IOException  If an I/O error occurs while reading or writing the file.
     * @throws CsvException If an error occurs while parsing the CSV content.
     */
    public static void deleteColumn(String filePath, int col, char separator) throws IOException, CsvException {
        List<String[]> data = readCSV(filePath, separator);
        List<String[]> modifiedData = new ArrayList<>(data.size());
        for (String[] row : data) {
            if (col < row.length) {
                String[] newRow = new String[row.length - 1];
                System.arraycopy(row, 0, newRow, 0, col);
                System.arraycopy(row, col + 1, newRow, col, row.length - col - 1);
                modifiedData.add(newRow);
            }
        }
        writeCSV(modifiedData, filePath, separator);
    }

}
