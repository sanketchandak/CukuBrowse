package utils.encryptdecrypt;

import io.github.mrspock182.Decryption;
import io.github.mrspock182.Encryption;
import io.github.mrspock182.constant.TypeEncryption;
import io.github.mrspock182.constant.Unicode;
import io.github.mrspock182.decryption.DecryptionAsymmetric;
import io.github.mrspock182.encryption.EncryptionAsymmetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.file.manipulation.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class EncryptDecryptUtils {
    private static final Logger logger = LoggerFactory.getLogger(EncryptDecryptUtils.class);
    private static final String STARTINGPATH = System.getProperty("user.dir") + File.separator + "target";
    private static final String PUBLICKEY = "public.key";
    private static final String PRIVATEKEY = "private.key";

    public static String getEncryptedTest(String testToEncrypt) throws IOException {
        String encryptString;
        String fileName = "encryptdecryptkey/public.key";
        ClassLoader classLoader = EncryptDecryptUtils.class.getClassLoader();
        String newFilePath = STARTINGPATH + File.separator + PUBLICKEY;
        File targetFile = new File(newFilePath);
        FileUtils.createFolderIfNotPresent(STARTINGPATH);
        try (InputStream inputStream = classLoader.getResourceAsStream(fileName)){
            Files.copy(inputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Encryption encryption = new EncryptionAsymmetric(Unicode.UTF8, TypeEncryption.RSA, newFilePath);
            encryptString = encryption.encrypt(testToEncrypt);
        } catch (IOException e){
            throw new RuntimeException(e);
        } finally {
            File file = new File(newFilePath);
            if (file.exists()) {
                file.delete();
            }
        }
        return encryptString;
    }

    public static String getDecryptedTest(String testToDecrypt) throws IOException {
        String decryptString;
        String fileName = "encryptdecryptkey/private.key";
        ClassLoader classLoader = EncryptDecryptUtils.class.getClassLoader();
        String newFilePath = STARTINGPATH + File.separator + PUBLICKEY;
        File targetFile = new File(newFilePath);
        FileUtils.createFolderIfNotPresent(STARTINGPATH);
        try (InputStream inputStream = classLoader.getResourceAsStream(fileName)){
            Files.copy(inputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Decryption decryption = new DecryptionAsymmetric(Unicode.UTF8, TypeEncryption.RSA, newFilePath);
            decryptString = decryption.toString(testToDecrypt);
        } catch (IOException e){
            throw new RuntimeException(e);
        } finally {
            File file = new File(newFilePath);
            if (file.exists()) {
                file.delete();
            }
        }
        return decryptString;
    }
}
