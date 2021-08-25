package com.insoo.jang.webcrawler.service;

import com.insoo.jang.webcrawler.domain.crawling.NoticeCrawler;
import com.insoo.jang.webcrawler.domain.crawling.NoticeCrawlerRepository;
import com.insoo.jang.webcrawler.web.dto.NoticeCrawlerSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class NoticeCrawlerService {
    private final NoticeCrawlerRepository noticeCrawlerRepository;
    private final String SOONGIL_UNIV_URL="https://scatch.ssu.ac.kr/%ea%b3%b5%ec%a7%80%ec%82%ac%ed%95%ad/?f&keyword";

    @Transactional
    public Long save(NoticeCrawlerSaveRequestDto requestDto){
        return noticeCrawlerRepository.save(requestDto.toEntity()).getId();
    }


    private Boolean CheckNoticeExists(String findTitle){
        NoticeCrawler noticeCrawler = noticeCrawlerRepository.findByTitle(findTitle);

        if(noticeCrawler == null){
            return false;
        }else{
            return true;
        }
    }

    public void getNoticeDatas(){
        try {
            Document doc = Jsoup.parse(new URL(SOONGIL_UNIV_URL).openStream(), "UTF-8", SOONGIL_UNIV_URL);
            saveNoticeDatas(doc);
        }catch (Exception e){
            System.out.print(e.getMessage());
        }
    }

    @Transactional
    public void saveNoticeDatas(Document doc) throws IOException{
        Elements contents = doc.select(".row.no-gutters.align-items-center");

        for(Element content: contents){
            Elements registDateContent = content.select(".notice_col1 div");
            Elements noticeContent = content.select(".notice_col3 a span span");
            Elements registerContent = content.select(".notice_col4");

            String registDate = registDateContent.text();
            String category = noticeContent.get(0).text();
            String title = noticeContent.get(1).text();
            String register = registerContent.text();

            if(CheckNoticeExists(title)){
                break;
            }else{
                noticeCrawlerRepository.save(NoticeCrawler.builder().registDate(registDate)
                                                                    .category(category)
                                                                    .title(title)
                                                                    .register(register)
                                                                    .build());
            }
        }

    }

}
