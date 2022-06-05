package ua.tqs.frostini.selenium;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RegisterPage {

    private WebDriver driver;

    //Page URL
    private static String PAGE_URL="http://localhost:3000/register";

    //Locators
    @FindBy(css = ".row:nth-child(2) > .register__textBox")
    WebElement name;

    @FindBy(css = ".row:nth-child(3) > .register__textBox")
    WebElement email;

    @FindBy(css = ".row:nth-child(4) > .register__textBox")
    WebElement password;

    @FindBy(css = ".row:nth-child(5) > .register__textBox")
    WebElement confirmPassword;

    @FindBy(css = ".register__btn")
    WebElement registerButton;

    @FindBy(css = ".popup-container")
    WebElement popupContainer;

    public RegisterPage(WebDriver driver){
        this.driver=driver;
        driver.get(PAGE_URL);
        //Initialise Elements
        PageFactory.initElements(driver, this);
    }

    public void setUserName(String userName) {
        name.sendKeys(userName);
    }

    public void setUserEmail(String userEmail) {
        email.sendKeys(userEmail);
    }

    public void setUserPassword(String userPassword) {
        password.sendKeys(userPassword);
    }

    public void setUserConfirmPassword(String userConfirmPassword) {
        confirmPassword.sendKeys(userConfirmPassword);
    }
    
    public void clickRegister() {
        registerButton.click();
    }

    public boolean isRegistered(String message) {
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".popup-container")));     
        WebElement registerMessage = popupContainer.findElement(By.tagName("h3"));
        return registerMessage.getText() == message;
    }

}
