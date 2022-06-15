package ua.tqs.delivera.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AdminSignInPage {
    private WebDriver driver;

    @FindBy(className = "email")
    WebElement email;

    @FindBy(className = "password")
    WebElement password;

    @FindBy(className = "signin-btn")
    WebElement signInBtn;

    public AdminSignInPage(WebDriver driver, String url){
        this.driver=driver;
        driver.get(url);
        //Initialise Elements
        PageFactory.initElements(driver, this);
    }

    public void setEmail() {
        email.sendKeys("email@ua.pt");
    }

    public void setPassword() {
        password.sendKeys("password");
    }

    public void clickSignIn() {
        signInBtn.click();
    }
}
