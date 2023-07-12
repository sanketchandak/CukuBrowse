package utils.file.manipulation;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for working with PDF files.
 */
public class PdfUtils {

    private static final Logger logger = LoggerFactory.getLogger(PdfUtils.class);

    /**
     * Checks if the given file is a PDF file.
     * @param file The file to check.
     * @return True if the file is a PDF file, false otherwise.
     */
    public static boolean isPDF(File file) {
        return file.getName().toLowerCase().endsWith(".pdf");
    }

    private static PDDocument getPDDocument(File file, String password) throws IOException {
        if ((password == null || password.isEmpty()) && !isPasswordProtected(file)) {
            return PDDocument.load(file);
        } else {
            return PDDocument.load(file, password);
        }
    }

    /**
     * Checks if a PDF file is password protected.
     * @param file The PDF file to check.
     * @return True if the PDF file is password protected, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    public static boolean isPasswordProtected(File file) throws IOException {
        try (PDDocument document = PDDocument.load(file)) {
            return document.isEncrypted();
        }
    }

    /**
     * Retrieves the number of pages in the given PDF file.
     * @param file The PDF file.
     * @return The number of pages in the PDF file.
     * @throws IOException If an I/O error occurs.
     */
    public static int getPageCount(File file) throws IOException {
        return getPageCount(file, null);
    }

    /**
     * Retrieves the number of pages in the given PDF file protected with a password.
     * @param file The PDF file.
     * @param password The password to unlock the PDF file.
     * @return The number of pages in the PDF file.
     * @throws IOException If an I/O error occurs.
     */
    public static int getPageCount(File file, String password) throws IOException {
        try (PDDocument document = getPDDocument(file, password)) {
            return document.getNumberOfPages();
        }
    }

    /**
     * Extracts the text content from the given PDF file.
     * @param file The PDF file.
     * @return The extracted text content.
     * @throws IOException If an I/O error occurs.
     */
    public static String extractPDFText(File file) throws IOException {
        return extractPDFText(file, null);
    }

    /**
     * Extracts the text content from the given PDF file protected with a password.
     * @param file The PDF file.
     * @param password The password to unlock the PDF file.
     * @return The extracted text content.
     * @throws IOException If an I/O error occurs.
     */
    public static String extractPDFText(File file, String password) throws IOException {
        try (PDDocument document = getPDDocument(file, password)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    /**
     * Extracts the text content from a specific page of the given PDF file.
     * @param file The PDF file.
     * @param page The page number (1-based index) to extract the text from.
     * @return The extracted text content.
     * @throws IOException If an I/O error occurs.
     */
    public static String extractPDFTextFromPage(File file, int page) throws IOException {
        return extractPDFTextFromPage(file, null, page);
    }

    /**
     * Extracts the text content from a specific page of the given PDF file protected with a password.
     * @param file The PDF file.
     * @param password The password to unlock the PDF file.
     * @param page The page number (1-based index) to extract the text from.
     * @return The extracted text content.
     * @throws IOException If an I/O error occurs.
     */
    public static String extractPDFTextFromPage(File file, String password, int page) throws IOException {
        try (PDDocument document = getPDDocument(file, password)) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setStartPage(page);
            stripper.setEndPage(page);
            return stripper.getText(document);
        }
    }

    /**
     * Counts the occurrences of a specific text in the given PDF file.
     * @param file The PDF file.
     * @param text The text to search for.
     * @return The number of occurrences of the text.
     * @throws IOException If an I/O error occurs.
     */
    public static int countOccurrencesOfText(File file, String text) throws IOException {
        return countOccurrencesOfText(file, null, text);
    }

    /**
     * Counts the occurrences of a specific text in the given PDF file protected with a password.
     * @param file The PDF file.
     * @param password The password to unlock the PDF file.
     * @param text The text to search for.
     * @return The number of occurrences of the text.
     * @throws IOException If an I/O error occurs.
     */
    public static int countOccurrencesOfText(File file, String password, String text) throws IOException {
        String content = extractPDFText(file, password);
        int count = 0;
        int index = content.indexOf(text);
        while (index != -1) {
            count++;
            index = content.indexOf(text, index + 1);
        }
        return count;
    }

    /**
     * Retrieves the author metadata from the given PDF file.
     * @param file The PDF file.
     * @return The author of the PDF file.
     * @throws IOException If an I/O error occurs.
     */
    public static String getAuthor(File file) throws IOException {
        try (PDDocument document = PDDocument.load(file)) {
            PDDocumentInformation info = document.getDocumentInformation();
            return info.getAuthor();
        }
    }

    /**
     * Retrieves the title metadata from the given PDF file.
     * @param file The PDF file.
     * @return The title of the PDF file.
     * @throws IOException If an I/O error occurs.
     */
    public static String getTitle(File file) throws IOException {
        try (PDDocument document = PDDocument.load(file)) {
            PDDocumentInformation info = document.getDocumentInformation();
            return info.getTitle();
        }
    }

    /**
     * Retrieves the creation date metadata from the given PDF file.
     * @param file The PDF file.
     * @return The creation date of the PDF file.
     * @throws IOException If an I/O error occurs.
     */
    public static String getCreationDate(File file) throws IOException {
        try (PDDocument document = PDDocument.load(file)) {
            PDDocumentInformation info = document.getDocumentInformation();
            return info.getCreationDate().toString();
        }
    }

    /**
     * Merges multiple PDF files into a single PDF file.
     * @param files The list of PDF files to merge.
     * @param mergedFile The output merged PDF file.
     * @throws IOException If an I/O error occurs.
     */
    public static void mergePDFs(List<File> files, File mergedFile) throws IOException {
        PDFMergerUtility merger = new PDFMergerUtility();
        for (File file : files) {
            merger.addSource(file);
        }
        merger.setDestinationFileName(mergedFile.getAbsolutePath());
        merger.mergeDocuments(null);
    }

    /**
     * Extracts images from a PDF file and saves them to the specified output directory.
     * @param pdfFile The PDF file.
     * @param outputDirectory The directory to save the extracted images.
     * @return A list of paths to the extracted images.
     * @throws IOException If an I/O error occurs.
     */
    public static List<String> extractImagesFromPdf(File pdfFile, String outputDirectory) throws IOException {
        return extractImagesFromPdf(pdfFile, null, outputDirectory);
    }

    /**
     * Extracts images from a password-protected PDF file and saves them to the specified output directory.
     * @param pdfFile The PDF file.
     * @param password The password to unlock the PDF file.
     * @param outputDirectory The directory to save the extracted images.
     * @return A list of paths to the extracted images.
     * @throws IOException If an I/O error occurs.
     */
    public static List<String> extractImagesFromPdf(File pdfFile, String password, String outputDirectory) throws IOException {
        List<String> imagePaths = new ArrayList<>();
        try (PDDocument document = getPDDocument(pdfFile, password)) {
            int imageIndex = 1;
            for (PDPage page : document.getPages()) {
                PDResources resources = page.getResources();
                Iterable<COSName> imageNames = resources.getXObjectNames();

                for (COSName imageName : imageNames) {
                    if (resources.isImageXObject(imageName)) {
                        PDImageXObject imageObject = (PDImageXObject) resources.getXObject(imageName);
                        // Convert the image object to a buffered image
                        BufferedImage bufferedImage = imageObject.getImage();
                        // Save the image to a file
                        String outputFilename = outputDirectory + File.separator + "image_" + imageIndex + ".png";
                        imagePaths.add(outputFilename);
                        ImageIO.write(bufferedImage, "PNG", new File(outputFilename));
                        imageIndex++;
                    }
                }
            }
        }
        return imagePaths;
    }

    /**
     * Compares the text content of two PDF files.
     * @param file1 The first PDF file.
     * @param file2 The second PDF file.
     * @return True if the text content of the files is equal, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    public static boolean compareTextOfPdfFiles(File file1, File file2) throws IOException {
        return compareTextOfPdfFiles(file1, file2, null, null);
    }

    /**
     * Compares the text content of two password-protected PDF files.
     * @param file1 The first PDF file.
     * @param file2 The second PDF file.
     * @param file1Password The password to unlock the first PDF file.
     * @param file2Password The password to unlock the second PDF file.
     * @return True if the text content of the files is equal, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    public static boolean compareTextOfPdfFiles(File file1, File file2, String file1Password, String file2Password) throws IOException {
        String file1Extract = extractPDFText(file1, file1Password);
        String file2Extract = extractPDFText(file2, file2Password);
        return file1Extract.equals(file2Extract);
    }

}
