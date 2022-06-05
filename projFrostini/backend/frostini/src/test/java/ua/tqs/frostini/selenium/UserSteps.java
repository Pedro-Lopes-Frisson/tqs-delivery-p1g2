package ua.tqs.frostini.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@SpringBootTest
public class UserSteps {

    private WebDriver driver = new FirefoxDriver();

    private RegisterPage registerPage;
    private UserLoginPage userLoginPage;
    private MenuPage menuPage;

    @After
    public void cleanUp() {
        driver.close();
    }

    // REGISTER

    @When("I navigate to the User register page")
    public void iNavigateRegisterPage() {
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
