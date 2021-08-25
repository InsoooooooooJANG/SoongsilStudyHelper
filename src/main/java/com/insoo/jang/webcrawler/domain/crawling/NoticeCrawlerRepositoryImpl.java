package com.insoo.jang.webcrawler.domain.crawling;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.h2.util.StringUtils;

import java.util.List;

import static com.insoo.jang.webcrawler.domain.crawling.QNoticeCrawler.noticeCrawler;

@RequiredArgsConstructor
public class NoticeCrawlerRepositoryImpl implements NoticeCrawlerRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<NoticeCrawler> findByCategoryNKeyword(String category, String keyword){
        return queryFactory.selectFrom(noticeCrawler)
                .where(eqCategory(category),
                        contanisRegisterKeyword(keyword),
                        contanisTitleKeyword(keyword))
                .fetch();
    }

    private BooleanExpression eqCategory(String category){
        if(StringUtils.isNullOrEmpty(category)){
            return null;
        }
        return noticeCrawler.category.eq(category);
    }

    private BooleanExpression contanisRegisterKeyword(String keyword){
        if(StringUtils.isNullOrEmpty(keyword)){
            return null;
        }

        return noticeCrawler.register.contains(keyword);
    }

    private BooleanExpression contanisTitleKeyword(String keyword){
        if(StringUtils.isNullOrEmpty(keyword)){
            return null;
        }

        return noticeCrawler.title.contains(keyword);
    }


}
