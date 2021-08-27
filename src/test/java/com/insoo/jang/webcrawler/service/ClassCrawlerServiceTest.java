package com.insoo.jang.webcrawler.service;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ClassCrawlerServiceTest {
    @Autowired
    ClassCrawlerService classCrawlerService;

    private WebDriver drive;

    @Autowired
    Environment env;

    @BeforeEach
    public void SetUp(){
        drive = new SafariDriver();
    }

    @AfterEach
    public void TearDown(){
        drive.quit();
    }

    @Test
    public void 수업_세부_url을_가져온다(){
        drive.get("https://class.ssu.ac.kr");

        assertThat(drive.getTitle()).isEqualTo("숭실대학교 스마트캠퍼스LMS");
    }
}
