package com.duktown.domain.foodMenus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jsoup")
@RequiredArgsConstructor
public class Controller {

    private final foodMenusService foodMenusService;

    @GetMapping("/getFoodMenus")
    public String getFoodMenus() {
        try {
            foodMenusService.getFoodMenus();
            return "Successfully fetched food menus!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching food menus. Check the logs for details.";
        }
    }

}