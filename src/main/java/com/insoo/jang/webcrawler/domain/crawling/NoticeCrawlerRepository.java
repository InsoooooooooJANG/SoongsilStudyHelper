package com.insoo.jang.webcrawler.domain.crawling;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoticeCrawlerRepository extends JpaRepository<NoticeCrawler, Long>, NoticeCrawlerRepositoryCustom {
    @Query("SELECT n FROM NoticeCrawler n WHERE n.title = :title")
    NoticeCrawler findByTitle(@Param("title") String title);

    List<NoticeCrawler> findByCatogoryNKeyword(@Param("category") String category, @Param("keyword") String keyword);
}
