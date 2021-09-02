package com.insoo.jang.webcrawler.service;

import com.insoo.jang.webcrawler.domain.crawling.NoticeCrawler;
import com.insoo.jang.webcrawler.domain.crawling.NoticeCrawlerRepository;
import com.insoo.jang.webcrawler.web.dto.NoticeCrawlerResponseDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class NoticeCrawlerServiceTest {
    @Autowired
    NoticeCrawlerService noticeCrawlerService;

    @Autowired
    NoticeCrawlerRepository noticeCrawlerRepository;

    @AfterEach
    public void cleanup() {noticeCrawlerRepository.deleteAll();}

    @Test
    public void 크롤링_데이터가_저장된다(){
        //given
        Document document = Jsoup.parse("\n" +
                "<!doctype html>\n" +
                "<html lang=\"ko-KR\">\n" +
                "<head>\n" +
                "\n" +
                "</head>\n" +
                "\n" +
                "<body class=\"page-template-default page page-id-13 wp-embed-responsive wpb-js-composer js-comp-ver-6.0.3 vc_responsive\">\n" +
                "\n" +
                "<div class=\"row justify-content-center notice_wrap\">\n" +
                "\t<div class=\"col-12\">\n" +
                "\t\t<ul class=\"notice-lists\">\n" +
                "\t\t\t<li class=\"notice_head\">\n" +
                "\t\t\t\t<div class=\"row no-gutters\">\n" +
                "\t\t\t\t\t<div class=\"notice_col1\">작성일</div>\n" +
                "\t\t\t\t\t<div class=\"notice_col2\">상태</div>\n" +
                "\t\t\t\t\t<div class=\"notice_col3\">제목</div>\n" +
                "\t\t\t\t\t<div class=\"notice_col4\">등록부서</div>\n" +
                "\t\t\t\t\t<div class=\"notice_col5\">조회수</div>\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t</li>\n" +
                "\t\t\t\t\t\t\n" +
                "\t\t\t\t\t\n" +
                "\t\t\t<li class=\"start\">\n" +
                "\t\t\t\t\n" +
                "\t\t\t\t<div class=\"row no-gutters align-items-center\">\n" +
                "\t\t\t\t\t<div class=\"notice_col1 m-text-left\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"h2 text-info font-weight-bold\">2021.08.25</div>\n" +
                "\t\t\t\t\t\t\n" +
                "\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t<div class=\"notice_col2\">\n" +
                "\t\t\t\t\t\t<span class='tag ing'>진행</span>\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t<div class=\"notice_col3\">\n" +
                "\t\t\t\t\t\t<a href=\"https://scatch.ssu.ac.kr/%ea%b3%b5%ec%a7%80%ec%82%ac%ed%95%ad/?f&category&paged=1&slug=%EC%88%AD%EC%8B%A4%EB%8C%80%ED%95%99%EA%B5%90-%EA%B3%84%EC%95%BD%EC%A7%81-%EC%A7%81%EC%9B%90-%EB%AA%A8%EC%A7%91%EA%B5%AD%EC%A0%9C%ED%8C%80-8-30&keyword\" class=\"text-decoration-none d-block text-truncate\">\n" +
                "\t\t\t\t\t\t\t\n" +
                "\t\t\t\t\t\t\t<span>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<span class=\"label d-inline-blcok border pl-3 pr-3 mr-2\">채용</span>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\n" +
                "\t\t\t\t\t\t\t\t<span class=\"d-inline-blcok m-pt-5\">숭실대학교 계약직 직원 모집(국제팀, ~8/30)</span>\n" +
                "\t\t\t\t\t\t\t</span>\n" +
                "\t\t\t\t\t\t</a>\n" +
                "\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t<div class=\"notice_col4\">\n" +
                "\t\t\t\t\t\t총무·인사팀\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t<div class=\"notice_col5\">63</div>\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t</li>\n" +
                "\t\t</ul>\n" +
                "\t</div>\n" +
                "</div>\n" +
                "\n" +
                "</body>\n" +
                "</html>");

        String registDate = "2021.08.25";
        String category = "채용";
        String register ="총무·인사팀";
        String title ="숭실대학교 계약직 직원 모집(국제팀, ~8/30)";

        //when
        try {
            noticeCrawlerService.saveNoticeDatas(document);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        //then
        List<NoticeCrawler> crawlingList = noticeCrawlerRepository.findAll();

        NoticeCrawler crawlingData = crawlingList.get(0);

        assertThat(crawlingData.getRegistDate()).isEqualTo(registDate);
        assertThat(crawlingData.getCategory()).isEqualTo(category);
        assertThat(crawlingData.getRegister()).isEqualTo(register);
        assertThat(crawlingData.getTitle()).isEqualTo(title);
    }

    @Test
    public void 크롤링_데이터를_키워드로_검색한다(){
        //given
        Document document = Jsoup.parse("\n" +
                "<!doctype html>\n" +
                "<html lang=\"ko-KR\">\n" +
                "<head>\n" +
                "\n" +
                "</head>\n" +
                "\n" +
                "<body class=\"page-template-default page page-id-13 wp-embed-responsive wpb-js-composer js-comp-ver-6.0.3 vc_responsive\">\n" +
                "\n" +
                "<div class=\"row justify-content-center notice_wrap\">\n" +
                "\t<div class=\"col-12\">\n" +
                "\t\t<ul class=\"notice-lists\">\n" +
                "\t\t\t<li class=\"notice_head\">\n" +
                "\t\t\t\t<div class=\"row no-gutters\">\n" +
                "\t\t\t\t\t<div class=\"notice_col1\">작성일</div>\n" +
                "\t\t\t\t\t<div class=\"notice_col2\">상태</div>\n" +
                "\t\t\t\t\t<div class=\"notice_col3\">제목</div>\n" +
                "\t\t\t\t\t<div class=\"notice_col4\">등록부서</div>\n" +
                "\t\t\t\t\t<div class=\"notice_col5\">조회수</div>\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t</li>\n" +
                "\t\t\t\t\t\t\n" +
                "\t\t\t\t\t\n" +
                "\t\t\t<li class=\"start\">\n" +
                "\t\t\t\t\n" +
                "\t\t\t\t<div class=\"row no-gutters align-items-center\">\n" +
                "\t\t\t\t\t<div class=\"notice_col1 m-text-left\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"h2 text-info font-weight-bold\">2021.08.25</div>\n" +
                "\t\t\t\t\t\t\n" +
                "\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t<div class=\"notice_col2\">\n" +
                "\t\t\t\t\t\t<span class='tag ing'>진행</span>\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t<div class=\"notice_col3\">\n" +
                "\t\t\t\t\t\t<a href=\"https://scatch.ssu.ac.kr/%ea%b3%b5%ec%a7%80%ec%82%ac%ed%95%ad/?f&category&paged=1&slug=%EC%88%AD%EC%8B%A4%EB%8C%80%ED%95%99%EA%B5%90-%EA%B3%84%EC%95%BD%EC%A7%81-%EC%A7%81%EC%9B%90-%EB%AA%A8%EC%A7%91%EA%B5%AD%EC%A0%9C%ED%8C%80-8-30&keyword\" class=\"text-decoration-none d-block text-truncate\">\n" +
                "\t\t\t\t\t\t\t\n" +
                "\t\t\t\t\t\t\t<span>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<span class=\"label d-inline-blcok border pl-3 pr-3 mr-2\">채용</span>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\n" +
                "\t\t\t\t\t\t\t\t<span class=\"d-inline-blcok m-pt-5\">숭실대학교 계약직 직원 모집(국제팀, ~8/30)</span>\n" +
                "\t\t\t\t\t\t\t</span>\n" +
                "\t\t\t\t\t\t</a>\n" +
                "\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t<div class=\"notice_col4\">\n" +
                "\t\t\t\t\t\t총무·인사팀\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t<div class=\"notice_col5\">63</div>\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t</li>\n" +
                "\t\t</ul>\n" +
                "\t</div>\n" +
                "</div>\n" +
                "\n" +
                "</body>\n" +
                "</html>");

        String registDate = "2021.08.25";
        String category = "채용";
        String register ="총무·인사팀";
        String title ="숭실대학교 계약직 직원 모집(국제팀, ~8/30)";

        //when
        try {
            noticeCrawlerService.saveNoticeDatas(document);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        //then
        List<NoticeCrawlerResponseDto> response = noticeCrawlerService.getNotice("","숭실");

        NoticeCrawler crawlingData = response.get(0).toEntity();

        assertThat(crawlingData.getRegistDate()).isEqualTo(registDate);
        assertThat(crawlingData.getCategory()).isEqualTo(category);
        assertThat(crawlingData.getRegister()).isEqualTo(register);
        assertThat(crawlingData.getTitle()).isEqualTo(title);
    }

    @Test
    public void 크롤링_데이터를_잘못된_키워드로_검색한다(){
        //given
        Document document = Jsoup.parse("\n" +
                "<!doctype html>\n" +
                "<html lang=\"ko-KR\">\n" +
                "<head>\n" +
                "\n" +
                "</head>\n" +
                "\n" +
                "<body class=\"page-template-default page page-id-13 wp-embed-responsive wpb-js-composer js-comp-ver-6.0.3 vc_responsive\">\n" +
                "\n" +
                "<div class=\"row justify-content-center notice_wrap\">\n" +
                "\t<div class=\"col-12\">\n" +
                "\t\t<ul class=\"notice-lists\">\n" +
                "\t\t\t<li class=\"notice_head\">\n" +
                "\t\t\t\t<div class=\"row no-gutters\">\n" +
                "\t\t\t\t\t<div class=\"notice_col1\">작성일</div>\n" +
                "\t\t\t\t\t<div class=\"notice_col2\">상태</div>\n" +
                "\t\t\t\t\t<div class=\"notice_col3\">제목</div>\n" +
                "\t\t\t\t\t<div class=\"notice_col4\">등록부서</div>\n" +
                "\t\t\t\t\t<div class=\"notice_col5\">조회수</div>\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t</li>\n" +
                "\t\t\t\t\t\t\n" +
                "\t\t\t\t\t\n" +
                "\t\t\t<li class=\"start\">\n" +
                "\t\t\t\t\n" +
                "\t\t\t\t<div class=\"row no-gutters align-items-center\">\n" +
                "\t\t\t\t\t<div class=\"notice_col1 m-text-left\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"h2 text-info font-weight-bold\">2021.08.25</div>\n" +
                "\t\t\t\t\t\t\n" +
                "\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t<div class=\"notice_col2\">\n" +
                "\t\t\t\t\t\t<span class='tag ing'>진행</span>\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t<div class=\"notice_col3\">\n" +
                "\t\t\t\t\t\t<a href=\"https://scatch.ssu.ac.kr/%ea%b3%b5%ec%a7%80%ec%82%ac%ed%95%ad/?f&category&paged=1&slug=%EC%88%AD%EC%8B%A4%EB%8C%80%ED%95%99%EA%B5%90-%EA%B3%84%EC%95%BD%EC%A7%81-%EC%A7%81%EC%9B%90-%EB%AA%A8%EC%A7%91%EA%B5%AD%EC%A0%9C%ED%8C%80-8-30&keyword\" class=\"text-decoration-none d-block text-truncate\">\n" +
                "\t\t\t\t\t\t\t\n" +
                "\t\t\t\t\t\t\t<span>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<span class=\"label d-inline-blcok border pl-3 pr-3 mr-2\">채용</span>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\n" +
                "\t\t\t\t\t\t\t\t<span class=\"d-inline-blcok m-pt-5\">숭실대학교 계약직 직원 모집(국제팀, ~8/30)</span>\n" +
                "\t\t\t\t\t\t\t</span>\n" +
                "\t\t\t\t\t\t</a>\n" +
                "\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t<div class=\"notice_col4\">\n" +
                "\t\t\t\t\t\t총무·인사팀\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t<div class=\"notice_col5\">63</div>\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t</li>\n" +
                "\t\t</ul>\n" +
                "\t</div>\n" +
                "</div>\n" +
                "\n" +
                "</body>\n" +
                "</html>");

        String registDate = "2021.08.25";
        String category = "채용";
        String register ="총무·인사팀";
        String title ="숭실대학교 계약직 직원 모집(국제팀, ~8/30)";

        //when
        try {
            noticeCrawlerService.saveNoticeDatas(document);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        //then
        List<NoticeCrawlerResponseDto> response = noticeCrawlerService.getNotice("","얍");

        assertThat(response).isNullOrEmpty();
    }
}
