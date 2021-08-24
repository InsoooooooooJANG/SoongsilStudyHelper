package com.insoo.jang.webcrawler.domain.crawling;

import com.insoo.jang.webcrawler.module.DateModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class NoticeCrawlerRepositoryTest {
    @Autowired
    NoticeCrawlerRepository noticeCrawlerRepository;

    @AfterEach
    public void cleanup() {noticeCrawlerRepository.deleteAll();}

    @Test
    public void 공지사항_불러오기(){
        //given
        Date today = DateModule.GetToday();
        String category="학사";
        String title = "공지사항 1";
        String register="학과사무실";

        noticeCrawlerRepository.save(NoticeCrawler.builder().registDate(today)
                                                            .category(category)
                                                            .title(title)
                                                            .register(register)
                                                            .build());

        //when
        List<NoticeCrawler> crawlingList = noticeCrawlerRepository.findAll();


        //then
        NoticeCrawler noticeCrawler = crawlingList.get(0);
        assertThat(noticeCrawler.getRegistDate()).isEqualTo(today);
        assertThat(noticeCrawler.getCategory()).isEqualTo(category);
        assertThat(noticeCrawler.getTitle()).isEqualTo(title);
        assertThat(noticeCrawler.getRegister()).isEqualTo(register);
    }
}
