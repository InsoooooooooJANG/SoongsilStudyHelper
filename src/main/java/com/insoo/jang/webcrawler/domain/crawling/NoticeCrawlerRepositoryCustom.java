package com.insoo.jang.webcrawler.domain.crawling;

import java.util.List;

public interface NoticeCrawlerRepositoryCustom {
    List<NoticeCrawler> findByCategory(String category);
}
