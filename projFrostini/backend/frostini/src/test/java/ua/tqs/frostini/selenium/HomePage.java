package ua.tqs.frostini.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {
    
    private WebDriver driver;

    //Locators
    @FindBy(tagName = "h1")
    WebElement title;

    //Constructor
    public HomePage(WebDriver driver, String url){
        this.driver=driver;
        driver.get(url);
        //Initialise Elements
        PageFactory.initElements(driver, this);
    }

    public boolean isPageOpened(String pageTitle) {
        return title.getText().equals(pageTitle);
    }
}
