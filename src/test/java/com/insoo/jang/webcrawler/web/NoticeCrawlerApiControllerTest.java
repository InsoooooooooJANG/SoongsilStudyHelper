package com.insoo.jang.webcrawler.web;

import com.insoo.jang.webcrawler.domain.crawling.NoticeCrawlerRepository;
import org.aspectj.lang.annotation.After;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.xmlunit.util.Convert;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class NoticeCrawlerApiControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private NoticeCrawlerRepository noticeCrawlerRepository;

    @AfterEach
    public void tearDown() throws Exception {
        noticeCrawlerRepository.deleteAll();
    }

    @Test
    public void  공지사항을_서버로부터_가져온다() throws Exception{
        mvc.perform(post("/api/crawlnotice"))
                .andExpect(status().isOk());
    }

    @Test
    public void  공지사항을_확인한다() throws Exception{
        mvc.perform(post("/api/crawlnotice"))
                .andExpect(status().isOk());

        mvc.perform(get("/api/getnotice?category=''&keyword=''"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
