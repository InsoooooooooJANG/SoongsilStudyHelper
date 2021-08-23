package com.insoo.jang.webcrawler.service;

import com.insoo.jang.webcrawler.domain.crawling.NoticeCrawlerRepository;
import com.insoo.jang.webcrawler.web.dto.NoticeCrawlerSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URL;

@RequiredArgsConstructor
@Service
public class NoticeCrawlerService {
    private final NoticeCrawlerRepository noticeCrawlerRepository;
    private static String SOONGIL_UNIV_URL="https://scatch.ssu.ac.kr/%ea%b3%b5%ec%a7%80%ec%82%ac%ed%95%ad/?f&keyword";

    @Transactional
    public Long save(NoticeCrawlerSaveRequestDto requestDto){
        return noticeCrawlerRepository.save(requestDto.toEntity()).getId();
    }

    @PostConstruct
    public void getNoticeDatas() throws IOException{
        Document doc = Jsoup.parse(new URL(SOONGIL_UNIV_URL).openStream(), "UTF-8", SOONGIL_UNIV_URL);
        Elements contents = doc.select("ul li div");

        for(Element content: contents){
            Elements noticeContents = content.select("div");
            System.out.println(noticeContents.toString());
        }

    }

    @Transactional
    public Boolean crawl(){


        return true;
    }
}
