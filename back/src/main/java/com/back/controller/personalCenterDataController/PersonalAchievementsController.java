package com.back.controller.personalCenterDataController;

import com.back.dto.PersonalAchievements;
import com.back.get.preparation.PersonalAchievementsPreparation;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
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
    @Description("个人中心接口，获取用户的个人成就信息")
    @GetMapping("/getPersonalAchievements")
    public PersonalAchievements getPersonalAchievements(int userId) {
        return personalAchievementsPreparation.getPersonalAchievements(userId);
    }
}
