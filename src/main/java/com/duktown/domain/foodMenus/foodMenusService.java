package com.duktown.domain.foodMenus;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class foodMenusService {
    private static final Logger logger = LoggerFactory.getLogger(foodMenusService.class);

    private static final String foodMenus_URL = "https://www.duksung.ac.kr/diet/schedule.do?startDate=2023-12-04&endDate=2023-12-08";

    @PostConstruct
    public void getFoodMenus() throws IOException {
        Document document = Jsoup.connect(foodMenus_URL).post();
        logger.info("document: {}", document);

        Element table = document.select("table#schedule-table").first();
        logger.info("Selected Table: {}", table);

        if (table != null) {
            Elements rows = table.select("tbody tr");

            for (Element row : rows) {
                Elements cells = row.select("td");

                // 각 셀에 액세스하여 메뉴 정보를 추출
                for (Element cell : cells) {
                    String menuText = cell.text(); // HTML 태그를 제거하고 텍스트만 가져옴
                    logger.info("Menu Text: {}", menuText);
                }
            }

        } else {
            logger.warn("페이지에서 테이블을 찾을 수 없습니다.");
        }
    }
}
