package ua.tqs.frostini.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class UserLoginPage {
    
    private WebDriver driver;

    //Page URL
    private static String PAGE_URL="http://localhost:3000/login";

    //Locators
    @FindBy(css = ".row:nth-child(2) > .login__textBox")
    WebElement email;

    @FindBy(css = ".row:nth-child(3) > .login__textBox")
    WebElement password;

    @FindBy(css = ".login__btn")
    WebElement loginButton;

    //Constructor
    public UserLoginPage(WebDriver driver){
        this.driver=driver;
        driver.get(PAGE_URL);
        //Initialise Elements
        PageFactory.initElements(driver, this);
    }

    public void setEmail() {
        email.sendKeys("email@ua.pt");
    }

    public void setPassword() {
        password.sendKeys("password");
    }

    public void clickLogin() {
        loginButton.click();
    }

}
