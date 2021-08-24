package com.insoo.jang.webcrawler.domain.crawling;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeCrawlerRepository extends JpaRepository<NoticeCrawler, Long> {
    @Query("SELECT n FROM NoticeCrawler n WHERE n.title = :title")
    NoticeCrawler findByTitle(@Param("title") String title);
}
