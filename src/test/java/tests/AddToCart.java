package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class AddToCart {
    @Test(dataProvider = "products")
    public void addToCart(String productToAdd) throws InterruptedException {
        String categoryToOpen = "Nereceptiniai vaistai";
        String subCategoryToOpen = "GRIPEX";
        String expectedMessage = "Jūsų pasirinktas vaistas " + productToAdd + " įdėtas į krepšelį!";

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.eurovaistine.lt");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        driver.findElement(By.id("onetrust-accept-btn-handler")).click();

        //įvedam į paieškos langelį terminą "Nereceptiniai vaistai"
        driver.findElement(By.xpath("//*[@id=\"search-block\"]/div/span/span/input[2]")).sendKeys(categoryToOpen);
        driver.findElement(By.xpath("//*[@id=\"search-block\"]/div/span/span/input[2]")).sendKeys(Keys.ENTER);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);


        //patikrinam ar atsidarė tinkamas puslapis "Nereceptiniai vaistai"
        String searchOpened = driver.findElement(By.cssSelector(".content-full pjax-products-container h1")).getText();
        Assert.assertEquals(searchOpened, categoryToOpen, "Wrong category opened");

        //įvedam į kt. paieškos langelį , ieškom vaisto pagal 'brand' "GRIPEX"
        driver.findElement(By.xpath("//*[@id=\"facet-brand\"]/div[2]/input")).sendKeys(subCategoryToOpen);
        //driver.findElement(By.xpath("//*[@id=\"search-block\"]/div/span/span/input[2]")).sendKeys(Keys.ENTER);
        Thread.sleep(2000);


        //Suradus "Gripex" pažymim checkbox'ą su reikšme "Gripex"
        Actions action = new Actions(driver);
        //action.moveToElement(driver.findElement(By.xpath("//*[@id=\"facet-brand\"]/div[2]/input")).sendKeys(subCategoryToOpen));
        action.moveToElement(driver.findElement(By.xpath(categoryToOpen))).build().perform();
        Thread.sleep(5000);
        driver.findElement(By.xpath("//*[@id=\"facet-brand\"]/div[3]/div[87]/label/input")).click();

        //Patikrinti ar paspaude ant checkbox'o su "GRIPEX"
        String categoryOpened = driver.findElement(By.cssSelector(".product-item-wrapper--left")).getText();
        Assert.assertEquals(categoryOpened, subCategoryToOpen, "Nepaspaudė ant tinkamos kategorijos");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);


        //Įdėti gerą produktą į krpešelį
        List<WebElement> products = driver.findElements(By.cssSelector("product-item-wrapper--left"));
        for (WebElement product : products) {
            String productName = product.findElement(By.cssSelector(".product-text underline")).getText();
            if (productName.equals(productToAdd)) {
                product.findElement(By.xpath("//*[@id=\"product-variants-form\"]/div[2]/button")).click();
                break;
            }
        }


        //Patikrinti ar geras produktas buvo įdėtas krepšelį.
        driver.findElement(By.id("cart-block")).click();
        String addedProductName = driver.findElement(By.cssSelector(".product-name display-inline")).getText();
        Assert.assertEquals(addedProductName, productToAdd, "Wrong product has been added to cart");

        driver.quit();
    }

    @DataProvider(name = "products")
    public Object[][] products() {
        return new Object[][]{
                {"Gripex, plėvele dengtos tabletės, N12"},
                {"Gripex, plėvele dengtos tabletės, N24"},
        };
    }
}
