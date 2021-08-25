package com.insoo.jang.webcrawler.domain.crawling;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class NoticeCrawler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String registDate;

    @Column(length=100, nullable = false)
    private String category;

    @Column(length=600, nullable = false)
    private String title;

    private String register;

    @Builder
    NoticeCrawler(String registDate, String category, String title, String register){
        this.registDate = registDate;
        this.category = category;
        this.title = title;
        this.register = register;
    }

}
