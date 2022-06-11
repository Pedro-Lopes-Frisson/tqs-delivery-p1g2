package ua.tqs.delivera.selenium;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.boot.test.context.SpringBootTest;


import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@SpringBootTest
public class AdminSteps {

    private final FirefoxOptions options = new FirefoxOptions();
    private WebDriver driver;

    private AdminSignInPage adminSignInPage;
    private DashboardPage dashboardPage;

    @After
    public void cleanUp() {
        driver.close();
    }
    
    // SIGN IN

    @When("I access {string}")
    public void iAccessSignInPage(String access) {

        WebDriverManager.firefoxdriver().setup();
        options.setHeadless(true);
        driver = new FirefoxDriver(options);

        adminSignInPage = new AdminSignInPage(driver, access);
        dashboardPage = new DashboardPage(driver);

    }

    @Then("I fill in the sign in with the necessary information")
    public void iFillSignInInformation() {
        adminSignInPage.setEmail();
        adminSignInPage.setPassword();
    }

    @And("I click Sign In")
    public void iClickSignIn() {
        adminSignInPage.clickSignIn();
    }

    @And("I should be redirected to the {string} page")
    public void iSignedIn(String dashboard) {
        dashboardPage.isPageOpened(dashboard);
    }
}
