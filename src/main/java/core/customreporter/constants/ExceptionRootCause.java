/**
 * Class representing different categories of exception root causes.
 */

package core.customreporter.constants;

import java.util.Arrays;
import java.util.List;

public class ExceptionRootCause {
    public static final List<String> INFRA_ISSUES = Arrays.asList(
            "200 returned",
            "200 not returned"
    );

    public static final List<String> AUTOMATION_ISSUES = Arrays.asList(
            "NullPointerException",
            "NoClassDefFoundError"
    );

    public static final List<String> FUNCTIONAL_ISSUES = Arrays.asList(
            "ElementNotFound",
            "TimeoutException"
    );
}
