package com.insoo.jang.webcrawler.web.dto;

import com.insoo.jang.webcrawler.domain.crawling.NoticeCrawler;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class NoticeCrawlerSaveRequestDto {
    private String category;
    private String title;
    private String register;
    private String registDate;

    @Builder
    public NoticeCrawlerSaveRequestDto(String registDate, String category, String title, String register){
        this.registDate = registDate;
        this.category = category;
        this.title = title;
        this.register = register;
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
