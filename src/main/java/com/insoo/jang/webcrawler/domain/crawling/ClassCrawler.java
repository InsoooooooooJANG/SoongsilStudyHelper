package com.insoo.jang.webcrawler.domain.crawling;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class ClassCrawler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String leftTime;
    private String className;
    private String title;
    private String category;
    private String isAttend;

    @Builder
    public ClassCrawler (String className, String category, String title, String leftTime, String isAttend){
        this.leftTime = leftTime;
        this.title = title;
        this.className = className;
        this.category = category;
        this.isAttend = isAttend;
    }
}
