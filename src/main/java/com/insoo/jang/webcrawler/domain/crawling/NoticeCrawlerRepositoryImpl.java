package com.insoo.jang.webcrawler.domain.crawling;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.h2.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.insoo.jang.webcrawler.domain.crawling.QNoticeCrawler.noticeCrawler;

@Repository
@RequiredArgsConstructor
public class NoticeCrawlerRepositoryImpl implements NoticeCrawlerRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<NoticeCrawler> findByCategoryKeyword(String category, String keyword){
        return queryFactory.selectFrom(noticeCrawler)
                .where(eqCategory(category),
                        contanisRegisterKeyword(keyword).or(contanisTitleKeyword(keyword)))
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
