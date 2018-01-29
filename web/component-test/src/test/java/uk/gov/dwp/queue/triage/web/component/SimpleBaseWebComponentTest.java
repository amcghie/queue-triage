package uk.gov.dwp.queue.triage.web.component;

import com.codeborne.selenide.WebDriverRunner;
import com.tngtech.jgiven.integration.spring.EnableJGiven;
import com.tngtech.jgiven.integration.spring.SimpleSpringRuleScenarioTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.dwp.queue.triage.web.server.QueueTriageWebApplication;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@EnableJGiven
@SpringBootTest(
        webEnvironment = RANDOM_PORT,
        classes = {
                QueueTriageWebApplication.class,
        }
)
@ActiveProfiles(value = "component-test")
@ComponentScan
public class SimpleBaseWebComponentTest<STAGE> extends SimpleSpringRuleScenarioTest<STAGE> {

        private static RemoteWebDriver driver;

        @BeforeClass
        public static void initialise() throws MalformedURLException {
                driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), DesiredCapabilities.firefox());
                driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                WebDriverRunner.setWebDriver(driver);
        }

        @AfterClass
        public static void tearDown() {
                driver.quit();
        }

}
