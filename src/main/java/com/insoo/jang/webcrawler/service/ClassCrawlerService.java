package com.insoo.jang.webcrawler.service;

import com.insoo.jang.webcrawler.domain.crawling.ClassCrawler;
import com.insoo.jang.webcrawler.module.DateModule;
import com.insoo.jang.webcrawler.web.dto.ClassCrawlerResponseDto;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ClassCrawlerService {
    private final String SOONGSIL_UNIV_LOGIN_URL="https://class.ssu.ac.kr/login";
    private final String SOONGSIL_UNIV_MYPAGE_URL="https://class.ssu.ac.kr/mypage";

    public void GetClassClawlerDatas() throws Exception{



    }

    public Connection.Response LoginToServer(String id, String password) throws Exception{

        Connection.Response login = Jsoup.connect(SOONGSIL_UNIV_LOGIN_URL)
                                    .data("id", id, "password", password)
                                    .method(Connection.Method.POST)
                                    .timeout(5000)
                                    .execute();

        return login;
    }

    public List<String> GetClassUrls(Connection.Response login) throws Exception{
        List<String> resultUrls = new ArrayList<String>();

        Document doc = Jsoup.connect(SOONGSIL_UNIV_MYPAGE_URL)
                .cookies(login.cookies())
                .timeout(3000000).get();

        Elements classUrls = doc.select(".xnch-link-wrapper.xnch-link");
        for(Element elem : classUrls){
            resultUrls.add(elem.attr("href"));
        }

        return resultUrls;
    }

    public List<ClassCrawlerResponseDto> GetClassDetail(Connection.Response login, String className, String url) throws  Exception{
        String classUrl = url + "/external_tools/2";
        List<ClassCrawlerResponseDto> returnValue = new ArrayList<ClassCrawlerResponseDto>();

        Document doc = Jsoup.connect(SOONGSIL_UNIV_MYPAGE_URL)
                .cookies(login.cookies())
                .timeout(3000000).get();


        Elements component = doc.select(".xncb-component-wrapper.xncb-component-main-wrapper");

        if(component.isEmpty()){
            return null;
        }

        for(Element elem : component){
            String title = elem.select(".xncb-component-title").text();
            String strStartDate = elem.select(".xncb-component-periods-wrapper.xncb-component-periods-item.xncb-component-periods-item-date").text();
            String strEndDate = elem.select(".xncb-component-periods-wrapper.xncb-component-periods-date.xncb-component-periods-item-date").text();

            Date today = DateModule.GetToday();

            SimpleDateFormat transFormat = new SimpleDateFormat("MM월 dd일 HH:mm");

            Date startDate = transFormat.parse(strStartDate);
            Date endDate = transFormat.parse(strEndDate);

            if(startDate.compareTo(today) > 1){
                break;
            }

            long timeDiff = endDate.getTime() - today.getTime();

            ClassCrawler newClass = ClassCrawler.builder().className(className).category("수업").leftTime(timeDiff).title(title).build();

            returnValue.add(new ClassCrawlerResponseDto(newClass));
        }

        return returnValue;
    }
}
