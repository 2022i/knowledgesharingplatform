package com.back.controller.personalCenterDataController;

import com.back.dto.ArticleAuthor;
import com.back.get.UserDataList.FansList;
import com.back.get.preparation.UserRenderingPreparation;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/personalCenterData")
public class FansController {
    @Resource
    private FansList fansList;
    @Resource
    private UserRenderingPreparation userRenderingPreparation;
    @Description("个人中心接口，获取用户的粉丝列表，返回用户数据用于渲染")
    @GetMapping("/getFans")
    public List<ArticleAuthor> getFans(@RequestParam int userId) {
        return userRenderingPreparation.getArticleAuthors(fansList.getUserDataList(userId));
    }
}
