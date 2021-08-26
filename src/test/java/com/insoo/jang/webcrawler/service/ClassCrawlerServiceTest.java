package com.insoo.jang.webcrawler.service;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ClassCrawlerServiceTest {
    @Autowired
    ClassCrawlerService classCrawlerService;

    @Autowired
    Environment env;

    @Test
    public void 수업_세부_url을_가져온다(){
        //given
        String classUrl = "https://canvas.ssu.ac.kr/courses/";

        //when
        try{
            Connection.Response login = classCrawlerService.LoginToServer(env.getProperty("id"), env.getProperty("password"));
            List<String> urlArr = classCrawlerService.GetClassUrls(login);

            String urlStr = urlArr.get(0);

            //then
            assertThat(urlStr).contains(classUrl);
        }catch (Exception e){

        }
    }
}
