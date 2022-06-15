package ua.tqs.delivera.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DashboardPage {
    private WebDriver driver;

    //Locators
    @FindBy(className = "title")
    WebElement title;

    //Constructor
    public DashboardPage(WebDriver driver){
        this.driver=driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isPageOpened(String pageTitle) {
        return title.getText().equals(pageTitle);
    }
}
