package utils.file.manipulation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a mapping of key columns for each sheet in a workbook.
 */
public class WorkbookSheetsKeyColumns {
    private Map<String , List<String>> sheetKeyColumns = new HashMap<>();

    /**
     * Adds the key columns for a specified sheet.
     * @param sheetName the name of the sheet
     * @param columnAddress a list of column addresses representing the key columns
     */
    public void addSheetColumns(String sheetName, List<String> columnAddress){
        sheetKeyColumns.put(sheetName, columnAddress.stream().map(String::toUpperCase).collect(Collectors.toList()));
    }

    /**
     * Retrieves the key columns for a specified sheet.
     * @param sheetName the name of the sheet
     * @return a list of column addresses representing the key columns for the sheet
     */
    public List<String> getSheetKeyColumns(String sheetName) {
        return sheetKeyColumns.get(sheetName);
    }

    /**
     * Checks if key columns are present for a specified sheet.
     * @param sheetName the name of the sheet
     * @return true if key columns are present for the sheet, false otherwise
     */
    public boolean isKeyColumnPresentForSheet(String sheetName) {
        return sheetKeyColumns.containsKey(sheetName);
    }
}
