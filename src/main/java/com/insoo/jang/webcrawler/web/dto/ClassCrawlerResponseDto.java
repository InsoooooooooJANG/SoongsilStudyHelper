package com.insoo.jang.webcrawler.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ClassCrawlerResponseDto {
    private int leftTime;
    private String category;
    private String className;
    private String title;

    public ClassCrawlerResponseDto(int leftTime, String category, String className, String title){
        this.leftTime = leftTime;
        this.category = category;
        this.className = className;
        this.title = title;
    }
}
