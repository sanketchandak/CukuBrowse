/**
 * Enumeration representing different categories of issues.
 */

package core.customreporter.constants;

public enum IssueCategory {
    INFRASTRUCTURE_ISSUE("Infrastructure Issue"),
    AUTOMATION_ISSUE("Automation Issue"),
    FUNCTIONAL_ISSUE("Functional Issue"),
    ASSERTION_ISSUE("Assertion Issue"),
    OTHER("Other"),
    CERTIFICATE_ISSUE("Certificate Issue"),
    SKIPPED("Skipped");

    final String value;

    /**
     * Constructs an IssueCategory with the specified value.
     * @param value String - The value associated with the IssueCategory
     */
    IssueCategory(String value) {
        this.value = value;
    }

    /**
     * Returns the string representation of the IssueCategory value.
     * @return String - The string representation of the IssueCategory value
     */
    @Override
    public String toString(){
        return value;
    }
}
