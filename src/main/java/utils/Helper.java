package utils;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {

    public static String removeQuotes(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        if (str.equals("''") || str.equals("\"\"")) {
            return "";
        }
        if (str.startsWith("'") && str.endsWith("'") ||
                str.startsWith("\"") && str.endsWith("\"")) {
        return str.replaceFirst("'","")
                .replaceFirst("\"","")
                .replaceAll("'$","")
                .replaceAll("\"$","");
        }
        return str;
    }

    public static File resolveFileSafely(String filePath) {
        File result = null;
        try{
            // is absolute path
            Path path = Paths.get(filePath);
            if(Files.exists(path)){
                return path.toFile();
            }

            //is relative path under project root
            result = new File(System.getProperty("user.dir")+File.separator+filePath);
            if(result.exists()){
                return result;
            }

            //is relative path under classpath
            URL resource = Thread.currentThread().getContextClassLoader().getResource(filePath);
            if(resource != null){
                result = new File(resource.toURI());
                return result;
            }
        } catch (Exception ignore) {
            //throw new IllegalArgumentException("File not found! "+filePath);
        }
        return result;
    }

    public static String getTimestamp(){
        return Thread.currentThread().getId()+new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date());
    }
}
