package com.insoo.jang.webcrawler.service;

import com.insoo.jang.webcrawler.web.dto.ClassCrawlerResponseDto;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ClassCrawlerService {
    private final String SOONGSIL_UNIV_LOGIN_URL="https://class.ssu.ac.kr/login";
    private final String SOONGSIL_UNIV_MYPAGE_URL="https://class.ssu.ac.kr/mypage";

    public void GetClassClawlerDatas() throws Exception{



    }

    public Connection.Response LoginToServer() throws Exception{
        String id = "20180435";
        String password = "Qaeldkah9./";

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
}
