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

    private Date GetToday()
    {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        Date today = c.getTime();
        return today;
    }

    private Boolean isToday(Date compareDate){
        Date today = GetToday();

        if(compareDate.equals(null)) {
            return false;
        }else if(today.compareTo(compareDate) == 0){
            return true;
        }
        return false;
    }

    private Boolean CheckNoticeExists(String findTitle){
        NoticeCrawler noticeCrawler = noticeCrawlerRepository.findByTitle(findTitle);

        if(noticeCrawler == null){
            return false;
        }else{
            return true;
        }
    }

    @PostConstruct
    public void getNoticeDatas() throws IOException{
        Document doc = Jsoup.parse(new URL(SOONGIL_UNIV_URL).openStream(), "UTF-8", SOONGIL_UNIV_URL);
        Elements contents = doc.select(".row.no-gutters.align-items-center");

        for(Element content: contents){
            Elements registDateContent = content.select(".notice_col1 div");
            Elements noticeContent = content.select(".notice_col3 a span span");
            Elements registerContent = content.select(".notice_col4");

            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy.MM.dd");
            String strRegistDate = registDateContent.text();
            Date registDate = null;
            try {
                registDate = transFormat.parse(strRegistDate);
            }catch(ParseException e) {
                registDate = null;
            }

            String category = noticeContent.get(0).text();
            String title = noticeContent.get(1).text();
            String register = registerContent.text();

            if(CheckNoticeExists(title)){
                break;
            }else{
                noticeCrawlerRepository.save(new NoticeCrawlerSaveRequestDto(registDate, category, title, register).toEntity());
            }
        }

    }

    @Transactional
    public Boolean crawl(){


        return true;
    }
}
