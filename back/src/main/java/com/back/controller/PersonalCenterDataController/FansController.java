package com.back.controller.PersonalCenterDataController;

import com.back.dto.ArticleAuthor;
import com.back.get.UserDataList.FansList;
import com.back.get.UserRenderingPreparation;
import jakarta.annotation.Resource;
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
    @GetMapping("/getFans")
    public List<ArticleAuthor> getCollectArticles(@RequestParam int userId) {
        return userRenderingPreparation.getArticleAuthors(fansList.getUserDataList(userId));
    }
}
