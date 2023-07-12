package utils;

import core.web.CommanderElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static core.web.Commander.*;

public class CommonOperation {

    public static void welcomeS(){
        open("https://www.google.com");
        maximizeBrowser();
        //driver.manage().window().maximize();
        System.out.println("---------------------------data-----------------------");
        System.out.println(find(By.xpath("//*[@id='SIvCob']")).getText());
    }

    public static void welcomeC(){
        open("https://www.google.com");
        maximizeBrowser();
        //driver.manage().window().maximize();
        //driver.findElement(By.xpath("//*[@class='NKcB']")).getText().equalsIgnoreCase("Resources to help teachers during");
        //GetText.getText(By.xpath("//*[@class='NKcBbd']")).contains("Wear a mask");
        System.out.println("---------------------------data-----------------------");
        waitForPageToReady();
        //find(By.xpath("//*[@class='gLFyf gsfi']")).sendKeys("Sanket Chandak"+ Keys.ENTER);
        CommanderElement gSearchBox = find(By.xpath("//*[@class='gLFyf gsfi']"));
        gSearchBox.sendKeys("Sanket Chandak");
        gSearchBox.sendKeys(""+Keys.ENTER);
        waitForPageToReady();
        CommanderElement topRibbonElement = find(By.cssSelector("[id='hdtb-msb-vis']"));
        System.out.println(topRibbonElement.getText());
        System.out.println(topRibbonElement.find(By.cssSelector("[data-sc='N']")).getText());
    }
}
