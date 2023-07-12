package core.customreporter.parser;

import com.google.common.base.Charsets;
import core.customreporter.constants.TestResult;
import core.customreporter.constants.TestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

public class TestNGParser extends Parser{
    /**
     * The logger instance for logging messages.
     */
    protected static final Logger logger = LoggerFactory.getLogger(TestNGParser.class);

    public TestNGParser(String testNGResultXMLFilePath){
        try (InputStream fileStream = Files.newInputStream(Paths.get(testNGResultXMLFilePath));
        Reader reader = new BufferedReader(new InputStreamReader(fileStream, Charsets.UTF_8))){
            InvalidXMLCharacterFilter invalidXMLCharacterFilter = new InvalidXMLCharacterFilter(reader);
            InputSource is = new InputSource(invalidXMLCharacterFilter);
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            document = builder.parse(is);
            document.getDocumentElement().normalize();
        } catch (Exception e) {
            try {
                File testNGResultXMLFile = new File(testNGResultXMLFilePath);
                factory = DocumentBuilderFactory.newInstance();
                builder = factory.newDocumentBuilder();
                document = builder.parse(testNGResultXMLFile);
                document.getDocumentElement().normalize();
            } catch (Exception e2){
                logger.error("Couldn't parse testng-result.xml file. Path of file is '"+testNGResultXMLFilePath+"'. Exception: "+e2);
            }
        }
    }

    public void setPassedFailedSkippedTestDetails(){
        if(detailsOfTests.isEmpty()){
            String testType = TestStatus.PASS.name() +
                    TestStatus.FAIL.name() +
                    TestStatus.SKIP.name();
            try{
                NodeList tMethods = document.getElementsByTagName("test-method");
                for (int temp =0; temp < tMethods.getLength(); temp++){
                    Node node = tMethods.item(temp);
                    if(node.getNodeType() == Node.ELEMENT_NODE){
                        Element eElement = (Element) node;
                        Element suiteElement = (Element) eElement.getParentNode();
                        String fetchedExecutionStatus = eElement.getAttribute("status").trim();
                        if(testType.contains(fetchedExecutionStatus) && !eElement.hasAttribute("is-config")){
                            List<Map<TestResult, String>> specificStatusAllTestDetails = detailsOfTests.getOrDefault(fetchedExecutionStatus, new ArrayList<>());
                            Map<TestResult, String> testDetails = new HashMap<>();

                            testDetails.put(TestResult.SUITE_NAME, ((Element) node.getParentNode().getParentNode()).getAttribute("name"));

                            StringBuilder paramsData = new StringBuilder();
                            Node sibling = eElement.getNextSibling();

                            while(sibling!=null && !(sibling instanceof Element)){
                                sibling = sibling.getNextSibling();
                                while(sibling instanceof Element){
                                    if(!((Element) sibling).getAttribute("name").equals("afterMethod") ||
                                            !((Element) sibling).getAttribute("signature").contains("afterMethod(org.testng.ITestResult)")){
                                        sibling = sibling.getNextSibling();
                                    } else {
                                        break;
                                    }
                                }
                            }

                            String tcName = eElement.getAttribute("name").trim();
                            if(sibling != null){
                                NodeList nodeListNextSibling = ((Element) sibling).getElementsByTagName("param");
                                for(int i = 0; i < nodeListNextSibling.getLength(); i++) {
                                    paramsData.append(nodeListNextSibling.item(i).getTextContent().trim().replaceAll("[{}\n]", "")).append(",");
                                }
                                if(paramsData.toString().contains("TestResult name=")){
                                    tcName = paramsData.substring(paramsData.indexOf("TestResult name=") + "TestResult name".length(),
                                            paramsData.indexOf("status=")).trim();
                                }
                            }

                            testDetails.put(TestResult.TC_NAME, tcName);

                            NodeList nodeList = eElement.getElementsByTagName("param");
                            for (int i=0; i<nodeList.getLength(); i++){
                                paramsData.append(nodeList.item(i).getTextContent().replaceAll("[{}\n]", "")).append(",");
                            }

                            String keyColumnName = Arrays.stream(paramsData.toString().split(",")).filter(inputData -> inputData.contains("KeyColumnName"))
                                    .findFirst().orElse("").trim();
                            if(!keyColumnName.isEmpty()){
                                String finalKeyColumnName = keyColumnName.contains("=") ? keyColumnName.substring(keyColumnName.lastIndexOf("=")+1) : "";
                                String tcDetails = Arrays.stream(paramsData.toString().split(","))
                                        .filter(inputData -> inputData.contains(finalKeyColumnName + "="))
                                        .findFirst().orElse("").trim();
                                tcDetails = tcDetails.contains("=") ? tcDetails.substring(tcDetails.lastIndexOf("=")+1) : "";
                                testDetails.put(TestResult.EXCEL_INPUT_TC_NAME, tcDetails);
                            }

                            String tcModuleName = Arrays.stream(paramsData.toString().split(",")).filter(inputData -> inputData.contains("ModuleName="))
                                    .findFirst().orElse("").trim();
                            tcModuleName = tcModuleName.contains("=") ? tcModuleName.substring(tcModuleName.lastIndexOf("=")+1) : "";
                            testDetails.put(TestResult.EXCEL_INPUT_TC_MODULE, tcModuleName);
                            testDetails.putIfAbsent(TestResult.EXCEL_INPUT_TC_MODULE, "");

                            if(testDetails.get(TestResult.EXCEL_INPUT_TC_NAME).isEmpty()){
                                testDetails.put(TestResult.FUNCTIONALITY_NAME, suiteElement.getAttribute("name"));
                            } else {
                                testDetails.put(TestResult.FUNCTIONALITY_NAME, testDetails.get(TestResult.EXCEL_INPUT_TC_MODULE));
                            }

                            if(TestStatus.PASS.name().equalsIgnoreCase(fetchedExecutionStatus)){
                                testDetails.put(TestResult.EXCEPTION_TYPE, "");
                                testDetails.put(TestResult.STACKTRACE, "");
                            } else {
                                Element exceptionElement = (Element) eElement.getElementsByTagName("exception").item(0);
                                testDetails.put(TestResult.EXCEPTION_TYPE, exceptionElement==null ? "" : exceptionElement.getAttribute("class"));

                                Node eNode1 = eElement.getElementsByTagName("full-stacktrace").item(0);
                                Element exceptionNode1 = (Element) eNode1;
                                String reporterOutput = "";
                                if(eElement.getElementsByTagName("reporter-output").getLength() > 0){
                                    reporterOutput = eElement.getElementsByTagName("reporter-output").item(0).getTextContent();
                                }
                                testDetails.put(TestResult.STACKTRACE, exceptionNode1==null
                                ? "" : exceptionNode1.getTextContent()+"\n"+reporterOutput);
                            }

                            testDetails.put(TestResult.RESULT, eElement.getAttribute("status"));
                            testDetails.put(TestResult.TIME_TAKEN, eElement.getAttribute("duration-ms"));
                            specificStatusAllTestDetails.add(testDetails);
                            detailsOfTests.put(fetchedExecutionStatus, specificStatusAllTestDetails);
                        }
                    }
                }
            } catch (Exception e){
                logger.error("Exception in getting Passed, Failed and Skipped test Details. Exception: "+e);
            }
            setDefaultTestStatusIfNotPresent();
        }
    }

    void mapTestCases(List<Map<TestResult, String>> listOfTests, HashMap<String, Integer> testCasesMapping){
        if(testCasesMapping.isEmpty()){
            for (Map<TestResult, String> listOfTest : listOfTests){
                String key = !listOfTest.get(TestResult.FUNCTIONALITY_NAME).contains(".")
                        ? listOfTest.get(TestResult.FUNCTIONALITY_NAME)
                        : listOfTest.get(TestResult.FUNCTIONALITY_NAME).substring(0, listOfTest.get(TestResult.FUNCTIONALITY_NAME).lastIndexOf("."));
                testCasesMapping.put(key, testCasesMapping.getOrDefault(key, 0) + 1);
            }
        }
    }

    public String getSuiteFinishTimestamp(){
        if(SUITE_RUN_ID.isEmpty()) {
            try {
                SUITE_RUN_ID = ((Element) document.getElementsByTagName("suite").item(0)).getAttribute("started-at");
                SUITE_RUN_ID = SUITE_RUN_ID.replace("T", "").replace("Z", "");
                SUITE_RUN_ID = new SimpleDateFormat("dd-MMM-yy HH:mm:ss").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(SUITE_RUN_ID));
            } catch (Exception ignore) {
            }
        }
        return SUITE_RUN_ID;
    }
}
