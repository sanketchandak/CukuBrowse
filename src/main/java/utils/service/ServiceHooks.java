package utils.service;

import core.web.browser.runner.BrowserType;
import core.web.browser.runner.WebDriverRunner;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.WebDriverListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import core.report.cleanup.CleanupTempDirectory;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

public class ServiceHooks {
    private static final Logger log = LoggerFactory.getLogger(ServiceHooks.class);
    private static final ThreadLocal<Scenario> scenario = new ThreadLocal<>();
    private WebDriver driver;
    private WebDriverRunner webDriverRunner;

    @Before("@chromeBrowser or @ChromeBrowser or @CHROMEBROWSER or @chromebrowser")
    public void configChromeBrowser(Scenario scenario) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        ServiceHooks.scenario.set(scenario);
        driverSetup(BrowserType.CHROME);
        log.info("Config Chrome Browser: Chrome Browser setup is successful");
    }

    @Before("@firefoxBrowser or @FIREFOXBROWSER or @FirefoxBrowser or @firefoxbrowser" +
            " or @mozillaFirefoxBrowser or @mozillafirefoxbrowser or @MozillaFirefoxBrowser or @MOZILLAFIREFOXBROWSER")
    public void configFirefoxBrowser(Scenario scenario) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        ServiceHooks.scenario.set(scenario);
        driverSetup(BrowserType.FIREFOX);
        log.info("Config Firefox Browser: Firefox Browser setup is successful");
    }

    @Before("@edgeBrowser or @EdgeBrowser or @edgebrowser or @EDGEBROWSER" +
            " or @MicrosoftEdgeBrowser or @microsoftEdgeBrowser or @microsoftedgebrowser or @MICROSOFTEDGEBROWSER")
    public void configEdgeBrowser(Scenario scenario) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        ServiceHooks.scenario.set(scenario);
        driverSetup(BrowserType.EDGE);
        log.info("Config Edge Browser: Edge Browser setup is successful");
    }

    @Before("@ieBrowser or @IEBrowser or @iebrowser or @IEBROWSER" +
            " or @InternetExplorerBrowser or @internetExplorerBrowser or @internetexplorerbrowser or @INTERNETEXPLORERBROWSER")
    public void configIEBrowser(Scenario scenario) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        ServiceHooks.scenario.set(scenario);
        driverSetup(BrowserType.IE);
        log.info("Config IE Browser: IE Browser setup is successful");
    }

    /*@Before("@operaBrowser or @OperaBrowser or @operabrowser or @OPERABROWSER")
    public void configOperaBrowser(Scenario scenario) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        ServiceHooks.scenario.set(scenario);
        driverSetup(BrowserType.OPERA);
        log.info("Config Opera Browser: Opera Browser setup is successful");
    }*/

    @Before("@safariBrowser or @SafariBrowser or @safaribrowser or @SAFARIBROWSER")
    public void configSafariBrowser(Scenario scenario) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        ServiceHooks.scenario.set(scenario);
        driverSetup(BrowserType.SAFARI);
        log.info("Config Safari Browser: Safari Browser setup is successful");
    }

    @After
    public void tearDownBrowser() {
        Scenario scenario = getCurrentScenario();
        if (scenario.isFailed()) {
            if (driver != null) {
                scenario.attach(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES), "image/png", scenario.getName());
            }
        }
        webDriverRunner.closeWebDriver();
        //driver = null;
        log.info("Tear down Browser: Browser closed successful");
    }

    private void driverSetup(BrowserType browserType) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        CleanupTempDirectory.main(null);
        webDriverRunner = WebDriverRunner.getInstance();
        setListeners();
        webDriverRunner.setWebDriver(browserType);
        driver = webDriverRunner.getWebDriver();
    }

    private void setListeners() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Properties prop = null;
        try (FileInputStream fis = new FileInputStream("configs/configuration.properties")) {
            prop = new Properties();
            prop.load(fis);
        } catch (IOException fnfe) {
            fnfe.printStackTrace();
        }
        assert prop != null;
        String eventHandlerNameWithDir = prop.getProperty("event.handler.classes");
        if (!eventHandlerNameWithDir.equals("")) {
            for (String eventHandler : Arrays.stream(eventHandlerNameWithDir.split(",")).collect(Collectors.toSet())) {
                webDriverRunner.addListener((WebDriverListener) Class.forName(eventHandler.trim()).getDeclaredConstructor().newInstance());
            }
        }
    }
    
    public static Scenario getCurrentScenario(){
        return scenario.get();
    }
}