package com.duktown.domain.foodMenus;

import com.duktown.domain.foodMenus.MenuRepository;
import com.duktown.domain.foodMenus.foodMenu;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FoodMenuService {

    private final MenuRepository menuRepository;
    private static final Logger logger = LoggerFactory.getLogger(FoodMenuService.class);

    @Transactional
    public void crawlAndSaveMenu() {
        WebDriver driver = createChromeDriver();

        try {
            navigateToSchedulePage(driver);

            WebElement table = driver.findElement(By.id("schedule-table"));

            for (WebElement row : table.findElements(By.tagName("tr"))) {
                processMenuRow(row);
            }
        } catch (Exception e) {
            logger.error("크롤링 중에 오류가 발생했습니다.", e);
        } finally {
            closeWebDriver(driver);
        }
    }

    private WebDriver createChromeDriver() {
        String chromeDriverPath = "D:\\2.llbzllctct\\UMC\\Duktown\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

        return new ChromeDriver(options);
    }

    private void navigateToSchedulePage(WebDriver driver) {
        driver.get("https://www.duksung.ac.kr/diet/schedule.do");
    }

    private void processMenuRow(WebElement row) {
        WebElement typeCell = row.findElement(By.tagName("th"));
        String mealType = typeCell.getText();

        for (WebElement menuCell : row.findElements(By.tagName("td"))) {
            processMenuCell(mealType, menuCell);
        }
    }

    private void processMenuCell(String mealType, WebElement menuCell) {
        String[] menus = menuCell.getAttribute("innerHTML").split("<br>");
        for (String menu : menus) {
            foodMenu menuEntity = new foodMenu();
            menuEntity.setMealType(mealType);
            menuEntity.setMenu(menu);

            menuRepository.save(menuEntity);

            logger.info("크롤링된 메뉴: {}", menuEntity);
        }
    }

    private void closeWebDriver(WebDriver driver) {
        if (driver != null) {
            driver.quit();
        }
    }
}
