package com.insoo.jang.webcrawler.web.dto;

import com.insoo.jang.webcrawler.domain.crawling.NoticeCrawler;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeCrawlerSaveRequestDto {
    private String category;
    private String title;
    private String register;

    @Builder
    public NoticeCrawlerSaveRequestDto(String category, String title, String register){
        this.category = category;
        this.title = title;
        this.register = register;
    }

    public NoticeCrawler toEntity(){
        return NoticeCrawler.builder()
                            .category(category)
                            .title(title)
                            .register(register)
                            .build();
    }
}
