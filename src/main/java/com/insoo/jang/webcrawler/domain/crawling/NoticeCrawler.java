package com.insoo.jang.webcrawler.domain.crawling;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
public class NoticeCrawler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private Date registDate;

    @Column(length=100, nullable = false)
    private String category;

    @Column(length=600, nullable = false)
    private String title;

    private String register;

    @Builder
    NoticeCrawler(Date registDate, String category, String title, String register){
        this.registDate = registDate;
        this.category = category;
        this.title = title;
        this.register = register;
    }

}
