package core.customreporter.parser;

import core.customreporter.constants.ProjectType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.file.manipulation.FileUtils;

import java.io.File;

public class GetParserFactory {
    protected static final Logger logger = LoggerFactory.getLogger(GetParserFactory.class);
    public static final String TestNGResultXMLFilePath;
    public static final String FunctionalityReportFilePath;
    public static final String SurefireReportsFilePath;
    static final String TargetDirectoryPath = System.getProperty("user.dir") + File.separator + "target";
    private final String BDDResultXMLFilePath;
    static String surefireReportsFolderName = "surefire-reports";
    static String targetFolderName = "target";
    static String testngResultsFileName = "testng-results.xml";
    static String functionalityReportFileName = "functionalityReport.xlsx";
    Parser parser;

    static {
        SurefireReportsFilePath = TargetDirectoryPath + File.separator + surefireReportsFolderName;
        TestNGResultXMLFilePath = SurefireReportsFilePath + File.separator + testngResultsFileName;
        FunctionalityReportFilePath = TargetDirectoryPath + File.separator + functionalityReportFileName;
    }

    public GetParserFactory(){
        BDDResultXMLFilePath = TargetDirectoryPath + File.separator + surefireReportsFolderName + File.separator + "cucumberReport"
                    + File.separator + "cucumber-reports" + File.separator + "Cucumber.xml";
    }

    public Parser getPlan(ProjectType projectType){
        try{
            switch (projectType){
                case TESTNG:
                    if(parser == null){
                        parser = new TestNGParser(TestNGResultXMLFilePath);
                    }
                    return parser;
                case CUCUMBER:
                    File testngResultXmlFile = new File(TestNGResultXMLFilePath);
                    if(parser == null && (testngResultXmlFile.exists() && !testngResultXmlFile.isDirectory())){
                        parser = new TestNGParser(TestNGResultXMLFilePath);
                    } else {
                        return new CucumberParser(BDDResultXMLFilePath);
                    }
            }
        } catch (Exception e) {
            logger.error("Exception while getting parser factory: " + e);
        }
        return null;
    }

}
