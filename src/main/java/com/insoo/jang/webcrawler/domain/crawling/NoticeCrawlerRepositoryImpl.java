package com.insoo.jang.webcrawler.domain.crawling;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.insoo.jang.webcrawler.domain.crawling.QNoticeCrawler.noticeCrawler;

@RequiredArgsConstructor
public class NoticeCrawlerRepositoryImpl implements NoticeCrawlerRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<NoticeCrawler> findByCategory(String category){
        return queryFactory.selectFrom(noticeCrawler)
                .where(noticeCrawler.category.eq(category))
                .fetch();
    }
}
