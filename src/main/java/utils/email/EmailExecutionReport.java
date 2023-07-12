package utils.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class EmailExecutionReport {
    private static final Logger logger = LoggerFactory.getLogger(EmailExecutionReport.class);

    public static void main(String[] args) {
        try {
            if (args.length < 5) {
                logger.error("Email Execution Report: Invalid number of parameters provided. Expected 5 parameters. " +
                        "1st Boolean flag to decide whether to send execution report via email or not || " +
                        "2nd is Email To || 3rd is Email CC || 4th is Reply To || 5th Attachment path");
            } else if (args.length == 5) {
                if (args[0].toUpperCase().trim().equals("TRUE")) {
                    if (!args[4].isEmpty()) {
                        if (new File(args[4] + "/cucumber-html-reports/overview-features.html").exists()) {
                            //List<File> listOfAttachments
                        } else {
                        }
                    } else {
                    }
                } else if (args[0].toUpperCase().trim().equals("FALSE")) {
                    logger.warn("Email Execution Report: Send execution report via email flag is set to FALSE.");
                } else {
                    logger.error("Email Execution Report:  1st parameters is Expected to be Boolean (TRUE?FALSE) value. It is used to decide whether to send execution report via email." +
                            " Actual value is:" + args[0]);
                }
                GenerateEmail generateEmail = new GenerateEmail();
                String emailSubject;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
