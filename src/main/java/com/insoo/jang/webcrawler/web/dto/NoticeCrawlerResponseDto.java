package com.insoo.jang.webcrawler.web.dto;

import com.insoo.jang.webcrawler.domain.crawling.NoticeCrawler;
import lombok.Getter;

import java.util.Date;

@Getter
public class NoticeCrawlerResponseDto {
    private Long Id;
    private String registDate;
    private String category;
    private String title;
    private String register;

    public NoticeCrawlerResponseDto (NoticeCrawler entity){
        this.Id = entity.getId();
        this.registDate = entity.getRegistDate();
        this.category = entity.getCategory();;
        this.title = entity.getTitle();
        this.register = entity.getRegister();
    }

    public NoticeCrawler toEntity(){
        return NoticeCrawler.builder()
                .registDate(registDate)
                .category(category)
                .title(title)
                .register(register)
                .build();
    }
}
