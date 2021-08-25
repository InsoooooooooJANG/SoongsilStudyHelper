package com.insoo.jang.webcrawler.domain.crawling;

import java.util.List;

public interface NoticeCrawlerRepositoryCustom {
    List<NoticeCrawler> findByCategoryNKeyword(String category, String keyword);
}
