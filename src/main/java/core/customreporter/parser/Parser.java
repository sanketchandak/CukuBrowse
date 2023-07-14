/**
 * The Parser class is an abstract class that provides common functionality for parsing and analyzing test results.
 * It contains methods for mapping test cases, updating passed/failed/skipped test details, and calculating issue breakdown.
 * This class is meant to be extended by specific parser implementations.
 */

package core.customreporter.parser;

import core.customreporter.constants.ExceptionRootCause;
import core.customreporter.constants.IssueCategory;
import core.customreporter.constants.TestResult;
import core.customreporter.constants.TestStatus;
import org.apache.commons.lang3.StringUtils;
//import org.apache.xml.utils.XMLChar;
import org.apache.xmlbeans.impl.common.XMLChar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Parser {

    /**
     * The logger instance for logging messages.
     */
    protected static final Logger logger = LoggerFactory.getLogger(Parser.class);

    /**
     * A map to store the details of tests, categorized by test results.
     */
    protected static Map<String, List<Map<TestResult, String>>> detailsOfTests = new HashMap<>();
    static String SUITE_RUN_ID = "";
    DocumentBuilderFactory factory;
    DocumentBuilder builder;
    Document document;

    /**
     * A map to store the count of failed test cases.
     */
    protected HashMap<String, Integer> failedTestCases = new HashMap<>();

    /**
     * A map to store the count of skipped test cases.
     */
    protected HashMap<String, Integer> skippedTestCases = new HashMap<>();

    /**
     * A map to store the count of passed test cases.
     */
    protected HashMap<String, Integer> passedTestCases = new HashMap<>();

    /**
     * A map to store the count of issues breakdown by category.
     */
    protected HashMap<IssueCategory, Integer> issueBreakdown = new HashMap<>();
    int automationIssue;
    int certificateIssue;
    int functionalIssue;
    int infraIssue;
    int skippedIssue;
    int assertionIssue;
    int otherIssue;

    /**
     * Maps the test cases.
     *
     * @param listOfTests    the list of tests to be mapped
     * @param testCasesMapping The map to store the mapping of test cases.
     */
    abstract void mapTestCases(List<Map<TestResult, String>> listOfTests, HashMap<String, Integer> testCasesMapping);

    /**
     * Sets the details of passed, failed, and skipped tests.
     */
    public abstract void setPassedFailedSkippedTestDetails();

    /**
     * Gets the timestamp when the test suite finished.
     *
     * @return the timestamp of test suite finish
     */
    public abstract String getSuiteFinishTimestamp();

    /**
     * Initializes the issue breakdown counters.
     */
    void initializeIssueBreakdown(){
        automationIssue = 0;
        functionalIssue = 0;
        infraIssue = 0;
        skippedIssue = 0;
        otherIssue = 0;
        certificateIssue = 0;

        // Initialize issue breakdown map
        issueBreakdown.put(IssueCategory.FUNCTIONAL_ISSUE, 0);
        issueBreakdown.put(IssueCategory.AUTOMATION_ISSUE, 0);
        issueBreakdown.put(IssueCategory.INFRASTRUCTURE_ISSUE, 0);
        issueBreakdown.put(IssueCategory.SKIPPED, 0);
        issueBreakdown.put(IssueCategory.CERTIFICATE_ISSUE, 0);
        issueBreakdown.put(IssueCategory.OTHER, 0);
    }

    /**
     * Updates the counters for passed, failed, and skipped tests.
     */
    public void updatePassedFailedSkippedTests(){
        List<Map<TestResult, String>> listOfFailedTests = getPassedFailedSkippedTestDetails(TestStatus.FAIL);
        List<Map<TestResult, String>> listOfSkippedTests = getPassedFailedSkippedTestDetails(TestStatus.SKIP);
        List<Map<TestResult, String>> listOfPassedTests = getPassedFailedSkippedTestDetails(TestStatus.PASS);
        mapTestCases(listOfFailedTests, failedTestCases);
        mapTestCases(listOfSkippedTests, skippedTestCases);
        mapTestCases(listOfPassedTests, passedTestCases);
    }

    /**
     * Gets the list of test case details for a specific test status.
     *
     * @param testStatus the test status
     * @return the list of test case for test status pass, fail and skip
     */
    public List<Map<TestResult, String>> getPassedFailedSkippedTestDetails(TestStatus testStatus){
        if(detailsOfTests.isEmpty()){
            setPassedFailedSkippedTestDetails();
        }
        return detailsOfTests.getOrDefault(testStatus.name(), Collections.emptyList());
    }

    /**
     * This method combines the results of failed and skipped tests into a single list
     * @return a list of maps containing test results and corresponding details.
     */
    public List<Map<TestResult, String>> getAllFailedSkippedTestDetails(){
        return Stream.of(getPassedFailedSkippedTestDetails(TestStatus.FAIL),
                getPassedFailedSkippedTestDetails(TestStatus.SKIP))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    /**

     * Updates the issue breakdown based on the failed and skipped test details.
     * This method initializes the issue breakdown, retrieves the list of issue types, and updates the issue breakdown map by analyzing each issue type.
     * The issueBreakdown map is updated with the count for each category.
     */
    public void updateIssueBreakdown(){
        initializeIssueBreakdown();
        List<Map<TestResult, String>> listOfIssueTypes = getAllFailedSkippedTestDetails();
        /*for (Map<TestResult, String> listOfIssueType : listOfIssueTypes){
            issueBreakdown.put(fetchFailureRootCause(listOfIssueType.get(TestResult.STACKTRACE), listOfIssueType.get(TestResult.RESULT)),
                    issueBreakdown.get(fetchFailureRootCause(listOfIssueType.get(TestResult.STACKTRACE), listOfIssueType.get(TestResult.RESULT))) + 1);
        }*/
        listOfIssueTypes.forEach(issueType -> {
            IssueCategory category = fetchFailureRootCause(issueType.get(TestResult.STACKTRACE), issueType.get(TestResult.RESULT));
            issueBreakdown.merge(category, 1, Integer::sum);
        });
    }

    /**

     * Fetches the root cause category of a test failure based on the exception message and testng result.
     * @param exceptionMsg String - The exception message associated with the test failure
     * @param testngResult String - The TestNG result status of the test
     * @return IssueCategory - The root cause category of the test failure
     */
    public IssueCategory fetchFailureRootCause(String exceptionMsg, String testngResult){
        String finalExceptionMsg = exceptionMsg.toLowerCase();
        if(testngResult.equalsIgnoreCase(TestStatus.PASS.name())){
            return null;
        }
        /*try{
            if (TestStatus.SKIP.name().equalsIgnoreCase(testngResult) || exceptionMsg.contains("skipped")){
                return incrementSkippedIssue();
            } else if(exceptionMsg.contains("ERR_BAD_SSL_CLIENT_AUTH_CERT")){
                return incrementCertificateIssue();
            } else if(exceptionMsg.contains("org.openqa.selenium.WebDriverException: element click intercepted")){
                return incrementFunctionalIssue();
            } else if(exceptionCriteriaCheck(finalExceptionMsg, ExceptionRootCause.INFRA_ISSUES)){
                return incrementInfraIssue();
            } else if(exceptionCriteriaCheck(finalExceptionMsg, ExceptionRootCause.AUTOMATION_ISSUES)){
                return incrementAutomationIssue();
            } else if(exceptionCriteriaCheck(finalExceptionMsg, ExceptionRootCause.FUNCTIONAL_ISSUES)){
                return incrementFunctionalIssue();
            } else if(exceptionMsg.contains("AssertionError") ||
                    exceptionMsg.contains("junit.framework.AssertionFailedError")){
                return incrementAssertionIssue();
            } else {
                return incrementOtherIssue();
            }
        } catch (Exception e){
            logger.error("Exception - fetchFailureRootCause(): "+e);
            return null;
        }*/
        try {
            if (TestStatus.SKIP.name().equalsIgnoreCase(testngResult) || exceptionMsg.contains("skipped")) {
                return incrementIssueCategory(IssueCategory.SKIPPED);
            } else if (exceptionMsg.contains("ERR_BAD_SSL_CLIENT_AUTH_CERT")) {
                return incrementIssueCategory(IssueCategory.CERTIFICATE_ISSUE);
            } else if (exceptionMsg.contains("org.openqa.selenium.WebDriverException: element click intercepted")) {
                return incrementIssueCategory(IssueCategory.FUNCTIONAL_ISSUE);
            } else if (exceptionCriteriaCheck(finalExceptionMsg, ExceptionRootCause.INFRA_ISSUES)) {
                return incrementIssueCategory(IssueCategory.INFRASTRUCTURE_ISSUE);
            } else if (exceptionCriteriaCheck(finalExceptionMsg, ExceptionRootCause.AUTOMATION_ISSUES)) {
                return incrementIssueCategory(IssueCategory.AUTOMATION_ISSUE);
            } else if (exceptionCriteriaCheck(finalExceptionMsg, ExceptionRootCause.FUNCTIONAL_ISSUES)) {
                return incrementIssueCategory(IssueCategory.FUNCTIONAL_ISSUE);
            } else if (exceptionMsg.contains("AssertionError") ||
                    exceptionMsg.contains("junit.framework.AssertionFailedError")) {
                return incrementIssueCategory(IssueCategory.ASSERTION_ISSUE);
            } else {
                return incrementIssueCategory(IssueCategory.OTHER);
            }
        } catch (Exception e) {
            logger.error("Exception - fetchFailureRootCause(): " + e);
            return null;
        }
    }

    private IssueCategory incrementIssueCategory(IssueCategory category) {
        issueBreakdown.merge(category, 1, Integer::sum);
        return category;
    }

    /*private IssueCategory incrementInfraIssue() {
        infraIssue++;
        return IssueCategory.INFRASTRUCTURE_ISSUE;
    }

    private IssueCategory incrementAutomationIssue() {
        automationIssue++;
        return IssueCategory.AUTOMATION_ISSUE;
    }

    private IssueCategory incrementFunctionalIssue() {
        functionalIssue++;
        return IssueCategory.FUNCTIONAL_ISSUE;
    }

    private IssueCategory incrementAssertionIssue() {
        assertionIssue++;
        return IssueCategory.ASSERTION_ISSUE;
    }

    private IssueCategory incrementOtherIssue() {
        otherIssue++;
        return IssueCategory.OTHER;
    }

    private IssueCategory incrementCertificateIssue() {
        certificateIssue++;
        return IssueCategory.CERTIFICATE_ISSUE;
    }

    private IssueCategory incrementSkippedIssue() {
        skippedIssue++;
        return IssueCategory.SKIPPED;
    }
*/
    public int getAutomationIssue() { return automationIssue; }
    public int getFunctionalIssue() { return functionalIssue; }
    public int getInfraIssue() { return infraIssue; }
    public int getSkippedIssue() { return skippedIssue; }
    public int getAssertionIssue() { return assertionIssue; }
    public int getOtherIssue() { return otherIssue; }

    public Map<IssueCategory, Integer> getIssueBreakdown() { return issueBreakdown; }
    public Map<String, Integer> getFailedTestCases() { return failedTestCases; }
    public Map<String, Integer> getSkippedTestCases() { return skippedTestCases; }
    public Map<String, Integer> getPassedTestCases() { return passedTestCases; }

    public int getTotalPass(){
        return detailsOfTests.getOrDefault(TestStatus.PASS.name(), new ArrayList<>()).size();
    }

    public int getTotalFail(){
        return detailsOfTests.getOrDefault(TestStatus.FAIL.name(), new ArrayList<>()).size();
    }

    public int getTotalSkip(){
        return detailsOfTests.getOrDefault(TestStatus.SKIP.name(), new ArrayList<>()).size();
    }

    public Map<String, List<Map<TestResult, String>>> getAllTestDetails() { return detailsOfTests; }

    public int getTotalIgnored(){
        String ignored = document.getDocumentElement().getAttribute("ignored");
        if(!StringUtils.isEmpty(ignored)){
            return Integer.parseInt(ignored);
        } else {
            return 0;
        }
    }

    public int getTotalTests(){
        return getTotalPass() + getTotalFail() + getTotalSkip() + getTotalIgnored();
    }

    public String getStartTime(){
        return convertTestngFileTimeToDBTime(document.getElementsByTagName("suite").item(0).getAttributes().getNamedItem("started-at").getTextContent());
    }

    public String getEndTime(){
        return convertTestngFileTimeToDBTime(document.getElementsByTagName("suite").item(0).getAttributes().getNamedItem("finished-at").getTextContent());
    }

    public String getDuration(){
        return Optional.ofNullable(document.getElementsByTagName("suite").item(0).getAttributes().getNamedItem("duration-ms").getTextContent())
                .orElse("");
    }

    public void setDefaultTestStatusIfNotPresent(){
        /*detailsOfTests.put(TestStatus.PASS.name(), detailsOfTests.getOrDefault(TestStatus.PASS.name(), new ArrayList<>()));
        detailsOfTests.put(TestStatus.FAIL.name(), detailsOfTests.getOrDefault(TestStatus.FAIL.name(), new ArrayList<>()));
        detailsOfTests.put(TestStatus.SKIP.name(), detailsOfTests.getOrDefault(TestStatus.SKIP.name(), new ArrayList<>()));*/
        detailsOfTests.computeIfAbsent(TestStatus.PASS.name(), k -> new ArrayList<>());
        detailsOfTests.computeIfAbsent(TestStatus.FAIL.name(), k -> new ArrayList<>());
        detailsOfTests.computeIfAbsent(TestStatus.SKIP.name(), k -> new ArrayList<>());
    }

    public String getSuiteName(){
        return document.getElementsByTagName("suite").item(0).getAttributes().getNamedItem("name").getTextContent();
    }

    /**
     * Retrieves the first and last line of an exception string.
     * @param exceptionString String - The exception string to extract the first and last lines from
     * @return String - The first and last line of the exception string
     */
    public static String getFirstAndLAstLineOfException(String exceptionString){
        String[] splitExceptionString = exceptionString.trim().split("\n");
        if(splitExceptionString.length == 1){
            return splitExceptionString[0];
        } else {
            return splitExceptionString[0] + System.lineSeparator() + splitExceptionString[splitExceptionString.length - 1];
        }
    }

    /**
     * Trims the failure root cause string by removing the leading portion before the first occurrence of a dot ('.').
     * If the failure root cause does not contain a dot, the original string is returned.
     * @param failureRootCause String - The failure root cause string to be trimmed
     * @return String - The trimmed failure root cause string
     */
    public static String trimFailureRootCause(String failureRootCause){
        if(failureRootCause.contains(".")){
            return failureRootCause.substring(failureRootCause.indexOf(".")+1);
        } else {
            return failureRootCause;
        }
    }

    /**
     * Retrieves the TestNG name from the given input string based on the specified position.
     * The input string is split using the dot ('.') delimiter, and the element at the specified position from the end is returned in uppercase.
     * @param inputStr String - The input string containing the TestNG name
     * @param position int - The position of the TestNG name element (counted from the end, starting from 1)
     * @return String - The TestNG name at the specified position in uppercase
     */
    public static String getTestngName(String inputStr, int position){
        String[] arr = inputStr.split("\\.");
        return arr[arr.length - position].toUpperCase();
    }

    /**
     * Converts the TestNG file timestamp value to the database time format.
     * The TestNG timestamp value is expected to be in the format "yyyy-MM-dd'T'hh:mm:ssZ".
     * The converted database time format is "dd-MMM-yy hh:mm:ss.SSS".
     * @param testngTimestampVal String - The TestNG file timestamp value
     * @return String - The converted database time format
     */
    private String convertTestngFileTimeToDBTime(String testngTimestampVal){
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MMM-yy hh:mm:ss.SSS");
            Date date = inputDateFormat.parse(testngTimestampVal.replaceAll("Z$", "+0000"));
            return outputDateFormat.format(date);
        } catch (Exception e){
            logger.error("Exception in converting TestNG File time to DB time:"+e);
            return "";
        }
    }

    /**
     * Checks if the exception message contains any of the specified exceptions in the given exception list.
     * @param exceptionMsg String - The exception message to be checked
     * @param exceptionListUnderCriteria List<String> - The list of exceptions to check against
     * @return boolean - True if the exception message contains any of the exceptions in the list, false otherwise
     */
    private boolean exceptionCriteriaCheck(String exceptionMsg, List<String> exceptionListUnderCriteria){
        return exceptionListUnderCriteria.stream().anyMatch(exceptionMsg::contains);
    }

    static class InvalidXMLCharacterFilter extends FilterReader {

        protected InvalidXMLCharacterFilter(Reader in){
            super(in);
        }

        @Override
        public int read(char[] cbuf, int off, int len) throws IOException {
            int read = super.read(cbuf, off, len);
            if(read == -1){
                return read;
            }

            for (int i=off; i<off+read; i++){
                if(!XMLChar.isInvalid(cbuf[i])){
                    cbuf[i] = '?';
                }
            }
            return read;
        }
    }
}