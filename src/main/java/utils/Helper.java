package utils;

import org.openqa.selenium.InvalidArgumentException;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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

    public static long convertTimeInSeconds(long duration, TimeUnit currentTimeUnit){
        switch (currentTimeUnit) {
            case DAYS:
                return TimeUnit.DAYS.toSeconds(duration);
            case HOURS:
                return TimeUnit.HOURS.toSeconds(duration);
            case MINUTES:
                return TimeUnit.MINUTES.toSeconds(duration);
            case SECONDS:
                return duration;
            case MILLISECONDS:
                return TimeUnit.MILLISECONDS.toSeconds(duration);
            case MICROSECONDS:
                return TimeUnit.MICROSECONDS.toSeconds(duration);
            case NANOSECONDS:
                return TimeUnit.NANOSECONDS.toSeconds(duration);
            default:
                throw new InvalidArgumentException("Invalid time unit parameter used as:"+currentTimeUnit);
        }
    }
}
