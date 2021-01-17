package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class Register {

    @Test
    public void register() {
        String email = "testas9@testas.com";
        String password = "Testukas10";
        String expectedErrorText = "Norėdami tęsti, privalote sutikti su taisyklėmis ir privatumo politika.";


        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.eurovaistine.lt");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        driver.findElement(By.id("onetrust-accept-btn-handler")).click();
       // driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);


        //Paspausti "PRISIJUNGIMAS"
        driver.findElement(By.cssSelector("a[href$='/login']")).click();

        //Patikrinti ar matomos prisijungimo ir registracijos formos ar ne
        boolean loginBlockVisible = driver.findElement(By.xpath("//form[@action='/login-check']")).isDisplayed();
        boolean registrationBlockVisible = driver.findElement(By.id("customer_registration")).isDisplayed();

        assertTrue(loginBlockVisible, "Login block is not visible");
        assertTrue(registrationBlockVisible, "Registration block is not visible");


        //Įrašyti registracijos formoje duomenis: email, password ir pakartot password
        driver.findElement(By.id("customer_registration_email")).sendKeys(email);
        driver.findElement(By.id("customer_registration_user_plainPassword_first")).sendKeys(password);
        driver.findElement(By.id("customer_registration_user_plainPassword_second")).sendKeys(password);

        //Uždėti 3 checkbox'us
        driver.findElement(By.id("customer_registration_marketing_generalOffers")).click();
        driver.findElement(By.id("customer_registration_marketing_personalOffers")).click();
        //driver.findElement(By.id("customer_registration_marketing_acceptSensitiveData")).click();
        driver.findElement(By.id("customer_registration_marketing_typeAddress")).click();

        //paspausti "Registracijos" mygtuką
        driver.findElement(By.xpath("//*[@id=\"customer_registration\"]/button")).click();  // data-click-disable


        //Patikrinti ar error tekstas rodomas jei nepaspaudėme sutikti su "Taisyklėmis"
        String actualErrorText = driver.findElement(By.cssSelector(".error-message ")).getText();  // surast kaip paselektint ta error message
        assertEquals(actualErrorText, expectedErrorText, "Text was different than expected");


        driver.quit();
    }
}
