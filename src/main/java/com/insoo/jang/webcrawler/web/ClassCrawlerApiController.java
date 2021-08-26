package com.insoo.jang.webcrawler.web;

import com.insoo.jang.webcrawler.service.ClassCrawlerService;
import com.insoo.jang.webcrawler.web.dto.ClassCrawlerResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ClassCrawlerApiController {
    private final ClassCrawlerService classCrawlerService;


    @PostMapping("/api/getclass")
    public List<ClassCrawlerResponseDto> GetClassName(){
        return classCrawlerService.GetClassClawlerDatas();
    }
}
