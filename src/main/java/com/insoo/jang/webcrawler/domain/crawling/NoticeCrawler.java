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

    @Column(length=100, nullable = false)
    private String category;

    @Column(length=600, nullable = false)
    private String title;

    private String register;

    @Builder
    NoticeCrawler(String category, String title, String register){
        this.category = category;
        this.title = title;
        this.register = register;
    }
}
