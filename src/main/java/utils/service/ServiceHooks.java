package utils.service;

import core.browser.runner.BrowserType;
import core.browser.runner.WebDriverRunner;
//import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;
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
    private Scenario scenario;
    private WebDriver driver;
    private WebDriverRunner webDriverRunner;

    @Before("@chromeBrowser or @ChromeBrowser or @CHROMEBROWSER or @chromebrowser")
    public void configChromeBrowser(Scenario scenario) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        this.scenario = scenario;
        driverSetup(BrowserType.CHROME);
        log.info("Config Chrome Browser: Chrome Browser setup is successful");
    }

    @Before("@firefoxBrowser or @FIREFOXBROWSER or @FirefoxBrowser or @firefoxbrowser" +
            " or @mozillaFirefoxBrowser or @mozillafirefoxbrowser or @MozillaFirefoxBrowser or @MOZILLAFIREFOXBROWSER")
    public void configFirefoxBrowser(Scenario scenario) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        this.scenario = scenario;
        driverSetup(BrowserType.FIREFOX);
        log.info("Config Firefox Browser: Firefox Browser setup is successful");
    }

    @Before("@edgeBrowser or @EdgeBrowser or @edgebrowser or @EDGEBROWSER" +
            " or @MicrosoftEdgeBrowser or @microsoftEdgeBrowser or @microsoftedgebrowser or @MICROSOFTEDGEBROWSER")
    public void configEdgeBrowser(Scenario scenario) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        this.scenario = scenario;
        driverSetup(BrowserType.EDGE);
        log.info("Config Edge Browser: Edge Browser setup is successful");
    }

    @Before("@ieBrowser or @IEBrowser or @iebrowser or @IEBROWSER" +
            " or @InternetExplorerBrowser or @internetExplorerBrowser or @internetexplorerbrowser or @INTERNETEXPLORERBROWSER")
    public void configIEBrowser(Scenario scenario) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        this.scenario = scenario;
        driverSetup(BrowserType.IE);
        log.info("Config IE Browser: IE Browser setup is successful");
    }

    @Before("@operaBrowser or @OperaBrowser or @operabrowser or @OPERABROWSER")
    public void configOperaBrowser(Scenario scenario) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        this.scenario = scenario;
        driverSetup(BrowserType.OPERA);
        log.info("Config Opera Browser: Opera Browser setup is successful");
    }

    @Before("@safariBrowser or @SafariBrowser or @safaribrowser or @SAFARIBROWSER")
    public void configSafariBrowser(Scenario scenario) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        this.scenario = scenario;
        driverSetup(BrowserType.SAFARI);
        log.info("Config Safari Browser: Safari Browser setup is successful");
    }

    @After
    public void tearDownBrowser() {
        if (scenario.isFailed()) {
            if (driver != null) {
                //scenario.embed(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES), "image/png");
                scenario.attach(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES), "image/png",this.scenario.getName());
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
        try (FileInputStream fis = new FileInputStream("configs/Configuration.properties")) {
            prop = new Properties();
            prop.load(fis);
        } catch (IOException fnfe) {
            fnfe.printStackTrace();
        }
        assert prop != null;
        String eventHandlerNameWithDir = prop.getProperty("event.handler.classes");
        for (String eventHandler : Arrays.stream(eventHandlerNameWithDir.split(",")).collect(Collectors.toSet())) {
            webDriverRunner.addListener((WebDriverEventListener) Class.forName(eventHandler.trim()).getDeclaredConstructor().newInstance());
        }
    }
}