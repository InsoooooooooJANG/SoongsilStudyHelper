package com.insoo.jang.webcrawler.domain.crawling;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class NoticeCrawlerRepositoryImpl implements NoticeCrawlerRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<NoticeCrawler> findByCategory(String category){
        return queryFactory.selectFrom(noticecrawler)
                .where(noticecrawler.category.eq(category))
                .fetch();
    }
}
