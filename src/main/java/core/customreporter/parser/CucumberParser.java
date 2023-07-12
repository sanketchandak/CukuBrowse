package core.customreporter.parser;

import com.google.common.base.Charsets;
import core.customreporter.constants.TestResult;
import core.customreporter.constants.TestStatus;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class CucumberParser extends Parser{

    public CucumberParser(String cucumberResultXmlFilePath) {
        try (InputStream fileStream = Files.newInputStream(Paths.get(cucumberResultXmlFilePath));
        Reader reader = new BufferedReader(new InputStreamReader(fileStream, Charsets.UTF_8))) {
            Parser.InvalidXMLCharacterFilter filter = new InvalidXMLCharacterFilter(reader);
            InputSource inputSource = new InputSource(filter);
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            document = builder.parse(inputSource);
            document.getDocumentElement().normalize();
        } catch (Exception e) {
            try {
                File testNGResultXMLFile = new File(cucumberResultXmlFilePath);
                factory = DocumentBuilderFactory.newInstance();
                builder = factory.newDocumentBuilder();
                document = builder.parse(testNGResultXMLFile);
                document.getDocumentElement().normalize();
            } catch (Exception e2){
                logger.error("Couldn't parse Cucumber.xml file. Path of file is '"+cucumberResultXmlFilePath+"'. Exception: "+e2);
            }
        }
    }

    public void setPassedFailedSkippedTestDetails() {
        if (detailsOfTests.isEmpty()) {
            String testType = TestStatus.PASS.name() +
                    TestStatus.FAIL.name() +
                    TestStatus.SKIP.name();
            try {
                NodeList tMethods = document.getElementsByTagName("test-method");
                for (int temp = 0; temp < tMethods.getLength(); temp++) {
                    Node node = tMethods.item(temp);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) node;
                        Map<TestResult, String> testDetails = new HashMap<>();
                        String bddSuiteName = getSuiteName();
                        String bddTCName = eElement.getAttribute("name");
                        String bddTCFunctionalityName = eElement.getAttribute("classname");
                        String bddExceptionType = "";
                        String bddStacktrace = "";
                        String bddTestStatus = "";
                        boolean isFail;
                        try {
                            String exception = eElement.getElementsByTagName("failure").item(0).getTextContent();
                            String tempStacktrace = exception.substring(exception.indexOf("StackTrace:")+11);
                            tempStacktrace = tempStacktrace.substring(0, tempStacktrace.indexOf(':')).trim();
                            bddExceptionType = tempStacktrace;
                            bddStacktrace = exception.substring(exception.indexOf("StackTrace:")+11).trim();
                            bddTestStatus = TestStatus.FAIL.name();
                            isFail = true;
                        } catch (Exception e) {
                            isFail = false;
                        }

                        if (!isFail) {
                            try {
                                String exception = eElement.getElementsByTagName("skipped").item(0).getTextContent();
                                bddStacktrace = exception;
                                bddTestStatus = TestStatus.SKIP.name();
                            } catch (Exception e) {
                                bddTestStatus = TestStatus.PASS.name();
                            }
                        }

                        String bddDuration = eElement.getAttribute("time");
                        if (testType.contains(bddTestStatus)) {
                            List<Map<TestResult, String>> specificStatusAllTestDetails = detailsOfTests.getOrDefault(bddTestStatus, new ArrayList<>());
                            testDetails.put(TestResult.SUITE_NAME, bddSuiteName);
                            testDetails.put(TestResult.TC_NAME, bddTCName);
                            testDetails.put(TestResult.FUNCTIONALITY_NAME, bddTCFunctionalityName);
                            testDetails.put(TestResult.EXCEPTION_TYPE, bddExceptionType);
                            testDetails.put(TestResult.STACKTRACE, bddStacktrace);
                            testDetails.put(TestResult.RESULT, bddTestStatus);
                            testDetails.put(TestResult.TIME_TAKEN, bddDuration);
                            specificStatusAllTestDetails.add(testDetails);
                            detailsOfTests.put(bddTestStatus, specificStatusAllTestDetails);

                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Exception in getting passed, failed and skipped test details: "+e);
            }
            setDefaultTestStatusIfNotPresent();
        }
    }

    void mapTestCases(List<Map<TestResult, String>> listOfTests, HashMap<String, Integer> testCasesMapping) {
        if(testCasesMapping.isEmpty()){
            for (Map<TestResult, String> listOfTest : listOfTests){
                String key = listOfTest.get(TestResult.FUNCTIONALITY_NAME);
                testCasesMapping.put(key, testCasesMapping.getOrDefault(key, 0) + 1);
            }
        }
    }

    public String getSuiteFinishTimestamp() {
        if(SUITE_RUN_ID.isEmpty()) {
            if (document != null) {
                try {
                    SUITE_RUN_ID = ((Element) document.getElementsByTagName("suite").item(0)).getAttribute("started-at");
                    SUITE_RUN_ID = SUITE_RUN_ID.replace("T", "").replace("Z", "");
                    SUITE_RUN_ID = new SimpleDateFormat("dd-MMM-yy HH:mm:ss").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(SUITE_RUN_ID));
                } catch (Exception ignore) {
                }
            } else {
                SimpleDateFormat dtFormat = new SimpleDateFormat("dd-MMM-yy HH.mm.ss");
                SUITE_RUN_ID = dtFormat.format(new Date());
            }
        }
        return SUITE_RUN_ID;
    }
}
