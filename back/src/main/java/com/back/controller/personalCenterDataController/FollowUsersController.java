package com.back.controller.personalCenterDataController;

import com.back.dto.ArticleAuthor;
import com.back.get.UserDataList.FollowUsersList;
import com.back.get.UserRenderingPreparation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/personalCenterData")
public class FollowUsersController {
    @Resource
    private FollowUsersList followUsersList;
    @Resource
    private UserRenderingPreparation userRenderingPreparation;
    @GetMapping("/getFollowUsers")
    public List<ArticleAuthor> getCollectArticles(@RequestParam int userId) {
        return userRenderingPreparation.getArticleAuthors(followUsersList.getUserDataList(userId));
    }
}
