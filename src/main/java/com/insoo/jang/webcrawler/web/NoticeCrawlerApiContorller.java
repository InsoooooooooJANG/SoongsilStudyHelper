package com.insoo.jang.webcrawler.web;

import com.insoo.jang.webcrawler.service.NoticeCrawlerService;
import com.insoo.jang.webcrawler.web.dto.NoticeCrawlerResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class NoticeCrawlerApiContorller {
    private final NoticeCrawlerService noticeCrawlerService;

    //공지사항 크롤링 요청
    @CrossOrigin(origins = "*")
    @PostMapping("/api/crawlnotice")
    public void CrawlNotice(){
        noticeCrawlerService.getNoticeCrawlingDatas();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/api/getnotice")
    public List<NoticeCrawlerResponseDto> GetNotice(@RequestParam String category, @RequestParam String keyword){
        noticeCrawlerService.getNoticeCrawlingDatas();
        return noticeCrawlerService.getNotice(category, keyword);
    }

}
