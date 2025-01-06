package com.back.controller.personalCenterDataController;

import com.back.dto.PersonalAchievements;
import com.back.get.PersonalAchievementsPreparation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/personalCenterData")
public class PersonalAchievementsController {
    @Resource
    private PersonalAchievementsPreparation personalAchievementsPreparation;
    @GetMapping("/getPersonalAchievements")
    public PersonalAchievements getPersonalAchievements(int userId) {
        return personalAchievementsPreparation.getPersonalAchievements(userId);
    }
}
