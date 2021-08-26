package com.insoo.jang.webcrawler.web;

import com.insoo.jang.webcrawler.service.NoticeCrawlerService;
import com.insoo.jang.webcrawler.web.dto.NoticeCrawlerResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class NoticeCrawlerApiContorller {
    private final NoticeCrawlerService noticeCrawlerService;

    //공지사항 크롤링 요청
    @PostMapping("/api/crawlnotice")
    public void CrawlNotice(){
        noticeCrawlerService.getNoticeCrawlingDatas();
    }

    @GetMapping("/api/getnotice")
    public List<NoticeCrawlerResponseDto> GetNotice(@RequestParam String category, @RequestParam String keyword){
        return noticeCrawlerService.getNotice(category, keyword);
    }

    @GetMapping("api/getclass")
    public void GetClass(){
        noticeCrawlerService.getClass();
    }
}
