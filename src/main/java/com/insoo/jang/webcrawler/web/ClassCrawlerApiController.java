package com.insoo.jang.webcrawler.web;

import com.insoo.jang.webcrawler.service.ClassCrawlerService;
import com.insoo.jang.webcrawler.web.dto.ClassCrawlerResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ClassCrawlerApiController {
    private final ClassCrawlerService classCrawlerService;

    @CrossOrigin(origins = "*")
    @GetMapping("/api/getclassinfos")
    public List<ClassCrawlerResponseDto> GetClassInfos(){
        classCrawlerService.loginToPage();
        List<ClassCrawlerResponseDto> returnValue =  classCrawlerService.getClassInfos();
        classCrawlerService.quitFromServer();
        return returnValue;
    }
}
