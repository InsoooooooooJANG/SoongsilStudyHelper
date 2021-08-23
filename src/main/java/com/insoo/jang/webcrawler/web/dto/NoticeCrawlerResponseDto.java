package com.insoo.jang.webcrawler.web.dto;

import com.insoo.jang.webcrawler.domain.crawling.NoticeCrawler;
import lombok.Getter;

@Getter
public class NoticeCrawlerResponseDto {
    private Long Id;
    private String category;
    private String title;
    private String register;

    public NoticeCrawlerResponseDto (NoticeCrawler entity){
        this.Id = entity.getId();
        this.category = entity.getCategory();;
        this.title = entity.getTitle();
        this.register = entity.getRegister();
    }
}
