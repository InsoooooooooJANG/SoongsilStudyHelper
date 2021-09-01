package com.insoo.jang.webcrawler.service;


import com.insoo.jang.webcrawler.domain.crawling.ClassCrawler;
import com.insoo.jang.webcrawler.module.DateModule;
import com.insoo.jang.webcrawler.web.dto.ClassCrawlerResponseDto;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private WebDriver myPage;
    private WebDriver classPage;
    private WebDriver attendancePage;

    public void loginToPage(){
        //System.setProperty("webdriver.chrome.driver", "/Users/jang-insu/Documents/OS/chromedriver");
        System.setProperty("webdriver.chrome.driver", "/Users/pc/Documents/os/chromedriver");
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
                            .isAttend(false)
                            .build();

                    returnVal.add(new ClassCrawlerResponseDto(errorClass));

                    idx++;
                    continue;
                }catch (Exception e){

                }
                wait = new WebDriverWait(driver, 30);
                by = By.xpath("//iframe[@id='tool_content']");

                classPage = wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(by));

                wait = new WebDriverWait(classPage, 5);
                by = By.xpath("//*[contains(text(), '모든 주차 펴기')]");

                WebElement toggleBtn = wait.until(ExpectedConditions.elementToBeClickable(by));
                String str = toggleBtn.getText();
                toggleBtn.click();

                wait = new WebDriverWait(classPage, 5);
                by = By.className("xncb-component-wrapper");

                lectures  = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));

                SimpleDateFormat transFormat = new SimpleDateFormat("MM월 dd일 HH:mm");

                if(lectures.stream().count()== 0){
                    ClassCrawler errorClass = ClassCrawler.builder()
                            .className(className)
                            .category("수업")
                            .title("아직 수업이 시작되지 않았습니다.")
                            .leftTime(0)
                            .isAttend(false)
                            .build();

                    returnVal.add(new ClassCrawlerResponseDto(errorClass));
                }

                for(WebElement lecture:lectures){
                    String lectureTitle = lecture.findElement(By.className("xncb-component-title")).getText();
                    List<WebElement> dates = lecture.findElements(By.className("xncb-component-periods-item-date"));
                    String strStartDate = dates.get(0).getText();
                    String strEndDate = dates.get(1).getText();

                    Date startDate = transFormat.parse(strStartDate);
                    Date endDate = transFormat.parse(strEndDate);

                    Calendar calStart = Calendar.getInstance();
                    calStart.setTime(startDate);
                    calStart.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));

                    Calendar calEnd = Calendar.getInstance();
                    calEnd.setTime(endDate);
                    calEnd.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));

                    if(DateModule.GetToday().compareTo(calEnd.getTime()) > 0){
                        if(!returnVal.stream().filter(o -> o.getClassName().equals(className)).findFirst().isPresent()){
                            ClassCrawler errorClass = ClassCrawler.builder()
                                    .className(className)
                                    .category("수업")
                                    .title("아직 수업이 시작되지 않았습니다.")
                                    .leftTime(0)
                                    .isAttend(false)
                                    .build();

                            returnVal.add(new ClassCrawlerResponseDto(errorClass));
                        }

                        break;
                    }

                    long dateDiff = calEnd.getTime().getTime() - calStart.getTime().getTime();

                    Boolean isAttend = false;

                    driver.get(classUrl + "/external_tools/6");
                    wait = new WebDriverWait(driver, 30);
                    by = By.xpath("//iframe[@id='tool_content']");

                    attendancePage = wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(by));

                    wait = new WebDriverWait(attendancePage, 5);
                    by = By.className("xnlsmpb-table");

                    wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));

                    List<WebElement> attendances = attendancePage.findElements(By.tagName("tr"));
                    for(WebElement attendance : attendances){
                        String lectureName;
                        try{
                            lectureName = attendance.findElement(By.className("component-title")).getText();
                        }catch (Exception e){
                            continue;
                        }

                        if(lectureName.equals(lectureTitle)){
                            try{
                                WebElement isComplete = attendance.findElement(By.className("complete"));

                                isAttend = true;
                            }catch (Exception e){
                                isAttend = false;
                            }
                        }
                    }


                    ClassCrawler newClass = ClassCrawler.builder()
                            .className(className)
                            .category("수업")
                            .title(lectureTitle)
                            .leftTime(dateDiff)
                            .isAttend(isAttend)
                            .build();

                    returnVal.add(new ClassCrawlerResponseDto(newClass));
                }

            }catch (Exception e){
                ClassCrawler errorClass = ClassCrawler.builder()
                        .className(className)
                        .category("수업")
                        .title("아직 수업이 시작되지 않았습니다.")
                        .leftTime(0)
                        .isAttend(false)
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
