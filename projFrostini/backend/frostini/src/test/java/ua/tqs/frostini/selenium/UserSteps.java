package ua.tqs.frostini.selenium;

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
public class UserSteps {

    private final FirefoxOptions options = new FirefoxOptions();
    private WebDriver driver;


    private HomePage homePage;
    private RegisterPage registerPage;
    private UserLoginPage userLoginPage;
    private MenuPage menuPage;

    @After
    public void cleanUp() {
        driver.close();
    }

    @Given("I access {string}")
    public void iAccess(String access) {

        WebDriverManager.firefoxdriver().setup();
        options.setHeadless(true);
        driver = new FirefoxDriver(options);

        homePage = new HomePage(driver, access);
    }

    // REGISTER

    @When("I navigate to the User register page")
    public void iNavigateRegisterPage() {
        homePage.clickRegister();
        registerPage = new RegisterPage(driver);
    }

    @Then("I fill in the necessary information")
    public void iFillRegisterInformation() {

        registerPage.setUserName("UserName");
        registerPage.setUserEmail("email@ua.pt");
        registerPage.setUserPassword("password");
        registerPage.setUserConfirmPassword("password");
    }

    @And("I click Register")
    public void iClickRegister() {
        registerPage.clickRegister();
    }

    @And("I should see a popup with {string}")
    public void iRegistered(String message) {
        registerPage.isRegistered(message);
    }

    // LOGIN

    @When("I navigate to the User login page")
    public void iNavigateLoginPage() {
        homePage.clickLogin();
        userLoginPage = new UserLoginPage(driver);
        menuPage = new MenuPage(driver);
    }

    @Then("I fill in the login necessary information")
    public void iFillLoginInformation() {
        userLoginPage.setEmail();
        userLoginPage.setPassword();
    }

    @And("I click Login")
    public void iClickLogin() {
        userLoginPage.clickLogin();
    }

    @And("I should be redirected to the {string} page")
    public void iLoggedIn(String menu) {
        menuPage.isPageOpened(menu);
    }
}
