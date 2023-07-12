package utils.file.manipulation;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class WordUtils {

    private static final Logger logger = LoggerFactory.getLogger(WordUtils.class);

    /**
     * Checks if a file is a Word document based on the file extension.
     * @param file The file to check.
     * @return True if the file is a Word document, false otherwise.
     */
    public static boolean isWordDocument(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".docx") || fileName.endsWith(".doc");
    }

    /**
     * Extracts the plain text content from a Word document.
     * @param file The to extract text from.
     * @return The plain text content of the document.
     */
    public static String extractText(File file) throws IOException {
        XWPFDocument document = loadDocument(file);
        XWPFWordExtractor extractor = new XWPFWordExtractor(document);
        return extractor.getText();
    }

    /**
     * Counts the number of paragraphs in a Word document.
     * @param file The file to count paragraphs in.
     * @return The number of paragraphs in the document.
     */
    public static int countParagraphs(File file) throws IOException {
        try(XWPFDocument document = loadDocument(file)) {
            return document.getParagraphs().size();
        }
    }

    /**
     * Deletes a paragraph at the specified position in a Word document and saves the modified document.
     * @param file The file to delete a paragraph from.
     * @param position The position (paragraph index) of the paragraph to delete.
     * @param outputFile The file to save the modified document to.
     * @throws IOException If an I/O error occurs.
     */
    public static void deleteParagraphAndSave(File file, int position, File outputFile) throws IOException {
        XWPFDocument document = loadDocument(file);
        document.removeBodyElement(position);
        saveDocument(document, outputFile);
    }

    /**
     * Deletes a paragraph at the specified position in a Word document and saves the modified document.
     * @param filePath The file to delete a paragraph from.
     * @param position The position (paragraph index) of the paragraph to delete.
     * @param outputFile The file to save the modified document to.
     * @throws IOException If an I/O error occurs.
     */
    public static void deleteParagraphAndSave(String filePath, int position, File outputFile) throws IOException {
        try(FileInputStream fis = new FileInputStream(filePath);
            XWPFDocument document = new XWPFDocument(fis)) {
            document.removeBodyElement(position);
            saveDocument(document, outputFile);
        }
    }

    /**
     * Replaces all occurrences of a specific text in a Word document with a new text.
     * @param filePath The file to perform text replacement on.
     * @param searchText The text to search for.
     * @param replacementText The text to replace with.
     */
    public static void replaceText(String filePath, String searchText, String replacementText) throws IOException {
        try(FileInputStream fis = new FileInputStream(filePath);
            XWPFDocument document = new XWPFDocument(fis)) {
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                for (XWPFRun run : paragraph.getRuns()) {
                    String text = run.getText(0);
                    if (text != null && text.contains(searchText)) {
                        text = text.replace(searchText, replacementText);
                        run.setText(text, 0);
                    }
                }
            }
            saveDocument(document, filePath);
        }
    }

    private static XWPFDocument loadDocument(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        return new XWPFDocument(fis);
    }

    private static void saveDocument(XWPFDocument document, File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        document.write(fos);
        fos.close();
    }

    private static void saveDocument(XWPFDocument document, String filePath) throws IOException {
        // Save the modified document
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            document.write(fos);
        }
    }

}
