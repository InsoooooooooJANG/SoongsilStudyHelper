package com.insoo.jang.webcrawler.service;


import com.insoo.jang.webcrawler.domain.crawling.ClassCrawler;
import com.insoo.jang.webcrawler.web.dto.ClassCrawlerResponseDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@PropertySource("classpath:application-login.properties")
public class ClassCrawlerServiceTest {
    @Autowired
    ClassCrawlerService classCrawlerService;

    private WebDriver drive;

    @Autowired
    Environment env;


    @Test
    public void 수업_세부_url을_가져온다(){
        drive = new SafariDriver();
        drive.get("https://class.ssu.ac.kr");

        assertThat(drive.getTitle()).isEqualTo("숭실대학교 스마트캠퍼스LMS");

        drive.quit();
    }

    @Test
    public void 수업_링크를_가져온다(){
        //given
        String mainUrl = "https://canvas.ssu.ac.kr/courses";

        classCrawlerService.loginToPage();
        List<String> urls = classCrawlerService.getClassUrls();
        classCrawlerService.quitFromServer();

        String url = urls.get(0);

        assertThat(url).contains(mainUrl);
    }

    @Test
    public void proerties_값을_가져온다(){
        //given
        String id = "20180435";

        //when
        String propId = env.getProperty("id");

        //then
        assertThat(propId).isEqualTo(id);
    }

    @Test
    public void 강의정보를_가져온다(){
        classCrawlerService.loginToPage();
        List<ClassCrawlerResponseDto> classInfos = classCrawlerService.getClassInfos();
        classCrawlerService.quitFromServer();

    }
}
