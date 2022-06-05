package ua.tqs.frostini.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MenuPage {

    private WebDriver driver;

    //Page URL
    private static String PAGE_URL="http://localhost:3000/menu";

    //Locators
    @FindBy(tagName = "h1")
    WebElement title;

    //Constructor
    public MenuPage(WebDriver driver){
        this.driver=driver;
        driver.get(PAGE_URL);
        //Initialise Elements
        PageFactory.initElements(driver, this);
    }

    public boolean isPageOpened(String pageTitle) {
        return title.getText().equals(pageTitle);
    }
}
