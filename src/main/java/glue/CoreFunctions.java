package glue;

import core.ElementImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CoreFunctions {

    private static final Logger logger = LoggerFactory.getLogger(CoreFunctions.class);
    public static CoreFunctions CoreFunctions =
            ThreadLocal.withInitial(CoreFunctions::new).get();

    private CoreFunctions() {
        if (CoreFunctions != null) {
            logger.error("Use CoreFunctions variable to get the single instance of this class.");
            throw new RuntimeException("Use CoreFunctions variable to get the single instance of this class.");
        }
    }

    public void when_I_import_elements(List<String> files) {
        logger.debug("when_I_import_elements(List<String> files:{})", files);
        ElementImporter.ElementImporter.importElements(files);
    }
}
