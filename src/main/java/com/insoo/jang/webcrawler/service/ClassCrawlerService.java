package com.insoo.jang.webcrawler.service;


import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@PropertySource("classpath:application-login.properties")
public class ClassCrawlerService {
    private final String SOONSIL_LOGIN_URL = "https://smartid.ssu.ac.kr/Symtra_sso/smln.asp?apiReturnUrl=https%3A%2F%2Fclass.ssu.ac.kr%2Fxn-sso%2Fgw-cb.php";
    private final String SOONSIL_CLASS_MAIN_URL="https://class.ssu.ac.kr/mypage";

    @Autowired
    private Environment env;
    private WebDriver driver;

    public void loginToPage(){
        driver = new SafariDriver();

        driver.get(SOONSIL_LOGIN_URL);

        WebElement id = driver.findElement(By.id("userid"));
        id.clear();
        id.sendKeys(env.getProperty("id"));

        WebElement password = driver.findElement(By.id("pwd"));
        password.clear();
        password.sendKeys(env.getProperty("password"));

        WebElement loginBtn = driver.findElement(By.className("btn_login"));
        loginBtn.click();

        new WebDriverWait(driver, 3).until(ExpectedConditions.urlToBe("https://class.ssu.ac.kr/"));

    }

    public List<String> getClassUrls(){
        List<String> returnVal = new ArrayList<String>();
        driver.get(SOONSIL_CLASS_MAIN_URL);

        driver.switchTo().frame("fulliframe");

        List<WebElement> classBtnList = driver.findElements(By.xpath("//a[@class='xnch-link']"));

        for(WebElement classBtn:classBtnList) {
            String classUrl = classBtn.getAttribute("href");
            returnVal.add(classUrl);
        }

        return returnVal;
    }

    public void quitFromServer(){
        driver.quit();
    }
}
