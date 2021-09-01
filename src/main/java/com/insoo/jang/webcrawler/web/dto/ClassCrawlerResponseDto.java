package com.insoo.jang.webcrawler.web.dto;

import com.insoo.jang.webcrawler.domain.crawling.ClassCrawler;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ClassCrawlerResponseDto {
    private long leftTime;
    private String category;
    private String className;
    private String title;
    private Boolean isAttend;

    public ClassCrawlerResponseDto(ClassCrawler entity){
        this.leftTime = entity.getLeftTime();
        this.category = entity.getCategory();
        this.className = entity.getClassName();
        this.title = entity.getTitle();
        this.isAttend = entity.getIsAttend();
    }
}
