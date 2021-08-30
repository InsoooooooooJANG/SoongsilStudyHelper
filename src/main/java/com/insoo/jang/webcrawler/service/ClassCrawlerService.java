package com.insoo.jang.webcrawler.service;


import com.insoo.jang.webcrawler.domain.crawling.ClassCrawler;
import com.insoo.jang.webcrawler.module.DateModule;
import com.insoo.jang.webcrawler.web.dto.ClassCrawlerResponseDto;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
@PropertySource("classpath:application-login.properties")
public class ClassCrawlerService {
    private final String SOONSIL_LOGIN_URL = "https://smartid.ssu.ac.kr/Symtra_sso/smln.asp?apiReturnUrl=https%3A%2F%2Fclass.ssu.ac.kr%2Fxn-sso%2Fgw-cb.php";
    private final String SOONSIL_CLASS_MAIN_URL="https://class.ssu.ac.kr/mypage";

    @Autowired
    private Environment env;
    private WebDriver driver;
    private WebDriver myPage;
    private WebDriver classPage;

    public void loginToPage(){
        System.setProperty("webdriver.chrome.driver", "/Users/pc/Documents/OS/chromedriver");
        driver = new ChromeDriver();

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

        WebDriverWait wait = new WebDriverWait(myPage, 30);
        By by = By.className("xnch-link");

        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));

        List<WebElement> classBtnList = myPage.findElements(By.className("xnch-link"));


        for(WebElement classBtn:classBtnList) {
            String classUrl = classBtn.getAttribute("href");
            returnVal.add(classUrl);
        }

        return returnVal;
    }

    public List<String> getClassNames(){
        List<String> returnVal = new ArrayList<String>();

        WebDriverWait wait = new WebDriverWait(myPage, 30);
        By by = By.className("xnch-title");

        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));

        List<WebElement> classBtnList = myPage.findElements(By.className("xnch-title"));

        for(WebElement classBtn:classBtnList) {
            String classUrl = classBtn.getText();
            returnVal.add(classUrl);
        }

        return returnVal;
    }

    public List<ClassCrawlerResponseDto> getClassInfos(){
        List<ClassCrawlerResponseDto> returnVal = new ArrayList<ClassCrawlerResponseDto>();

        driver.get(SOONSIL_CLASS_MAIN_URL);

        WebDriverWait wait = new WebDriverWait(driver, 30);
        By by = By.xpath("//iframe[@id='fulliframe']");

        myPage = wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(by));

        List<String> classNames = getClassNames();
        List<String> classUrls = getClassUrls();

        int idx = 0;

        for(String classUrl:classUrls){
            driver.get(classUrl+"/external_tools/2");
            List<WebElement> lectures;
            String className = classNames.get(idx);

            try {
                try{
                    driver.findElement(By.id("unauthorized_message"));

                    ClassCrawler errorClass = ClassCrawler.builder()
                            .className(classNames.get(idx))
                            .category("수업")
                            .title("아직 수업이 시작되지 않았습니다.")
                            .leftTime(0)
                            .build();

                    returnVal.add(new ClassCrawlerResponseDto(errorClass));

                    idx++;
                    continue;
                }catch (Exception e){

                }
                wait = new WebDriverWait(driver, 30);
                by = By.xpath("//iframe[@id='tool_content']");

                classPage = wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(by));

                wait = new WebDriverWait(classPage, 30);
                by = By.className("xncb-fold-toggle-button");

                wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));

                WebElement toggleBtn = classPage.findElement(By.className("xncb-fold-toggle-button"));
                toggleBtn.click();

                wait = new WebDriverWait(classPage, 30);
                by = By.className("xncb-component-wrapper");

                wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));

                lectures = classPage.findElements(By.className("xncb-component-wrapper"));

                SimpleDateFormat transFormat = new SimpleDateFormat("MM월 dd일 HH:mm");

                if(lectures.stream().count()== 0){
                    ClassCrawler errorClass = ClassCrawler.builder()
                            .className(className)
                            .category("수업")
                            .title("아직 수업이 시작되지 않았습니다.")
                            .leftTime(0)
                            .build();

                    returnVal.add(new ClassCrawlerResponseDto(errorClass));
                }

                for(WebElement lecture:lectures){
                    String lectureTitle = lecture.findElement(By.xpath("//p[@class='xncb-component-title']")).getText();
                    List<WebElement> dates = lecture.findElements(By.xpath("//span[@class='xncb-component-periods-item-date']"));
                    String strStartDate = dates.get(0).getText();
                    String strEndDate = dates.get(1).getText();

                    Date startDate = transFormat.parse(strStartDate);
                    Date endDate = transFormat.parse(strEndDate);

                    if(DateModule.GetToday().compareTo(startDate) > 0){
                        if(!returnVal.stream().filter(o -> o.getClassName().equals(className)).findFirst().isPresent()){
                            ClassCrawler errorClass = ClassCrawler.builder()
                                    .className(className)
                                    .category("수업")
                                    .title("아직 수업이 시작되지 않았습니다.")
                                    .leftTime(0)
                                    .build();

                            returnVal.add(new ClassCrawlerResponseDto(errorClass));
                        }

                        break;
                    }

                    long dateDiff = endDate.getTime() - startDate.getTime();

                    ClassCrawler newClass = ClassCrawler.builder()
                            .className(className)
                            .category("수업")
                            .title(lectureTitle)
                            .leftTime(dateDiff)
                            .build();

                    returnVal.add(new ClassCrawlerResponseDto(newClass));
                }

            }catch (Exception e){
                ClassCrawler errorClass = ClassCrawler.builder()
                        .className(className)
                        .category("수업")
                        .title("아직 수업이 시작되지 않았습니다.")
                        .leftTime(0)
                        .build();

                returnVal.add(new ClassCrawlerResponseDto(errorClass));

                idx++;
                continue;
            }



            idx ++;
        }

        return returnVal;
    }

    public void quitFromServer(){
        driver.quit();
    }
}
