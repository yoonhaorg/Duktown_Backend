package com.duktown.domain.foodMenus;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class foodMenusService {

    private final MenuRepository menuRepository;
    private static final Logger logger = LoggerFactory.getLogger(foodMenusService.class);

    @Transactional
    public void crawlAndSaveMenu() {
        String url = "https://www.duksung.ac.kr/diet/schedule.do";

        try {
            Document document = Jsoup.connect(url).post();
            //
            logger.info("document: {}", document);

            // 테이블의 ID가 schedule-table인 테이블을 선택
            Element table = document.selectFirst("#schedule-table");
            //
            logger.info("Selected Table: {}", table);

            if (table != null) {
                // 테이블의 모든 행을 선택
                //
                logger.info("Table HTML: {}", table.html());

                Elements rows = table.select("tr");

                for (Element row : rows) {
                    // 각 행의 첫 번째 셀에 있는 내용(학생식당, 교직원식당 등) 출력
                    Element typeCell = row.selectFirst("th[scope=row]");
                    String mealType = typeCell.text();

                    // 각 행의 나머지 셀에 있는 내용(월요일, 화요일, ...) 출력
                    Elements menuCells = row.select("td");
                    for (Element menuCell : menuCells) {
                        // 셀 내용에서 <br>을 개행문자로 변환하여 줄 단위로 출력
                        String[] menus = menuCell.html().split("<br>");
                        for (String menu : menus) {
                            // Menu 엔티티에 저장
                            foodMenu menuEntity = new foodMenu();
                            menuEntity.setMealType(mealType);
                            menuEntity.setMenu(menu);

                            // 엔티티 저장
                            menuRepository.save(menuEntity);

                            //
                            logger.info("Crawled menu: {}", menuEntity);
                        }
                    }
                }
            } else {
                logger.error("Table with id 'schedule-table' not found.");
            }

        } catch (IOException e) {
            logger.error("Error while crawling and saving menu data.", e);
        }
    }
}
