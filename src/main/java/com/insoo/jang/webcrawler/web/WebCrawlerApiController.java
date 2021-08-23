package com.insoo.jang.webcrawler.web;

import com.insoo.jang.webcrawler.service.NoticeCrawlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class WebCrawlerApiController {
    private final NoticeCrawlerService noticeCrawlerService;

    //공지사항 크롤링 요청
    @PostMapping("/api/crawlnotice")
    public Boolean CrawlNotice(){
        return noticeCrawlerService.crawl();
    }
}
